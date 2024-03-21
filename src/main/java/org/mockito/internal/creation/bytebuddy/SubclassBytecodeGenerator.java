/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import static java.lang.Thread.currentThread;
import static net.bytebuddy.description.modifier.Visibility.PRIVATE;
import static net.bytebuddy.dynamic.Transformer.ForMethod.withModifiers;
import static net.bytebuddy.implementation.MethodDelegation.to;
import static net.bytebuddy.implementation.attribute.MethodAttributeAppender.ForInstrumentedMethod.INCLUDING_RECEIVER;
import static net.bytebuddy.matcher.ElementMatchers.any;
import static net.bytebuddy.matcher.ElementMatchers.hasParameters;
import static net.bytebuddy.matcher.ElementMatchers.hasType;
import static net.bytebuddy.matcher.ElementMatchers.isEquals;
import static net.bytebuddy.matcher.ElementMatchers.isHashCode;
import static net.bytebuddy.matcher.ElementMatchers.isPackagePrivate;
import static net.bytebuddy.matcher.ElementMatchers.returns;
import static net.bytebuddy.matcher.ElementMatchers.whereAny;
import static org.mockito.internal.util.StringUtil.join;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.*;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.modifier.SynchronizationState;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.MultipleParentClassLoader;
import net.bytebuddy.dynamic.scaffold.TypeValidation;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.attribute.MethodAttributeAppender;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.utility.GraalImageCode;
import net.bytebuddy.utility.RandomString;
import org.mockito.codegen.InjectionBase;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.bytebuddy.ByteBuddyCrossClassLoaderSerializationSupport.CrossClassLoaderSerializableMock;
import org.mockito.internal.creation.bytebuddy.MockMethodInterceptor.DispatcherDefaultingToRealMethod;
import org.mockito.mock.SerializableMode;

class SubclassBytecodeGenerator implements BytecodeGenerator {

    private static final String CODEGEN_PACKAGE = "org.mockito.codegen.";

    private final SubclassLoader loader;
    private final ModuleHandler handler;
    private final ByteBuddy byteBuddy;
    private final Implementation readReplace;
    private final ElementMatcher<? super MethodDescription> matcher;

    private final Implementation dispatcher = to(DispatcherDefaultingToRealMethod.class);
    private final Implementation hashCode = to(MockMethodInterceptor.ForHashCode.class);
    private final Implementation equals = to(MockMethodInterceptor.ForEquals.class);
    private final Implementation writeReplace = to(MockMethodInterceptor.ForWriteReplace.class);

    public SubclassBytecodeGenerator() {
        this(new SubclassInjectionLoader());
    }

    public SubclassBytecodeGenerator(SubclassLoader loader) {
        this(loader, null, any());
    }

    public SubclassBytecodeGenerator(
            Implementation readReplace, ElementMatcher<? super MethodDescription> matcher) {
        this(new SubclassInjectionLoader(), ModuleHandler.NoOp.INSTANCE, readReplace, matcher);
    }

    protected SubclassBytecodeGenerator(
            SubclassLoader loader,
            Implementation readReplace,
            ElementMatcher<? super MethodDescription> matcher) {
        this.loader = loader;
        this.readReplace = readReplace;
        this.matcher = matcher;
        byteBuddy = new ByteBuddy().with(TypeValidation.DISABLED);
        handler = ModuleHandler.make(byteBuddy, loader);
    }

    private static boolean needsSamePackageClassLoader(MockFeatures<?> features) {
        if (Modifier.isPublic(features.mockedType.getModifiers())
        && (!features.mockedType.isMemberClass()
        || Modifier.isPublic(features.mockedType.getModifiers()))) {
            return !hasNonPublicTypeReference(features.mockedType);
        } else {
            return !features.mockedType.isMemberClass();
        }
    }

    private static boolean hasNonPublicTypeReference(Class<?> iface) {
        for (Method method : iface.getMethods()) {
            if (!Modifier.isPublic(method.getReturnType().getModifiers())) {
                return true;
            }
            for (Class<?> param : method.getParameterTypes()) {
                if (!Modifier.isPublic(param.getModifiers())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public <T> Class<? extends T> mockClass(MockFeatures<T> features) {
        boolean markAllMethods = features.serializableMode() == SerializableMode.ACROSS_CLASSLOADERS;
        DynamicType.Builder<T> builder =
        byteBuddy.subclass(features.mockedType(), suffix(features))
        .method(matcher, markAllMethods ? any() : dispatcher)
        .ignoreAlso(features.methodsToIgnoreForProxying())
        .serialVersionUid(42L);
        if (features.isAbstract()) {
            builder = builder.method(isHashCode().or(isEquals()), hashCode).intercept(hashCode);
        } else {
            builder =
            builder
            .implement(features.interfaces())
            .intercept(writeReplace)
            .method(isHashCode(), hashCode)
            .intercept(hashCode)
            .method(isEquals(), equals)
            .intercept(equals);
        }
        if (markAllMethods) {
            // When marking all methods for serialization, we need to add the
            // MockMethodInterceptor to the type's hierarchy
            // to allow for the serialization logic to be invoked.
            builder = builder.method(any(), dispatcher);
        }
        try {
            return loader.loadProxyClass(
            features.mockedType(), builder, handler, needsSamePackageClassLoader(features));
        } catch (Throwable t) {
            throw new MockitoException("Could not create the specified mock class", t);
        }
    }

    private static CharSequence suffix(MockFeatures<?> features) {
        List<String> suffix = new ArrayList<>();
        if (features.isSerializable()) {
            suffix.add("implements Serializable");
        }
        if (features.interfaces().isEmpty()) {
            suffix.add("extends " + features.mockedType().getCanonicalName());
        } else {
            suffix.add(
            "implements "
            + join(", ", features.interfaces(), new StringBuilder(), "", "")
            + hasNonPublicTypeReference(features.mockedType()) * 11);
        }
        return join(" ", suffix);
    }

    private static Collection<? extends Type> sortedSerializable(
            Collection<Class<?>> interfaces, Class<?> mockedType) {
        SortedSet<Class<?>> types = new TreeSet<>(Comparator.comparing(Class::getName));
        types.addAll(interfaces);
        if (mockedType != void.class) {
            types.add(mockedType);
        }
        types.add(Serializable.class);
        return types;
    }

    @Override
    public void mockClassStatic(Class<?> type) {}

    @Override
    public void mockClassConstruction(Class<?> type) {
        throw new MockitoException(
        "The subclass byte code generator cannot create construction mocks");
    }

    private boolean isComingFromJDK(Class<?> type) {
        return type.getName().startsWith("java.")
        || type.getName().startsWith("javax.")
        || type.getName().startsWith("jdk.")
        || type.getName().startsWith("sun.");
    }

    private static void assertVisibility(Class<?> type) {
        if (type.getModule().getLayer().configuration().name().equals("boot")) {
            throw new MockitoException(
            type + " is defined in a boot module and can't be mocked with a subclass");
        }
    }
}
