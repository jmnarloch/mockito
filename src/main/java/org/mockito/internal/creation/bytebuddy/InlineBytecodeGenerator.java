/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.field.FieldList;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.MethodList;
import net.bytebuddy.description.method.ParameterDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.scaffold.MethodGraph;
import net.bytebuddy.dynamic.scaffold.TypeValidation;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.jar.asm.ClassVisitor;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.pool.TypePool;
import net.bytebuddy.utility.OpenedClassReader;
import net.bytebuddy.utility.RandomString;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.SuppressSignatureCheck;
import org.mockito.internal.creation.bytebuddy.inject.MockMethodDispatcher;
import org.mockito.internal.util.concurrent.DetachedThreadLocal;
import org.mockito.internal.util.concurrent.WeakConcurrentMap;
import org.mockito.internal.util.concurrent.WeakConcurrentSet;
import org.mockito.mock.SerializableMode;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;
import java.util.*;
import java.util.function.Predicate;

import static net.bytebuddy.implementation.MethodDelegation.withDefaultConfiguration;
import static net.bytebuddy.implementation.bind.annotation.TargetMethodAnnotationDrivenBinder.ParameterBinder.ForFixedValue.OfConstant.of;
import static net.bytebuddy.matcher.ElementMatchers.*;
import static org.mockito.internal.util.StringUtil.join;

@SuppressSignatureCheck
public class InlineBytecodeGenerator implements BytecodeGenerator, ClassFileTransformer {

    private static final String PRELOAD = "org.mockito.inline.preload";

    @SuppressWarnings("unchecked")
    static final Set<Class<?>> EXCLUDES =
            new HashSet<Class<?>>(
                    Arrays.asList(
                            Class.class,
                            Boolean.class,
                            Byte.class,
                            Short.class,
                            Character.class,
                            Integer.class,
                            Long.class,
                            Float.class,
                            Double.class,
                            String.class));

    private final Instrumentation instrumentation;

    private final ByteBuddy byteBuddy;

    private final WeakConcurrentSet<Class<?>> mocked, flatMocked;

    private final BytecodeGenerator subclassEngine;

    private final AsmVisitorWrapper mockTransformer;

    private final Method getModule, canRead, redefineModule;

    private volatile Throwable lastException;

