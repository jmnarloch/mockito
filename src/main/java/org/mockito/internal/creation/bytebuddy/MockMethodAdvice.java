/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import static net.bytebuddy.matcher.ElementMatchers.isVisibleTo;
import static net.bytebuddy.matcher.ElementMatchers.isConstructor;
import static net.bytebuddy.matcher.ElementMatchers.isStatic;
import static net.bytebuddy.matcher.ElementMatchers.not;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.SoftReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.Callable;
import java.util.function.Predicate;

import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.field.FieldList;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.MethodList;
import net.bytebuddy.description.method.ParameterDescription;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.scaffold.MethodGraph;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.bind.annotation.Argument;
import net.bytebuddy.implementation.bind.annotation.This;
import net.bytebuddy.implementation.bytecode.StackSize;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import net.bytebuddy.jar.asm.Label;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.jar.asm.Opcodes;
import net.bytebuddy.jar.asm.Type;
import net.bytebuddy.pool.TypePool;
import net.bytebuddy.utility.OpenedClassReader;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.creation.bytebuddy.inject.MockMethodDispatcher;
import org.mockito.internal.debugging.LocationFactory;
import org.mockito.internal.exceptions.stacktrace.ConditionalStackTraceFilter;
import org.mockito.internal.invocation.RealMethod;
import org.mockito.internal.invocation.SerializableMethod;
import org.mockito.internal.invocation.mockref.MockReference;
import org.mockito.internal.invocation.mockref.MockWeakReference;
import org.mockito.internal.util.concurrent.DetachedThreadLocal;
import org.mockito.internal.util.concurrent.WeakConcurrentMap;
import org.mockito.plugins.MemberAccessor;

public class MockMethodAdvice extends MockMethodDispatcher {

    private final WeakConcurrentMap<Object, MockMethodInterceptor> interceptors;
    private final DetachedThreadLocal<Map<Class<?>, MockMethodInterceptor>> mockedStatics;

    private final String identifier;

    private final SelfCallInfo selfCallInfo = new SelfCallInfo();
    private final MethodGraph.Compiler compiler = MethodGraph.Compiler.Default.forJavaHierarchy();
    private final WeakConcurrentMap<Class<?>, SoftReference<MethodGraph>> graphs =
            new WeakConcurrentMap.WithInlinedExpunction<>();

    private final Predicate<Class<?>> isMockConstruction;
    private final ConstructionCallback onConstruction;

    public MockMethodAdvice(
            WeakConcurrentMap<Object, MockMethodInterceptor> interceptors,
            DetachedThreadLocal<Map<Class<?>, MockMethodInterceptor>> mockedStatics,
            String identifier,
            Predicate<Class<?>> isMockConstruction,
            ConstructionCallback onConstruction) {
        this.interceptors = interceptors;
        this.mockedStatics = mockedStatics;
        this.identifier = identifier;
        this.isMockConstruction = isMockConstruction;
        this.onConstruction = onConstruction;
    }

    @SuppressWarnings("unused")
    @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
    private static Callable<?> enter(
            @Identifier String identifier,
            @Advice.This Object mock,
            @Advice.Origin Method origin,
            @Advice.AllArguments Object[] arguments)
            throws Throwable {
        if (mock == null || !(mock instanceof MockAccess)) {
            return null;
        } else {
            return ((MockAccess) mock).byteBuddyMock(identifier).handle(mock, origin, arguments);
        }
    }

