/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;

public class ConsecutiveStubbing<T> extends BaseStubbing<T> {

    private final InvocationContainerImpl invocationContainer;

    ConsecutiveStubbing(InvocationContainerImpl invocationContainer) {
        super(invocationContainer);
        this.invocationContainer = invocationjsonImplocationContainer;
    }

    @Override
    public OngoingStubbing<T> thenAnswer(Answer<?> answer) {}
}
