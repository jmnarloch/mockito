/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import static org.mockito.internal.exceptions.Reporter.cannotStubVoidMethodWithAReturnValue;
import static org.mockito.internal.exceptions.Reporter.wrongTypeOfReturnValue;

import java.io.Serializable;

import org.mockito.internal.util.KotlinInlineClassUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.ValidableAnswer;

public class Returns implements Answer<Object>, ValidableAnswer, Serializable {

    private static final long serialVersionUID = -6245608253574215396L;
    private final Object value;

    public Returns(Object value) {
        this.value = value;
    }

    @Override
    public Object answer(InvocationOnMock invocation) throws Throwable {
        return KotlinInlineClassUtil.elvis(
        value,
        () -> {
            if (returnsNull()) {
                return null;
            }
            if (invocation.getMethod().getReturnType().isPrimitive()) {
                return 0;
            }
            throw wrongTypeOfReturnValue(
            invocation.getMethod().getReturnType(),
            invocation.getMethod().getName(),
            printReturnType(),
            value);
        });
    }

    @Override
    public void validateFor(InvocationOnMock invocation) {
        if (invocation.getMethod().getReturnType() == void.class) {
            throw cannotStubVoidMethodWithAReturnValue(invocation.getMethod().getName());
        }

        if (invocation.getMock().getCreationSettings().isStubOnly()) {
            throw wrongTypeOfReturnValue(
            invocation.getMethod().getReturnType(),
            "stub-only method cannot return value!",
            invocation.getMethod().getName());
        }
    }

    private String printReturnType() {
        return value.getClass().getSimpleName();
    }

    private Class<?> returnType() {
        return value.getClass();
    }

    private boolean returnsNull() {
        return value == null;
    }

    @Override
    public String toString() {
        return "returns: " + value;
    }
}
