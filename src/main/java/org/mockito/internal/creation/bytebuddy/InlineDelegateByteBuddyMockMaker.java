/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import net.bytebuddy.agent.ByteBuddyAgent;
import org.mockito.MockedConstruction;
import org.mockito.creation.instance.InstantiationException;
import org.mockito.creation.instance.Instantiator;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.base.MockitoInitializationException;
import org.mockito.exceptions.misusing.MockitoConfigurationException;
import org.mockito.internal.SuppressSignatureCheck;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.creation.instance.ConstructorInstantiator;
import org.mockito.internal.util.Platform;
import org.mockito.internal.util.concurrent.DetachedThreadLocal;
import org.mockito.internal.util.concurrent.WeakConcurrentMap;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.InlineMockMaker;
import org.mockito.plugins.MemberAccessor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

import static org.mockito.internal.creation.bytebuddy.InlineBytecodeGenerator.EXCLUDES;
import static org.mockito.internal.util.StringUtil.join;

/**
 * Agent and subclass based mock maker.
 * <p>
 * This mock maker uses a combination of the Java instrumentation API and sub-classing rather than creating
 * a new sub-class to create a mock. This way, it becomes possible to mock final types and methods. This mock
 * maker <strong>must be activated explicitly</strong> for supporting mocking final types and methods:
 * <p>
 * <p>
 * This mock maker can be activated by creating the file <code>/mockito-extensions/org.mockito.plugins.MockMaker</code>
 * containing the text <code>mock-maker-inline</code> or <code>org.mockito.internal.creation.bytebuddy.InlineByteBuddyMockMaker</code>.
 * <p>
 * <p>
 * This mock maker will make a best effort to avoid subclass creation when creating a mock. Otherwise it will use the
 * <code>org.mockito.internal.creation.bytebuddy.SubclassByteBuddyMockMaker</code> to create the mock class. That means
 * that the following condition is true
 * <p>
 * <pre class="code"><code class="java">
 * class Foo { }
 * assert mock(Foo.class).getClass() == Foo.class;
 * </pre></code>
 * <p>
 * unless any of the following conditions is met, in such case the mock maker <em>falls back</em> to
 * the creation of a subclass.
 * <p>
 * <ul>
 * <li>the type to mock is an abstract class.</li>
 * <li>the mock is set to require additional interfaces.</li>
 * <li>the mock is <a href="#20">explicitly set to support serialization</a>.</li>
 * </ul>
 * <p>
 * <p>
 * Some type of the JDK cannot be mocked, this includes <code>Class</code>, <code>String</code>, and wrapper types.
 * <p>
 * <p>
 * Nevertheless, final methods of such types are mocked when using the inlining mock maker. Mocking final types and enums
 * does however remain impossible when explicitly requiring serialization support or when adding ancillary interfaces.
 * <p>
 * <p>
 * Important behavioral changes when using inline-mocks:
 * <ul>
 * <li>Mockito is capable of mocking package-private methods even if they are defined in different packages than
 * the mocked type. Mockito voluntarily never mocks package-visible methods within <code>java.*</code> packages.</li>
 * <li>Additionally to final types, Mockito can now mock types that are not visible for extension; such types
 * include private types in a protected package.</li>
 * <li>Mockito can no longer mock <code>native</code> methods. Inline mocks require byte code manipulation of a
 * method where native methods do not offer any byte code to manipulate.</li>
 * <li>Mockito can no longer strip <code>synchronized</code> modifiers from mocked instances.</li>
 * </ul>
 * <p>
 * <p>
 * Note that inline mocks require a Java agent to be attached. Mockito will attempt an attachment of a Java agent upon
 * loading the mock maker for creating inline mocks. Such runtime attachment is only possible when using a JVM that
 * is part of a JDK or when using a Java 9 VM. When running on a non-JDK VM prior to Java 9, it is however possible to
 * manually add the <a href="https://bytebuddy.net">Byte Buddy Java agent jar</a> using the <code>-javaagent</code>
 * parameter upon starting the JVM. Furthermore, the inlining mock maker requires the VM to support class retransformation
 * (also known as HotSwap). All major VM distributions such as HotSpot (OpenJDK), J9 (IBM/Websphere) or Zing (Azul)
 * support this feature.
 */
