/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.SuspendMethod;

public class SerializableMethod implements Serializable, MockitoMethod {

    private static final long serialVersionUID = 6005610965006048445L;

    private final Class<?> declaringClass;
    private final String methodName;
    private final Class<?>[] parameterTypes;
    private final Class<?> returnType;
    private final Class<?>[] exceptionTypes;
    private final boolean isVarArgs;
    private final boolean isAbstract;

    private transient volatile Method method;

    public SerializableMethod(Method method) {
        
    }

    @Override
    public String getName() {
        
    }

    @Override
    public Class<?> getReturnType() {
        
    }

    @Override
    public Class<?>[] getParameterTypes() {
        
    }

    @Override
    public Class<?>[] getExceptionTypes() {
        
    }

    @Override
    public boolean isVarArgs() {
        
    }

    @Override
    public boolean isAbstract() {
        
    }

    @Override
    public Method getJavaMethod() {
        
    }

    @Override
    public int hashCode() { }

    @Override
    public boolean equals(Object obj) {
        
    }
}
