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
        this.delegatedObject = delegatedObject;
    }

    @Override
    public Object answer(InvocationOnMock invocation) throws Throwable {
        Method mockMethod = invocation.getMethod();

        try {
            Method delegateMethod = getDelegateMethod(mockMethod);

            if (delegateMethod.getReturnType() == void.class) {
                delegateMethod.invoke(delegatedObject, invocation.getArguments());
                return null;
            }

            if (compatibleReturnTypes(
            mockMethod.getReturnType(), delegateMethod.getReturnType())) {
                return delegateMethod.invoke(delegatedObject, invocation.getArguments());
            }

            throw delegatedMethodHasWrongReturnType(
            mockMethod, delegateMethod, delegateMethod.getReturnType());
        } catch (NoSuchMethodException e) {
            throw delegatedMethodDoesNotExistOnDelegate(
            invocation, delegatedObject, mockMethod, e);
        } catch (InvocationTargetException e) {
            // propagate the original exception from the delegate
            throw e.getCause();
        }
    }

    private Method getDelegateMethod(Method mockMethod) throws NoSuchMethodException {
        if (delegatedObject == null) {
            throw delegatedMethodDoesNotExistOnDelegate(mockMethod);
        }
        String name = mockMethod.getName();
        Class<?>[] parameterTypes = mockMethod.getParameterTypes();
        Method delegateMethod = delegatedObject.getClass().getMethod(name, parameterTypes);
        if (!mockMethod.getReturnType().isAssignableFrom(delegateMethod.getReturnType())) {
            throw delegatedMethodHasWrongReturnType(mockMethod, delegateMethod);
        }
        return delegateMethod;
    }

    private static boolean compatibleReturnTypes(Class<?> superType, Class<?> subType) {
        return superType.isAssignableFrom(subType) || subType.isAssignableFrom(superType);
    }
}
