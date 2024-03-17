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
        
    }

    @Override
    public Object answer(InvocationOnMock invocation) throws Throwable {
        
    }

    @Override
    public void validateFor(InvocationOnMock invocation) {
        
    }

    private String printReturnType() {
        
    }

    private Class<?> returnType() {
        
    }

    private boolean returnsNull() {
        
    }

    @Override
    public String toString() {
        
    }
}
