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
        
    }

    @SuppressWarnings("unused")
    @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
    private static Callable<?> enter(
            @Identifier String identifier,
            @Advice.This Object mock,
            @Advice.Origin Method origin,
            @Advice.AllArguments Object[] arguments)
            throws Throwable {
        
    }

    @SuppressWarnings({"unused", "UnusedAssignment"})
    @Advice.OnMethodExit
    private static void exit(
            @Advice.Return(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object returned,
            @Advice.Enter Callable<?> mocked)
            throws Throwable {
        
    }

    @Override
    public Callable<?> handle(Object instance, Method origin, Object[] arguments) throws Throwable {
        
    }

    @Override
    public Callable<?> handleStatic(Class<?> type, Method origin, Object[] arguments)
            throws Throwable {
        
    }

    @Override
    public Object handleConstruction(
            Class<?> type, Object object, Object[] arguments, String[] parameterTypeNames) {
        
    }

    @Override
    public boolean isMock(Object instance) {
        // We need to exclude 'interceptors.target' explicitly to avoid a recursive check on whether
        // the map is a mock object what requires reading from the map.
        
    }

    @Override
    public boolean isMocked(Object instance) {
        
    }

    @Override
    public boolean isMockedStatic(Class<?> type) {
        
    }

    @Override
    public boolean isOverridden(Object instance, Method origin) {
        
    }

    @Override
    public boolean isConstructorMock(Class<?> type) {
        
    }

    private static class RealMethodCall implements RealMethod {

        private final SelfCallInfo selfCallInfo;

        private final Method origin;

        private final MockWeakReference<Object> instanceRef;

        private final Object[] arguments;

        private RealMethodCall(
                SelfCallInfo selfCallInfo, Method origin, Object instance, Object[] arguments) {
            
        }

        @Override
        public boolean isInvokable() { }

        @Override
        public Object invoke() throws Throwable {
            
        }
    }

    private static class SerializableRealMethodCall implements RealMethod {

        private final String identifier;

        private final SerializableMethod origin;

        private final MockReference<Object> instanceRef;

        private final Object[] arguments;

        private SerializableRealMethodCall(
                String identifier, Method origin, Object instance, Object[] arguments) {
            
        }

        @Override
        public boolean isInvokable() { }

        @Override
        public Object invoke() throws Throwable {
            
        }
    }

    private static class StaticMethodCall implements RealMethod {

        private final SelfCallInfo selfCallInfo;

        private final Class<?> type;

        private final Method origin;

        private final Object[] arguments;

        private StaticMethodCall(
                SelfCallInfo selfCallInfo, Class<?> type, Method origin, Object[] arguments) {
            
        }

        @Override
        public boolean isInvokable() { }

        @Override
        public Object invoke() throws Throwable {
            
        }
    }

    private static Object tryInvoke(Method origin, Object instance, Object[] arguments)
            throws Throwable {
        
    }

    static Throwable removeRecursiveCalls(final Throwable cause, final Class<?> declaringClass) {
        
    }

    private static class ReturnValueWrapper implements Callable<Object> {

        private final Object returned;

        private ReturnValueWrapper(Object returned) {
            
        }

        @Override
        public Object call() {
            
        }
    }

    private static class SelfCallInfo extends ThreadLocal<Object> {

        Object replace(Object value) {
            
        }

        boolean checkSelfCall(Object value) {
            
        }
    }

    static class ConstructorShortcut
            implements AsmVisitorWrapper.ForDeclaredMethods.MethodVisitorWrapper {

        private final String identifier;

        ConstructorShortcut(String identifier) {
            
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
            
        }

        private static Object[] toFrames(Object self, List<TypeDescription> types) {
            
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface Identifier {}

    static class ForHashCode {

        @SuppressWarnings("unused")
        @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
        private static boolean enter(@Identifier String id, @Advice.This Object self) {
            
        }

        @SuppressWarnings({"unused", "UnusedAssignment"})
        @Advice.OnMethodExit
        private static void enter(
                @Advice.This Object self,
                @Advice.Return(readOnly = false) int hashCode,
                @Advice.Enter boolean skipped) {
            
        }
    }

    static class ForEquals {

        @SuppressWarnings("unused")
        @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
        private static boolean enter(@Identifier String identifier, @Advice.This Object self) {
            
        }

        @SuppressWarnings({"unused", "UnusedAssignment"})
        @Advice.OnMethodExit
        private static void enter(
                @Advice.This Object self,
                @Advice.Argument(0) Object other,
                @Advice.Return(readOnly = false) boolean equals,
                @Advice.Enter boolean skipped) {
            
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
            
        }

        @SuppressWarnings({"unused", "UnusedAssignment"})
        @Advice.OnMethodExit
        private static void exit(
                @Advice.Return(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object returned,
                @Advice.Enter Callable<?> mocked)
                throws Throwable {
            
        }
    }

    public static class ForReadObject {

        @SuppressWarnings({"unused", "BanSerializableRead"})
        public static void doReadObject(
                @Identifier String identifier,
                @This MockAccess thiz,
                @Argument(0) ObjectInputStream objectInputStream)
                throws IOException, ClassNotFoundException {
            
        }
    }
}
