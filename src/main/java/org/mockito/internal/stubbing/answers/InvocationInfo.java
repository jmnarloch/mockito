/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.mockito.internal.invocation.AbstractAwareMethod;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.Primitives;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockCreationSettings;

public class InvocationInfo implements AbstractAwareMethod {

    private final Method method;
    private final InvocationOnMock invocation;

    public InvocationInfo(InvocationOnMock theInvocation) {
        
    }

    public boolean isValidException(final Throwable throwable) {
        
    }

    private boolean isValidExceptionForParents(final Class<?> parent, final Throwable throwable) {
        
    }

    private boolean isValidExceptionForClass(final Class<?> parent, final Throwable throwable) {
        
    }

    private boolean isValidException(final Method method, final Throwable throwable) {
        
    }

    public boolean isValidReturnType(Class<?> clazz) {
        
    }

    /**
     * Returns {@code true} is the return type is {@link Void} or represents the pseudo-type to the keyword {@code void}.
     * E.g:  {@code void foo()} or {@code Void bar()}
     */
    public boolean isVoid() {
        
    }

    public String printMethodReturnType() {
        
    }

    public String getMethodName() {
        
    }

    public boolean returnsPrimitive() {
        
    }

    public Method getMethod() {
        
    }

    public boolean isDeclaredOnInterface() {
        
    }

    @Override
    public boolean isAbstract() {
        
    }
}
