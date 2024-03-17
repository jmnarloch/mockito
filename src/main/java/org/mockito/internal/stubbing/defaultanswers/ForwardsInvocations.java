/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import static org.mockito.internal.exceptions.Reporter.delegatedMethodDoesNotExistOnDelegate;
import static org.mockito.internal.exceptions.Reporter.delegatedMethodHasWrongReturnType;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.plugins.MemberAccessor;
import org.mockito.stubbing.Answer;

/**
 * Internal answer to forward invocations on a real instance.
 *
 * @since 1.9.5
 */
public class ForwardsInvocations implements Answer<Object>, Serializable {
    private static final long serialVersionUID = -8343690268123254910L;

    private Object delegatedObject = null;

    public ForwardsInvocations(Object delegatedObject) {
        
    }

    @Override
    public Object answer(InvocationOnMock invocation) throws Throwable {
        
    }

    private Method getDelegateMethod(Method mockMethod) throws NoSuchMethodException {
        
    }

    private static boolean compatibleReturnTypes(Class<?> superType, Class<?> subType) {
        
    }
}