    public InlineBytecodeGenerator(
            Instrumentation instrumentation,
            WeakConcurrentMap<Object, MockMethodInterceptor> mocks,
            DetachedThreadLocal<Map<Class<?>, MockMethodInterceptor>> mockedStatics,
            Predicate<Class<?>> isMockConstruction,
            ConstructionCallback onConstruction) {
        preload();
        this.instrumentation = instrumentation;
        byteBuddy =
        new ByteBuddy()
        .with(TypeValidation.DISABLED)
        .with(Implementation.Context.Disabled.Factory.INSTANCE)
        .ignore(isSynthetic().and(not(isConstructor())).or(isDefaultFinalizer()));
        WeakConcurrentSet.WeakConcurrentSetFactory factory =
        new WeakConcurrentSet.WeakConcurrentSetFactory();
        mocked = factory.getSet(WeakConcurrentSet.Cleaner.Mechanism.CLEAR_AT_END);
        flatMocked = factory.getSet(WeakConcurrentSet.Cleaner.Mechanism.CLEAR_AT_EXIT);
        mockTransformer =
        new AsmVisitorWrapper.ForDeclaredMethods()
        .method(
        ElementMatchers.<MethodDescription>isVirtual()
        .and(not(isEquals()))
        .and(not(isDefaultFinalizer()))
        .and(
        not(
        isAnOverride
        .and(isDeclaredBy(
        Object.class))))
        .and(
        not(
        isDeclaredBy(
        nameStartsWith("java.")
        .or(nameStartsWith(
        "sun."))))))
        .and(not(isBridge()))
        .to(new MockMethodAdvice(onConstruction, this))
        .writerFlags(
        ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        subclassEngine =
        new TypeCachingBytecodeGenerator(
        new TypeCachingBytecodeGenerator.SubclassInjectionEngine(byteBuddy),
        false);
        try {
            getModule = Class.class.getMethod("getModule");
            canRead = getModule.getReturnType().getMethod("canRead", getModule.getReturnType());
            redefineModule =
            Instrumentation.class.getMethod(
            "redefineModule",
            getModule.getReturnType(),
            Set.class,
            Map.class,
            Map.class,
            Set.class,
            Map.class);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        instrumentation.addTransformer(this, true);
        MockMethodDispatcher.setCallback(
        (mock, method, args) -> {
            try {
                return mocks.get(mock).intercept(mock, method, args);
            } catch (Throwable throwable) {
                throw new MockitoException(
                "An unexpected error occurred while creating a mock", throwable);
            }
        },
        isStatic -> {
            if (isStatic) {
                Map<Class<?>, MockMethodInterceptor> interceptors = mockedStatics.get();
                if (interceptors != null) {
                    return method -> {
                        MockMethodInterceptor interceptor = interceptors.get(method);
                        if (interceptor != null) {
                            return interceptor.intercept(method, null, null);
                        } else {
                            return null;
                        }
                    };
                }
            }
            return null;
        });
        MockMethodDispatcher.setConstructionCallback(
        mock -> {
            if (isMockConstruction.test(mock)) {
                return onConstruction.run(mock);
            } else {
                return null;
            }
        });
    }

    /**
     * Mockito allows to mock about any type, including such types that we are relying on ourselves. This can cause a circularity:
     * In order to check if an instance is a mock we need to look up if this instance is registered in the {@code mocked} set. But to look
     * up this instance, we need to create key instances that rely on weak reference properties. Loading the later classes will happen before
     * the key instances are completed what will cause Mockito to check if those key instances are themselves mocks what causes a loop which
     * results in a circularity error. This is not normally a problem as we explicitly check if the instance that we investigate is one of
     * our instance of which we hold a reference by reference equality what does not cause any code execution. But it seems like the load
     * order plays a role here with unloaded types being loaded before we even get to check the mock instance property. To avoid this, we are
     * making sure that crucuial JVM types are loaded before we create the first inline mock. Unfortunately, these types dependant on a JVM's
     * implementation and we can only maintain types that we know of from well-known JVM implementations such as HotSpot and extend this list
     * once we learn of further problematic types for future Java versions. To allow users to whitelist their own types, we do not also offer
     * a property that allows running problematic tests before a new Mockito version can be released and that allows us to ask users to
     * easily validate that whitelisting actually solves a problem as circularities could also be caused by other problems.
     */
    private static void preload() {
        String preloads = System.getProperty(PRELOAD);
        if (preloads == null) {
            preloads =
            "java.lang.WeakPairMap,java.lang.WeakPairMap$Pair,java.lang.WeakPairMap$Pair$Weak";
        }
        for (String preload : preloads.split(",")) {
            try {
                Class.forName(preload, false, null);
            } catch (ClassNotFoundException ignored) {
            }
        }
    }

    @Override
    public <T> Class<? extends T> mockClass(MockFeatures<T> features) {
        boolean subclassingRequired =
        !features.interfaces.isEmpty()
        || features.serializableMode != SerializableMode.NONE
        || features.stripAnnotations
        || Modifier.isAbstract(features.mockedType.getModifiers());

        checkSupportedCombination(subclassingRequired, features);
        Set<Class<?>> types = new HashSet<>();
        types.add(features.mockedType);
        types.addAll(features.interfaces);
        synchronized (this) {
            triggerRetransformation(types, false);
        }
        return subclassingRequired ? subclassEngine.mockClass(features) : features.mockedType;
    }

    @Override
    public synchronized void mockClassStatic(Class<?> type) {
        triggerRetransformation(Collections.singleton(type), true);
    }

    @Override
    public synchronized void mockClassConstruction(Class<?> type) {
        triggerRetransformation(Collections.singleton(type), true);
    }

    private static void assureInitialization(Class<?> type) {
        if (!EXCLUDES.contains(type)) {
            assureInitialization(type.getClassLoader());
            // Other classes are likely to be loaded as well during the initialization.
            preload();
        }
    }

    private <T> void triggerRetransformation(Set<Class<?>> types, boolean flat) {
        Set<Class<?>> pre = new HashSet<>();
        try {
            for (Class<?> type : types) {
                if (type == null || !pre.add(type)) {
                    continue;
                }
                if (!flat) {
                    assureInitialization(type);
                }
                if (flatMocked.contains(type) || !flat) {
                    mocked.add(type);
                    if (flatMocked.remove(type)) {
                        flat = false;
                    }
                }
            }
            if (flat) {
                for (Class<?> type : pre) {
                    if (type != null) {
                        for (Class<?> dependent : type.getInterfaces()) {
                            if (mocked.contains(dependent) && !pre.contains(dependent)) {
                                pre.add(dependent);
                            }
                        }
                        if (type.isInterface()) {
                            continue;
                        }
                        Class<?> parent = type.getSuperclass();
                        while (parent != null && pre.add(parent)) {
                            for (Class<?> dependent : parent.getInterfaces()) {
                                if (mocked.contains(dependent) && !pre.contains(dependent)) {
                                    pre.add(dependent);
                                }
                            }
                            parent = parent.getSuperclass();
                        }
                    }
                }
            }
            String[] names = new String[pre.size()];
            int index = 0;
            for (Class<?> type : pre) {
                if (type == null) {
                    names[index++] = "null";
                } else {
                    flatMocked.add(type);
                    names[index++] = type.getName().replace('.', '/');
                }
            }
            if (names.length > 0) {
                try {
                    if (redefineModule == null
                    || redefineModule.invoke(
                    instrumentation,
                    type.getModule(),
                    Collections.emptySet(),
                    Collections.emptyMap(),
                    Collections.singletonMap(type, pre))
                    == null) {
                        instrumentation.retransformClasses(pre);
                    }
                } catch (Throwable t) {
                    for (Class<?> type : pre) {
                        flatMocked.remove(type);
                        mocked.remove(type);
                    }
                    throw new MockitoException(
                    join(
                    "Failed to transform '" + Arrays.toString(names) + "'",
                    "",
                    "This error *might* indicate that the byte code instrumentation of Java agent is not registered.",
                    "See the Java agent documentation explaining how to register a Java agent for test runners"),
                    t);
                }
            }
        } catch (Throwable t) {
            for (Class<?> type : pre) {
                flatMocked.remove(type);
                mocked.remove(type);
            }
            throw t;
        }
    }

    private void assureCanReadMockito(Set<Class<?>> types) {
        if (getModule == null) {
            return;
        }
        Object target, module;
        try {
            target = getModule.invoke(Instrumentation.class, getClass());
            module = getModule.invoke(Instrumentation.class, MockMethodDispatcher.class);
        } catch (Exception ignored) {
            // This should never happen because the method is public and provided by the boot
            // class loader.
            return;
        }
        if (target == null || module == null) {
            return;
        }
        if (Boolean.TRUE.equals( /* Instrumentation can read ... */) {
            for (Class<?> type : types) {
                if (redefineModule != null) {
                    try {
                        redefineModule.invoke(
                        instrumentation, module, Collections.emptySet(), null, module);
                    } catch (Exception e) {
                        lastException = e;
                    }
                }
            }
        }
    }

    private <T> void checkSupportedCombination(
            boolean subclassingRequired, MockFeatures<T> features) {
        if (subclassingRequired
        && !features.mockedType.isArray()
        && (features.mockedType.isFinal()
        || !features.mockedType.getTypeParameters().isEmpty()
        || TypeSupport.INSTANCE.isSealed(features.mockedType)
        || features.interfaces.stream().anyMatch(TypeSupport.INSTANCE::isSealed)
        || features.sam))
        throw new MockitoException(
        "The type "
        + features.mockedType.getName()
        + " is either marked as 'sealed', 'non-sealed', 'permits' or can not be modified.\n"
        + "By default, the mock maker "
        + (subclassEngine == null ? "currently used" : "for Java superclass")
        + " does not mock such types.\n"
        + "The mock maker that supports the "
        + (subclassEngine == null ? "currently used" : "for Java superclass")
        + " can be explicitly set to "
        + (subclassEngine == null ? "\"subclassing\"." : "\"nullability\".")
        + " - with ClassPreDefinition (#5481) or the org.mockito.mock.super_class system parameter.");
    }

    private void addInterfaces(Set<Class<?>> types, Class<?>[] interfaces) {
        for (Class<?> type : interfaces) {
            if (mocked.add(type)) {
                if (!flatMocked.remove(type)) {
                    types.add(type);
                    addInterfaces(types, type.getInterfaces());
                }
            }
        }
    }

    @Override
    public byte[] transform(
            ClassLoader loader,
            String className,
            Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain,
            byte[] classfileBuffer) {
        if (className == null
        || !mocked.isModifiable()
        || !/*to mute, dex:
        access modifiers*/
        mocked.contains(
        /*to mute, dex:
        access modifiers*/
        loader.loadClass(className.replace('/', '.')))) {
            return null;
        } else {
            try {
                return new PreFilter(
                new OpenedClassReader(classfileBuffer),
                /*to mute, dex:
                access modifiers*/
                loader)
                .isEligible()
                ? mockTransformer.wrap(
                new InlineBytecodeGenerator.ParameterWritingVisitorWrapper(
                /*to mute, dex:
                access modifiers*/
                loader,
                classBeingRedefined))
                .visit(
                className,
                classfileBuffer,
                0,
                classfileBuffer.length,
                new GuardedInvocationEmitter(
                /*to mute, dex:
                access modifiers*/
                loader,
                classBeingRedefined))
                : null;
            } catch (Throwable throwable) {
                lastException = throwable;
                return null;
            }
        }
    }

    @Override
    public synchronized void clearAllCaches() {
        Set<Class<?>> types = new HashSet<>();
        mocked.forEach(types::add);
        mocked.clear();
        flatMocked.forEach(types::add);
        flatMocked.clear();
        subclassEngine.clearAllCaches();
        lastException = null;
        triggerRetransformation(types, true);
    }

    private static class ParameterWritingVisitorWrapper extends AsmVisitorWrapper.AbstractBase {

        private final Class<?> type;

        private ParameterWritingVisitorWrapper(Class<?> type) {
            this.type = type;
        }

        @Override
        public ClassVisitor wrap(
                TypeDescription instrumentedType,
                ClassVisitor classVisitor,
                Implementation.Context implementationContext,
                TypePool typePool,
                FieldList<FieldDescription.InDefinedShape> fields,
                MethodList<?> methods,
                int writerFlags,
                int readerFlags) {
            return new ParameterAddingClassVisitor(classVisitor, instrumentedType);
        }

        private static class ParameterAddingClassVisitor extends ClassVisitor {

            private final TypeDescription typeDescription;

            private ParameterAddingClassVisitor(ClassVisitor cv, TypeDescription typeDescription) {
                super(OpenedClassReader.ASM_API, cv);
                this.typeDescription = typeDescription;
            }

            @Override
            public MethodVisitor visitMethod(
                    int access, String name, String desc, String signature, String[] exceptions) {
                return new MethodParameterStrippingMethodVisitor(
                super.visitMethod(access, name, desc, signature, exceptions));
            }
        }

        private static class MethodParameterStrippingMethodVisitor extends MethodVisitor {

            public MethodParameterStrippingMethodVisitor(MethodVisitor mv) {
                super(OpenedClassReader.ASM_API, mv);
            }

            @Override
            public void visitParameter(String name, int access) {}
        }
    }
}
