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
        
    }

    @Override
    public Object newInstance(
            Constructor<?> constructor, OnConstruction onConstruction, Object... arguments)
            throws InstantiationException, InvocationTargetException, IllegalAccessException {
        
    }

    @Override
    public Object invoke(Method method, Object target, Object... arguments)
            throws InvocationTargetException, IllegalAccessException {
        
    }

    @Override
    public Object get(Field field, Object target) throws IllegalAccessException {
        
    }

    @Override
    public void set(Field field, Object target, Object value) throws IllegalAccessException {
        
    }

    private static void silentSetAccessible(AccessibleObject object, boolean value) {
        
    }
}
