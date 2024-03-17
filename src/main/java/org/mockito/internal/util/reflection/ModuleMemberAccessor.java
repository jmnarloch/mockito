/*
 * Copyright (c) 2020 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import net.bytebuddy.ClassFileVersion;
import org.mockito.plugins.MemberAccessor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ModuleMemberAccessor implements MemberAccessor {

    private final MemberAccessor delegate;

    public ModuleMemberAccessor() {
        
    }

    private static MemberAccessor delegate() {
        
    }

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
}
