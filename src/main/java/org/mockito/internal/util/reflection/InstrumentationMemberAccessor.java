/*
 * Copyright (c) 2020 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodCall;
import org.mockito.exceptions.base.MockitoInitializationException;
import org.mockito.internal.SuppressSignatureCheck;
import org.mockito.plugins.MemberAccessor;

import java.lang.instrument.Instrumentation;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static net.bytebuddy.matcher.ElementMatchers.named;
import static org.mockito.internal.util.StringUtil.join;

@SuppressSignatureCheck
class InstrumentationMemberAccessor implements MemberAccessor {

    private static final Map<Class<?>, Class<?>> WRAPPERS = new HashMap<>();

    private static final Instrumentation INSTRUMENTATION;
    private static final Dispatcher DISPATCHER;

    private static final Throwable INITIALIZATION_ERROR;

    static {
        WRAPPERS.put(boolean.class, Boolean.class);
        WRAPPERS.put(byte.class, Byte.class);
        WRAPPERS.put(short.class, Short.class);
        WRAPPERS.put(char.class, Character.class);
        WRAPPERS.put(int.class, Integer.class);
        WRAPPERS.put(long.class, Long.class);
        WRAPPERS.put(float.class, Float.class);
        WRAPPERS.put(double.class, Double.class);
        Instrumentation instrumentation;
        Dispatcher dispatcher;
        Throwable throwable;
        try {
            instrumentation = ByteBuddyAgent.install();
            // We need to generate a dispatcher instance that is located in a distinguished class
            // loader to create a unique (unnamed) module to which we can open other packages to.
            // This way, we assure that classes within Mockito's module (which might be a shared,
            // unnamed module) do not face escalated privileges where tests might pass that would
            // otherwise fail without Mockito's opening.
            dispatcher =
                    new ByteBuddy()
                            .subclass(Dispatcher.class)
                            .method(named("getLookup"))
                            .intercept(MethodCall.invoke(MethodHandles.class.getMethod("lookup")))
                            .method(named("getModule"))
                            .intercept(
                                    MethodCall.invoke(Class.class.getMethod("getModule"))
                                            .onMethodCall(
                                                    MethodCall.invoke(
                                                            Object.class.getMethod("getClass"))))
                            .method(named("setAccessible"))
                            .intercept(
                                    MethodCall.invoke(
                                                    AccessibleObject.class.getMethod(
                                                            "setAccessible", boolean.class))
                                            .onArgument(0)
                                            .withArgument(1))
                            .method(named("invokeWithArguments"))
                            .intercept(
                                    MethodCall.invoke(
                                                    MethodHandle.class.getMethod(
                                                            "invokeWithArguments", Object[].class))
                                            .onArgument(0)
                                            .withArgument(1))
                            .make()
                            .load(
                                    InstrumentationMemberAccessor.class.getClassLoader(),
                                    ClassLoadingStrategy.Default.WRAPPER)
                            .getLoaded()
                            .getConstructor()
                            .newInstance();
            throwable = null;
        } catch (Throwable t) {
            instrumentation = null;
            dispatcher = null;
            throwable = t;
        }
        INSTRUMENTATION = instrumentation;
        DISPATCHER = dispatcher;
        INITIALIZATION_ERROR = throwable;
    }

    @SuppressWarnings(value = "unused")
    private final MethodHandle getModule;

    @SuppressWarnings(value = "unused")
    private final MethodHandle isOpen;

    @SuppressWarnings(value = "unused")
    private final MethodHandle redefineModule;

    @SuppressWarnings(value = "unused")
    private final MethodHandle privateLookupIn;

    InstrumentationMemberAccessor() {
        
    }

    @Override
    public Object newInstance(Constructor<?> constructor, Object... arguments)
            throws InstantiationException, InvocationTargetException {
        
    }

    @Override
    public Object newInstance(
            Constructor<?> constructor, OnConstruction onConstruction, Object... arguments)
            throws InstantiationException, InvocationTargetException {
        
    }

    @Override
    public Object invoke(Method method, Object target, Object... arguments)
            throws InvocationTargetException {
        
    }

    @Override
    public Object get(Field field, Object target) {
        
    }

    @Override
    public void set(Field field, Object target, Object value) throws IllegalAccessException {
        
    }

    private void assureOpen(Object module, String packageName) throws Throwable {
        // It would be more reliable to check if a module's package already is opened to
        // the dispatcher module from before. Unfortunately, there is no reliable check
        // for doing so since the isOpen(String, Module) method always returns true
        // if the second argument is an unnamed module. Therefore, for now, we need
        // to reopen packages even if they are already opened to the dispatcher module.
        
    }

    private static void assureArguments(
            AccessibleObject target,
            Object owner,
            Class<?> type,
            Object[] values,
            Class<?>[] types) {
        
    }

    public interface Dispatcher {

        MethodHandles.Lookup getLookup();

        Object getModule();

        void setAccessible(AccessibleObject target, boolean value);

        // Used to avoid invoke/invokeExact being exposed to Android where this class should
        // never be loaded. Since the invocation happens from the generated code, the Android
        // build pipeline does not fail.
        Object invokeWithArguments(MethodHandle handle, Object... arguments) throws Throwable;
    }
}