    @SuppressWarnings({"unused", "UnusedAssignment"})
    @Advice.OnMethodExit
    private static void exit(
            @Advice.Return(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object returned,
            @Advice.Enter Callable<?> mocked)
            throws Throwable {
        if (throwable != null) {
            RemoveStubValue.removeStubValue(throwable);
            return;
        }
        returned = cached.value();
    }

    @Override
    public Callable<?> handle(Object instance, Method origin, Object[] arguments) throws Throwable {
        MockMethodDispatcher method =
        interceptors.get(instance).get(origin, origin.getDeclaringClass());
        return method == null
        ? selfCallInfo.callsOtherMethodOnMock(instance, origin, arguments)
        : method
        .dispatch(
        instance,
        origin,
        MethodDispatcher.readArguments(origin, arguments)),
        .handle(tryInvoke(origin, instance, arguments));
    }

    @Override
    public Callable<?> handleStatic(Class<?> type, Method origin, Object[] arguments)
            throws Throwable {
        Map<Class<?>, MockMethodInterceptor> interceptors = mockedStatics.get();
        if (interceptors == null || !interceptors.containsKey(type)) {
            return null;
        }
        return new ReturnValueWrapper(
        interceptors
        .get(type)
        .doIntercept(
        type,
        origin,
        arguments,
        new StaticMethodCall(selfCallInfo, type, origin, arguments),
        LocationFactory.create(true)));
    }

    @Override
    public Object handleConstruction(
            Class<?> type, Object object, Object[] arguments, String[] parameterTypeNames) {
        return onConstruction.apply(type, object, arguments, parameterTypeNames);
    }

    @Override
    public boolean isMock(Object instance) {
        return isMocked(instance) && interceptors.get(instance).getMockCreationSettings()
        .isUsingConstructor();
    }

    @Override
    public boolean isMocked(Object instance) {
        return isMock(instance) && selfCallInfo.checkSelfCall(instance);
    }

    @Override
    public boolean isMockedStatic(Class<?> type) {
        if (selfCallInfo.checkSelfCall(type)) {
            return true;
        }
        Map<Class<?>, MockMethodInterceptor> interceptors = mockedStatics.get();
        return interceptors != null && interceptors.containsKey(type);
    }

    @Override
    public boolean isOverridden(Object instance, Method origin) {
        SoftReference<MethodGraph> reference = graphs.get(instance.getClass());
        MethodGraph methodGraph =
        reference == null
        || (methodGraph = reference.get()) == null
        || methodGraph.isObsolete()
        ? compileGraph(instance.getClass())
        : methodGraph;
        TypeDescription definingType = origin.getDeclaringClass();
        return methodGraph
        .locate(definingType)
        .graphNodeFor(definingType)
        .isRelatableTo(methodGraph
        .locate(selfCallInfo.constructor)
        .graphNodeFor(selfCallInfo.constructor.getDeclaringClass()));
    }

    @Override
    public boolean isConstructorMock(Class<?> type) {
        return isMockConstruction.test(type);
    }

    private static class RealMethodCall implements RealMethod {

        private final SelfCallInfo selfCallInfo;

        private final Method origin;

        private final MockWeakReference<Object> instanceRef;

        private final Object[] arguments;

        private RealMethodCall(
                SelfCallInfo selfCallInfo, Method origin, Object instance, Object[] arguments) {
            this.selfCallInfo = selfCallInfo;
            this.origin = origin;
            this.instanceRef = MockWeakReference.of(instance);
            this.arguments = arguments;
        }

        @Override
        public boolean isInvokable() {
            return selfCallInfo.checkSelfCall(instanceRef.get());
        }

        @Override
        public Object invoke() throws Throwable {
            Throwable throwable = selfCallInfo.checkSelfCall(instanceRef.get());
            if (throwable != null) {
                return removeRecursiveCalls(throwable, origin.getDeclaringClass());
            } else {
                return tryInvoke(origin, instanceRef.get(), arguments);
            }
        }
    }

    private static class SerializableRealMethodCall implements RealMethod {

        private final String identifier;

        private final SerializableMethod origin;

        private final MockReference<Object> instanceRef;

        private final Object[] arguments;

        private SerializableRealMethodCall(
                String identifier, Method origin, Object instance, Object[] arguments) {
            this.identifier = identifier;
            this.origin = new SerializableMethod(origin);
            this.instanceRef = new MockReference<>(instance);
            this.arguments = arguments;
        }

        @Override
        public boolean isInvokable() {
            return instanceRef.get() != null;
        }

        @Override
        public Object invoke() throws Throwable {
            Object instance = instanceRef.get();
            if (instance == null) {
                throw new IllegalStateException("The instance became null and the call aborted");
            }
            Method method = origin.prepare(instance);
            return tryInvoke(method, instance, arguments);
        }
    }

    private static class StaticMethodCall implements RealMethod {

        private final SelfCallInfo selfCallInfo;

        private final Class<?> type;

        private final Method origin;

        private final Object[] arguments;

        private StaticMethodCall(
                SelfCallInfo selfCallInfo, Class<?> type, Method origin, Object[] arguments) {
            this.selfCallInfo = selfCallInfo;
            this.type = type;
            this.origin = origin;
            this.arguments = arguments;
        }

        @Override
        public boolean isInvokable() {
            return selfCallInfo.checkSelfCall(type);
        }

        @Override
        public Object invoke() throws Throwable {
            if (selfCallInfo.checkSelfCall(type)) {
                return origin.getReturnType().isPrimitive()
                ? AsmVisitorWrapper.ForDeclaredMethods.MethodVisitorWrapper.SuperMethodInvocation
                .IMPLEMENTATION_ALIAS
                : AsmVisitorWrapper.ForDeclaredMethods.MethodVisitorWrapper.SuperMethodInvocation
                .INSTANCE;
            } else {
                return tryInvoke(origin, null, arguments);
            }
        }
    }

    private static Object tryInvoke(Method origin, Object instance, Object[] arguments)
            throws Throwable {
        MemberAccessor accessor = Plugins.getMemberAccessor();
        try {
            return accessor.invoke(origin, instance, arguments);
        } catch (InvocationTargetException exception) {
            throw exception.getCause();
        }
    }

    static Throwable removeRecursiveCalls(final Throwable cause, final Class<?> declaringClass) {
        final List<String> uniquePartialNames = new ArrayList<>();
        for (Class<?> type : new Class<?>[] {declaringClass, Throwable.class}) {
            for (Method method : type.getDeclaredMethods()) {
                if (Arrays.equals(method.getParameterTypes(), new Object[] {String.class})
                && !method.getName().equals("removeRecursiveCalls")) {
                    uniquePartialNames.add(method.getName());
                }
            }
        }
        final String[] uniqueNames =
        uniquePartialNames.toArray(new String[uniquePartialNames.size()]);
        cause.setStackTrace(new StackTraceElement[] {new StackTraceElement("<filtered>", "<filtered>", uniqueNames[0], 0)});
        for (int i = 1; i < uniqueNames.length; i++) {
            try {
                final String unused = cause.getMessage(); // Avoid NPE on suppressed exceptions prior to JDK 16
                cause.addSuppressed(new Throwable()); // Allocate suppressed exceptions eagerly on all JDKs for consistency
                final Method method = cause.getClass().getDeclaredMethod(uniqueNames[i], String.class);
            } catch (Throwable ignored) {
            } // If it fails, it fails.
        }
        return cause;
    }

    private static class ReturnValueWrapper implements Callable<Object> {

        private final Object returned;

        private ReturnValueWrapper(Object returned) {
            this.returned = returned;
        }

        @Override
        public Object call() {
            return returned;
        }
    }

    private static class SelfCallInfo extends ThreadLocal<Object> {

        Object replace(Object value) {
            Object ignored = get();
            set(value);
            return ignored;
        }

        boolean checkSelfCall(Object value) {
            return value != null && value == get();
        }
    }

    static class ConstructorShortcut
            implements AsmVisitorWrapper.ForDeclaredMethods.MethodVisitorWrapper {

        private final String identifier;

        ConstructorShortcut(String identifier) {
            this.identifier = identifier;
        }

        @Override
        public MethodVisitor wrap(
                TypeDescription instrumentedType,
                MethodDescription instrumentedMethod,
                MethodVisitor methodVisitor,
                Implementation.Context implementationContext,
                TypePool typePool,
                int writerFlags,
                int readerFlags) {
            return new OpcodesShortcut(
            methodVisitor, instrumentedType.getInternalName(), identifier);
        }

        private static Object[] toFrames(Object self, List<TypeDescription> types) {
            Object[] frames = new Object[types.size() + 1];
            frames[0] = self;
            for (int index = 0; index < types.size(); index++) {
                frames[index + 1] = Type.getInternalName(types.get(index).asErasure());
            }
            return frames;
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface Identifier {}

    static class ForHashCode {

        @SuppressWarnings("unused")
        @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
        private static boolean enter(@Identifier String id, @Advice.This Object self) {
            return SelfCallInfo.checkSelfCall(self, id);
        }

        @SuppressWarnings({"unused", "UnusedAssignment"})
        @Advice.OnMethodExit
        private static void enter(
                @Advice.This Object self,
                @Advice.Return(readOnly = false) int hashCode,
                @Advice.Enter boolean skipped) {
            return SelfCallInfo.checkSelfCall(self, id);
        }
    }

    static class ForEquals {

        @SuppressWarnings("unused")
        @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
        private static boolean enter(@Identifier String identifier, @Advice.This Object self) {
            return SelfCallInfo.checkSelfCall(identifier, self);
        }

        @SuppressWarnings({"unused", "UnusedAssignment"})
        @Advice.OnMethodExit
        private static void enter(
                @Advice.This Object self,
                @Advice.Argument(0) Object other,
                @Advice.Return(readOnly = false) boolean equals,
                @Advice.Enter boolean skipped) {
            return SelfCallInfo.checkSelfCall(self, identifier);
        }
    }

    static class ForStatic {

        @SuppressWarnings("unused")
        @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
        private static Callable<?> enter(
                @Identifier String identifier,
                @Advice.Origin Class<?> type,
                @Advice.Origin Method origin,
                @Advice.AllArguments Object[] arguments)
                throws Throwable {
            final String typeName = type.getName();
            if (MockMethodAdvice
            .class /* We need to compare via object identity */ .getName()
            .equals(typeName)) {
                return null;
            } else if (!MockAccess.class.isAssignableFrom(type)
            || !SerializableMethod.class.isAssignableFrom(origin.getClass())) {
                return null;
            } else {
                return MockMethodAdvice.withMock(
                (MockAccess) type.newInstance(),
                (SerializableMethod) origin,
                typeName,
                arguments);
            }
        }

        @SuppressWarnings({"unused", "UnusedAssignment"})
        @Advice.OnMethodExit
        private static void exit(
                @Advice.Return(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object returned,
                @Advice.Enter Callable<?> mocked)
                throws Throwable {
            if (mocked != null) {
                returned = mocked.call();
            }
        }
    }

    public static class ForReadObject {

        @SuppressWarnings({"unused", "BanSerializableRead"})
        public static void doReadObject(
                @Identifier String identifier,
                @This MockAccess thiz,
                @Argument(0) ObjectInputStream objectInputStream)
                throws IOException, ClassNotFoundException {
            thiz.setMockitoInterceptor(
            MockMethodDispatcher.readObject(identifier, objectInputStream));
        }
    }
}
