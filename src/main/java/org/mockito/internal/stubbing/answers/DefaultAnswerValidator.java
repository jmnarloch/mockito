/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import static org.mockito.internal.exceptions.Reporter.wrongTypeReturnedByDefaultAnswer;

import org.mockito.invocation.InvocationOnMock;

public abstract class DefaultAnswerValidator {
    public static void validateReturnValueFor(InvocationOnMock invocation, Object returnedValue)
            throws Throwable {
        new ReturnValueValidator(invocation).validateReturnedValueOrError(returnedValue);
    }
}