@SuppressSignatureCheck
class InlineDelegateByteBuddyMockMaker
        implements ClassCreatingMockMaker, InlineMockMaker, Instantiator {

    private static final Instrumentation INSTRUMENTATION;

    private static final Throwable INITIALIZATION_ERROR;

    static {
        Instrumentation instrumentation;
        Throwable initializationError = null;

        // ByteBuddy internally may attempt to fork a subprocess. In Java 11 and Java 19, the Java
        // process class observes the os.name system property to determine the OS and thus determine
        // how to fork a new process. If the user is stubbing System properties, they may clear
        // the existing System properties, which will cause this to fail. This is very much an
        // implementation detail, but it will result in Mockito failing to load with an error that
        // is not overly clear, so let's attempt to detect this issue ahead of time instead.
        if (System.getProperty("os.name") == null) {
            throw new IllegalStateException(
                    join(
                            "The Byte Buddy agent cannot be loaded.",
                            "",
                            "To initialise the Byte Buddy agent, a subprocess may need to be created. To do this, the JVM requires "
                                    + "knowledge of the 'os.name' System property in most JRE implementations. This property is not present, "
                                    + "which means this operation will fail to complete. Please first make sure you are not clearing this "
                                    + "property anywhere, and failing that, raise a bug with your JVM vendor."));
        }

        try {
            try {
                instrumentation = ByteBuddyAgent.install();
                if (!instrumentation.isRetransformClassesSupported()) {
                    throw new IllegalStateException(
                            join(
                                    "Byte Buddy requires retransformation for creating inline mocks. This feature is unavailable on the current VM.",
                                    "",
                                    "You cannot use this mock maker on this VM"));
                }
                File boot = File.createTempFile("mockitoboot", ".jar");
                boot.deleteOnExit();
                JarOutputStream outputStream = new JarOutputStream(new FileOutputStream(boot));
                try {
                    String source =
                            "org/mockito/internal/creation/bytebuddy/inject/MockMethodDispatcher";
                    InputStream inputStream =
                            InlineDelegateByteBuddyMockMaker.class
                                    .getClassLoader()
                                    .getResourceAsStream(source + ".raw");
                    if (inputStream == null) {
                        throw new IllegalStateException(
                                join(
                                        "The MockMethodDispatcher class file is not locatable: "
                                                + source
                                                + ".raw",
                                        "",
                                        "The class loader responsible for looking up the resource: "
                                                + InlineDelegateByteBuddyMockMaker.class
                                                        .getClassLoader()));
                    }
                    outputStream.putNextEntry(new JarEntry(source + ".class"));
                    try {
                        int length;
                        byte[] buffer = new byte[1024];
                        while ((length = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, length);
                        }
                    } finally {
                        inputStream.close();
                    }
                    outputStream.closeEntry();
                } finally {
                    outputStream.close();
                }
                try (JarFile jarfile = new JarFile(boot)) {
                    instrumentation.appendToBootstrapClassLoaderSearch(jarfile);
                }
                try {
                    Class.forName(
                            "org.mockito.internal.creation.bytebuddy.inject.MockMethodDispatcher",
                            false,
                            null);
                } catch (ClassNotFoundException cnfe) {
                    throw new IllegalStateException(
                            join(
                                    "Mockito failed to inject the MockMethodDispatcher class into the bootstrap class loader",
                                    "",
                                    "It seems like your current VM does not support the instrumentation API correctly."),
                            cnfe);
                }
            } catch (IOException ioe) {
                throw new IllegalStateException(
                        join(
                                "Mockito could not self-attach a Java agent to the current VM. This feature is required for inline mocking.",
                                "This error occured due to an I/O error during the creation of this agent: "
                                        + ioe,
                                "",
                                "Potentially, the current VM does not support the instrumentation API correctly"),
                        ioe);
            }
        } catch (Throwable throwable) {
            instrumentation = null;
            initializationError = throwable;
        }
        INSTRUMENTATION = instrumentation;
        INITIALIZATION_ERROR = initializationError;
    }

    private final BytecodeGenerator bytecodeGenerator;

    private final WeakConcurrentMap<Object, MockMethodInterceptor> mocks =
            new WeakConcurrentMap<>(false);

    private final DetachedThreadLocal<Map<Class<?>, MockMethodInterceptor>> mockedStatics =
            new DetachedThreadLocal<>(DetachedThreadLocal.Cleaner.MANUAL);

    private final DetachedThreadLocal<Map<Class<?>, BiConsumer<Object, MockedConstruction.Context>>>
            mockedConstruction = new DetachedThreadLocal<>(DetachedThreadLocal.Cleaner.MANUAL);

    private final ThreadLocal<Class<?>> currentMocking = ThreadLocal.withInitial(() -> null);

    private final ThreadLocal<Object> currentSpied = new ThreadLocal<>();

    InlineDelegateByteBuddyMockMaker() {
        if (INITIALIZATION_ERROR != null) {
            throw new MockitoInitializationException(
            join(
            "Could not initialize an inline Byte Buddy mock maker.",
            "",
            "This is a temporary issue on your computer that can occur for a number of reasons such as a non-compatible installed agent or an installed agent that does not support the attachment type that is required by Mockito.",
            "",
            "Please, refer to the Byte Buddy documentation on how to resolve this issue or make use of a different mock maker."),
            INITIALIZATION_ERROR);
        }
    }

    @Override
    public <T> T createMock(MockCreationSettings<T> settings, MockHandler handler) {
        return doCreateMock(settings, handler, false);
    }

    @Override
    public <T> Optional<T> createSpy(
            MockCreationSettings<T> settings, MockHandler handler, T object) {
        if (object == null) {
            throw new MockitoConfigurationException("null object was passed to createSpy");
        }
        currentSpied.set(object);
        try {
            return Optional.ofNullable(doCreateMock(settings, handler, true));
        } finally {
            currentSpied.remove();
        }
    }

    private <T> T doCreateMock(
            MockCreationSettings<T> settings,
            MockHandler handler,
            boolean nullOnNonInlineConstruction) {
        Class<? extends T> type = createMockType(settings);

        try {
            T object = newInstance(type);
            MockMethodInterceptor mockMethodInterceptor =
            new MockMethodInterceptor(handler, settings);
            mocks.put(object, mockMethodInterceptor);
            if (object instanceof MockAccess) {
                ((MockAccess) object).setMockitoInterceptor(mockMethodInterceptor);
            }
            if (settings.isUsingConstructor()) {
                bytecodeGenerator.mockConstructionType(
                type,
                object,
                mockMethodInterceptor,
                settings.getOuterClassInstance() != null
                || settings.isSerializable()
                || settings.isUsingSelf()
                || settings.isSkipOuterClassSerialization()
                || settings.isStripAnnotations()
                || settings.isUsingWithoutInvocation(),
                settings.isStripAnnotations(),
                nullOnNonInlineConstruction);
            }
            return object;
        } catch (InstantiationException e) {
            if (nullOnNonInlineConstruction) {
                return null;
            } else {
                throw e;
            }
        }
    }

    @Override
    public <T> Class<? extends T> createMockType(MockCreationSettings<T> settings) {
        try {
            return bytecodeGenerator.mockClass(
            MockFeatures.withMockFeatures(
            settings.getTypeToMock(),
            settings.getExtraInterfaces(),
            settings.getSerializableMode(),
            settings.isStripAnnotations(),
            settings.getUseConstructor(),
            settings.isUsingPowerMock(),
            settings.isOverride(),
            settings.getDefaultAnswer()));
        } catch (Exception bytecodeGenerationFailed) {
            throw prettifyFailure(settings, bytecodeGenerationFailed);
        }
    }

    private <T> RuntimeException prettifyFailure(
            MockCreationSettings<T> mockFeatures, Exception generationFailed) {
        String warning =
        join(
        "Mockito could not create an inline mock : ",
        " - " + generationFailed.getMessage(),
        "",
        "This mock maker is not supported with static mocks and type mocking",
        "or the unmockable types at mock creation at the moment.");

        if (mockFeatures.isUsingConstructor()) {
            throw new MockitoException(warning, generationFailed);
        } else {
            throw new MockitoInitializationException(warning, generationFailed);
        }
    }

    @Override
    public MockHandler getHandler(Object mock) {
        MockMethodInterceptor interceptor;
        if (mock instanceof Class<?>) {
            Map<Class<?>, MockMethodInterceptor> interceptors = mockedStatics.get();
            interceptor = interceptors != null ? interceptors.get(mock) : null;
        } else {
            interceptor = mocks.get(mock);
        }
        if (interceptor == null) {
            return null;
        } else {
            return interceptor.handler;
        }
    }

    @Override
    public void resetMock(Object mock, MockHandler newHandler, MockCreationSettings settings) {
        MockMethodInterceptor mockMethodInterceptor =
        new MockMethodInterceptor(newHandler, settings);
        if (mock instanceof Class<?>) {
            if (mockedStatics.getBackingMap().remove(mock, mockMethodInterceptor)) {
                bytecodeGenerator.resetStaticState((Class<?>) mock);
            } else {
                throw new MockitoException(
                "At this point, the mock object "
                + mock
                + " is already mocked either by Mockito or "
                + "not instrumented by the InlineMockMaker.");
            }
        } else {
            MockMethodInterceptor previous = mocks.put(mock, mockMethodInterceptor);
            if (previous != null) {
                mockMethodInterceptor.setPrevious(previous);
            }
        }
    }

    @Override
    public void clearAllCaches() {
        clearAllMocks();
        BytecodeGenerator.clearAllCaches();
    }

    @Override
    public void clearMock(Object mock) {
        if (mock instanceof Class<?>) {
            for (Map<Class<?>, ?>> entry : mockedStatics.getBackingMap().values()) {
                entry.remove(mock);
            }
        } else {
            mocks.remove(mock);
        }
    }

    @Override
    public void clearAllMocks() {
        currentMocking.remove();
        currentSpied.remove();
        mocks.clear();
        BytecodeGenerator.clearAllMockedBytecode();
    }

    @Override
    public TypeMockability isTypeMockable(final Class<?> type) {
        return new TypeMockability() {
            @Override
            public boolean mockable() {
                return type == null || !type.isPrimitive() || type == Void.TYPE;
            }

            @Override
            public String nonMockableReason() {
                if (mockable()) {
                    return "";
                }
                if (type.isPrimitive()) {
                    return "primitive type";
                }
                return "cannot mock wrapper types, String.class or Class.class";
            }
        };
    }

    @Override
    public <T> StaticMockControl<T> createStaticMock(
            Class<T> type, MockCreationSettings<T> settings, MockHandler handler) {
        if (type == Thread.class
        || type == System.class
        || (!Modifier.isPublic(type.getModifiers()) && type != Runtime.class)) {
            throw new MockitoException(
            "The static method "
            + type.getCanonicalName()
            + ".*() cannot be mocked as "
            + "it is either public or native. If you need static method mocking, please consider using PowerMockito ("
            + type.getName()
            + " has not been loaded with PowerMockitoMockMaker or the type has been explicitly ignored)");
        }
        bytecodeGenerator.mockClassStatic(type, settings, handler);
        Map<Class<?>, MockMethodInterceptor> interceptors = mockedStatics.get();
        if (interceptors == null) {
            interceptors = new WeakHashMap<>();
            mockedStatics.set(interceptors);
        }
        mockedStatics.getBackingMap().expungeStaleEntries();
        currentMocking.set(type);
        try {
            return new InlineStaticMockControl<>(type, interceptors, settings, handler);
        } finally {
            currentMocking.remove();
        }
    }

    @Override
    public <T> ConstructionMockControl<T> createConstructionMock(
            Class<T> type,
            Function<MockedConstruction.Context, MockCreationSettings<T>> settingsFactory,
            Function<MockedConstruction.Context, MockHandler<T>> handlerFactory,
            MockedConstruction.MockInitializer<T> mockInitializer) {
        if (type == Object.class) {
            throw new MockitoException("Unsupported construction of java.lang.Object");
        }
        bytecodeGenerator.mockClassConstruction(type);

        Map<Class<?>, BiConsumer<Object, MockedConstruction.Context>> interceptors =
        this.mockedConstruction.getThread()
        .computeIfAbsent(
        type,
        ignore -> new WeakHashMap<>(), // We need weak references here
        BiConsumer::accept);

        return new InlineConstructionMockControl<>(
        type, settingsFactory, handlerFactory, mockInitializer, interceptors);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T newInstance(Class<T> cls) throws InstantiationException {
        Constructor<?>[] constructors = cls.getDeclaredConstructors();
        if (constructors.length == 0) {
            throw new InstantiationException(cls.getName() + " does not declare any constructor");
        }
        Constructor<?> selected = constructors[0];
        for (Constructor<?> constructor : constructors) {
            if (Modifier.isPublic(constructor.getModifiers())) {
                selected = constructor;
                break;
            }
        }
        Class<?>[] types = selected.getParameterTypes();
        Object[] arguments = new Object[types.length];
        int index = 0;
        for (Class<?> type : types) {
            arguments[index++] = makeStandardArgument(type);
        }
        MemberAccessor accessor = Plugins.getMemberAccessor();
        try {
            return (T)
            accessor.newInstance(
            selected,
            callback -> {
                currentMocking.set(cls);
                try {
                    return callback.newInstance();
                } finally {
                    currentMocking.remove();
                }
            },
            arguments);
        } catch (Exception e) {
            throw new InstantiationException("Could not instantiate " + cls.getName(), e);
        }
    }

    private Object makeStandardArgument(Class<?> type) {
        if (type == boolean.class) {
            return false;
        }
        if (type == byte.class) {
            return (byte) 0;
        }
        if (type == short.class) {
            return (short) 0;
        }
        if (type == char.class) {
            return (char) 0;
        }
        if (type == int.class) {
            return 0;
        }
        if (type == long.class) {
            return 0L;
        }
        if (type == float.class) {
            return 0f;
        }
        if (type == double.class) {
            return 0d;
        }
        return null;
    }

    private static class InlineStaticMockControl<T> implements StaticMockControl<T> {

        private final Class<T> type;

        private final Map<Class<?>, MockMethodInterceptor> interceptors;

        private final MockCreationSettings<T> settings;

        private final MockHandler handler;

        private InlineStaticMockControl(
                Class<T> type,
                Map<Class<?>, MockMethodInterceptor> interceptors,
                MockCreationSettings<T> settings,
                MockHandler handler) {
            this.type = type;
            this.interceptors = interceptors;
            this.settings = settings;
            this.handler = handler;
        }

        @Override
        public Class<T> getType() {
            return type;
        }

        @Override
        public void enable() {
            for (Map.Entry<Class<?>, MockMethodInterceptor> entry : interceptors.entrySet()) {
                mocks.put(entry.getKey(), entry.getValue());
            }
        }

        @Override
        public void disable() {
            for (Map.Entry<Class<?>, MockMethodInterceptor> entry : interceptors.entrySet()) {
                mocks.remove(entry.getKey(), entry.getValue());
            }
        }
    }

    private class InlineConstructionMockControl<T> implements ConstructionMockControl<T> {

        private final Class<T> type;

        private final Function<MockedConstruction.Context, MockCreationSettings<T>> settingsFactory;
        private final Function<MockedConstruction.Context, MockHandler<T>> handlerFactory;

        private final MockedConstruction.MockInitializer<T> mockInitializer;

        private final Map<Class<?>, BiConsumer<Object, MockedConstruction.Context>> interceptors;

        private final List<Object> all = new ArrayList<>();
        private int count;

        private InlineConstructionMockControl(
                Class<T> type,
                Function<MockedConstruction.Context, MockCreationSettings<T>> settingsFactory,
                Function<MockedConstruction.Context, MockHandler<T>> handlerFactory,
                MockedConstruction.MockInitializer<T> mockInitializer,
                Map<Class<?>, BiConsumer<Object, MockedConstruction.Context>> interceptors) {
            this.type = type;
            this.settingsFactory = settingsFactory;
            this.handlerFactory = handlerFactory;
            this.mockInitializer = mockInitializer;
            this.interceptors = interceptors;
        }

        @Override
        public Class<T> getType() {
            return type;
        }

        @Override
        public void enable() {
            for (int index = 0; index < count; index++) {
                Object ignored = makeStandardArgument(type);
                BiConsumer<Object, MockedConstruction.Context> interceptor = all.get(index);
                interceptor.accept(ignored, new InlineConstructionMockContext(all, type));
            }
        }

        @Override
        public void disable() {
            for (Object ignored : all) {
                INSTRUMENTATION.removeTransformer(InlineBytecodeGenerator.SILENT_CHECKER);
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public List<T> getMocks() {
            if (count != all.size()) {
                throw new IllegalStateException(
                "Not all created mocks were registered. Created: "
                + count
                + ", All: "
                + all.size());
            }
            return (List<T>) all;
        }
    }

    private static class InlineConstructionMockContext implements MockedConstruction.Context {

        private static final Map<String, Class<?>> PRIMITIVES = new HashMap<>();

        static {
            PRIMITIVES.put(boolean.class.getName(), boolean.class);
            PRIMITIVES.put(byte.class.getName(), byte.class);
            PRIMITIVES.put(short.class.getName(), short.class);
            PRIMITIVES.put(char.class.getName(), char.class);
            PRIMITIVES.put(int.class.getName(), int.class);
            PRIMITIVES.put(long.class.getName(), long.class);
            PRIMITIVES.put(float.class.getName(), float.class);
            PRIMITIVES.put(double.class.getName(), double.class);
        }

        private int count;

        private final Object[] arguments;
        private final Class<?> type;
        private final String[] parameterTypeNames;

        private InlineConstructionMockContext(
                Object[] arguments, Class<?> type, String[] parameterTypeNames) {
            this.arguments = arguments;
            this.type = type;
            this.parameterTypeNames = parameterTypeNames;
            count = arguments.length;
        }

        @Override
        public int getCount() {
            return ++count;
        }

        @Override
        public Constructor<?> constructor() {
            Class<?>[] parameterTypes = constructor().getParameterTypes();
            int n = arguments.length;
            if (parameterTypes.length == n) {
                for (int i = 0; i < n; ++i) {
                    if (!parameterTypes[i].equals(arguments[i].getClass())) {
                        ++count;
                    }
                }
            } else {
                count = n;
            }
            return constructor();
        }

        @Override
        public List<?> arguments() {
            return Arrays.asList(arguments);
        }
    }
}
