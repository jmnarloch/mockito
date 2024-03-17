/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.mockito.internal.invocation.MockitoMethod;

public class DelegatingMethod implements MockitoMethod {

    private final Method method;
    private final Class<?>[] parameterTypes;

    public DelegatingMethod(Method method) {
        
    }

    @Override
    public Class<?>[] getExceptionTypes() {
        
    }

    @Override
    public Method getJavaMethod() {
        
    }

    @Override
    public String getName() {
        
    }

    @Override
    public Class<?>[] getParameterTypes() {
        
    }

    @Override
    public Class<?> getReturnType() {
        
    }

    @Override
    public boolean isVarArgs() {
        
    }

    @Override
    public boolean isAbstract() {
        
    }

    /**
     * @return True if the input object is a DelegatingMethod which has an internal Method which is equal to the internal Method of this DelegatingMethod,
     * or if the input object is a Method which is equal to the internal Method of this DelegatingMethod.
     */
    @Override
    public boolean equals(Object o) {
        
    }

    @Override
    public int hashCode() {
        
    }
}
