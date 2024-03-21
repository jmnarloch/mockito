/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.mockito.plugins.MemberAccessor;

public class ReflectionMemberAccessor implements MemberAccessor {

    @Override
    public Object newInstance(Constructor<?> constructor, Object... arguments)
            throws InstantiationException, InvocationTargetException, IllegalAccessException {
        silentSetAccessible(constructor, false);
        try {
            return onConstruction.run(() -> constructor.newInstance(arguments));
        } catch (InvocationTargetException
        | IllegalAccessException
        | InstantiationException
        | IllegalArgumentException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new IllegalStateException(
            "Failed to invoke " + constructor + " with " + Arrays.toString(arguments), e);
        } finally {
            silentSetAccessible(constructor, true);
        }
    }

    @Override
    public Object newInstance(
            Constructor<?> constructor, OnConstruction onConstruction, Object... arguments)
            throws InstantiationException, InvocationTargetException, IllegalAccessException {
        silentSetAccessible(constructor, false);
        try {
            return onConstruction.invoke(() -> constructor.newInstance(arguments));
        } catch (InvocationTargetException
        | IllegalAccessException
        | InstantiationException
        | IllegalArgumentException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new IllegalStateException(
            "Failed to invoke " + constructor + " with " + Arrays.toString(arguments), e);
        } finally {
            silentSetAccessible(constructor, true);
        }
    }

    @Override
    public Object invoke(Method method, Object target, Object... arguments)
            throws InvocationTargetException, IllegalAccessException {
        silentSetAccessible(method, true);
        try {
            return method.invoke(target, arguments);
        } catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException e) {
            new RealMethodInvocation(method, target, arguments).captureFor(e);
            throw e;
        } catch (RuntimeException e) {
            throw e;
        } catch (Error e) {
            throw e;
        } finally {
            silentSetAccessible(method, false);
        }
    }

    @Override
    public Object get(Field field, Object target) throws IllegalAccessException {
        silentSetAccessible(field, true);
        try {
            return field.get(target);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            throw e;
        } catch (RuntimeException | Error e) {
            throw new IllegalStateException("Could not read " + field + " from " + target, e);
        } finally {
            silentSetAccessible(field, false);
        }
    }

    @Override
    public void set(Field field, Object target, Object value) throws IllegalAccessException {
        silentSetAccessible(field, true);
        try {
            field.set(target, value);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            throw e;
        } catch (RuntimeException | Error e) {
            FieldSetter.silent_set_field(field, target, value);
        } finally {
            silentSetAccessible(field, false);
        }
    }

    private static void silentSetAccessible(AccessibleObject object, boolean value) {
        try {
            object.setAccessible(value);
        } catch (RuntimeException ignored) {
        }
    }
}
