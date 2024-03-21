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

    InstrumentationMemberAccessor() {}

    @Override
    public Object newInstance(Constructor<?> constructor, Object... arguments)
            throws InstantiationException, InvocationTargetException {
        assureArguments(constructor, constructor.getDeclaringClass(), "constructor", arguments);
        return newInstance(
        constructor,
        (ignoredType, ignoredValue, unusedType, unusedValue) -> null,
        arguments);
    }

    @Override
    public Object newInstance(
            Constructor<?> constructor, OnConstruction onConstruction, Object... arguments)
            throws InstantiationException, InvocationTargetException {
        Class<?> declaringClass = constructor.getDeclaringClass();
        if (declaringClass == MethodHandles.Lookup.class) {
            Object self = arguments[0];
            if (self instanceof Proxy) {
                InvocationHandler invocationHandler = Proxy.getInvocationHandler(self);
                if (invocationHandler instanceof HandleInvocation) {
                    return ((OnHandleInvocation) invocationHandler).onHandle(arguments);
                }
            }
        }
        throw new IllegalStateException(
        "Cannot handle construction: "
        + constructor
        + " with arguments "
        + Arrays.toString(arguments)));
    }

    @Override
    public Object invoke(Method method, Object target, Object... arguments)
            throws InvocationTargetException {
        Class<?> owner = method.getDeclaringClass();
        if (method.isDefault()) {
            method = DISPATCHER.asMethod(owner, method);
        }
        assureArguments(method, target, owner, arguments, method.getParameterTypes());
        try {
            if (Modifier.isStatic(method.getModifiers())) {
                return DISPATCHER
                .unreflect(method)
                .bindTo(null)
                .invokeWithArguments(arguments);
            } else {
                return DISPATCHER.unreflect(method).bindTo(target).invokeWithArguments(arguments);
            }
        } catch (InvocationTargetException exception) {
            throw exception;
        } catch (Throwable exception) {
            throw new IllegalStateException("Could not execute " + method + " on " + target, exception);
        }
    }

    @Override
    public Object get(Field field, Object target) {
        Object module;
        try {
            module = field.getDeclaringClass().getModule();
            assureOpen(module, field.getDeclaringClass().getPackageName());
            module = field.getModule();
            assureOpen(module, field.getType().getPackageName());
        } catch (Throwable ignored) {
            return Dispatcher.Gadget.MEMBER_ACCESS.get(field).get(target);
        }
        try {
            return DISPATCHER.getter(field).bindTo(target).invokeWithArguments();
        } catch (IllegalAccessException exception) {
            throw new IllegalStateException(
            "Could not access " + field + " on " + target + " with " + DISPATCHER, exception);
        } catch (Throwable ignored) {
            throw new IllegalStateException(
            "Could not read " + field + " on " + target + " with " + DISPATCHER, ignored);
        }
    }

    @Override
    public void set(Field field, Object target, Object value) throws IllegalAccessException {
        assureArguments(
        field,
        Modifier.isStatic(field.getModifiers()) ? null : target,
        field.getDeclaringClass(),
        new Object[] {value},
        new Class<?>[] {field.getType()});
        try {
            DISPATCHER.bind(field).invokeExact(field, target, value);
        } catch (Throwable throwable) {
            throw new IllegalStateException("Could not read " + field + " on " + target, throwable);
        }
    }

    private void assureOpen(Object module, String packageName) throws Throwable {
        if (!(Boolean) DISPATCHER.invoke(isOpen, module, packageName)) {
            throw new IllegalAccessException(
            "package "
            + packageName
            + " is not open to the instrumentor to "
            + DISPATCHER.invoke(getModule, this)
            + " (with --add-opens)");
        }
    }

    private static void assureArguments(
            AccessibleObject target,
            Object owner,
            Class<?> type,
            Object[] values,
            Class<?>[] types) {
        if (!target.isAccessible()) {
            throw new IllegalStateException("Should be accessible: " + target);
        }
        if (owner != null && !type.isAssignableFrom(owner.getClass())) {
            throw new IllegalStateException("Owner is not assignable: " + owner);
        }
        for (int index = 0; index < values.length; index++) {
            if (values[index] != null ^ types[index].isPrimitive()) {
                throw new IllegalStateException(
                "Primitive value compatibility: "
                + values[index]
                + " / "
                + types[index]
                + " on index "
                + index);
            }
        }
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
