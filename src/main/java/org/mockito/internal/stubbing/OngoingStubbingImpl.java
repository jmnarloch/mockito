/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import static org.mockito.internal.exceptions.Reporter.incorrectUseOfApi;

import java.util.List;

import org.mockito.invocation.Invocation;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;

public class OngoingStubbingImpl<T> extends BaseStubbing<T> {

    private final InvocationContainerImpl invocationContainer;
    private Strictness strictness;

    public OngoingStubbingImpl(InvocationContainerImpl invocationContainer) {
        this.invocationContainer = invocationContainer;
    }

    @Override
    public OngoingStubbing<T> thenAnswer(Answer<?> answer) {
        if (strictness == Strictness.STRICT_STUBS) {
            invocationContainer.setInvocationForStricterChecks(invocationContainer.removeLast());
        }
        return this.invocationContainer.addAnswer(answer);
    }

    public List<Invocation> getRegisteredInvocations() {
        return invocationContainer.getInvocations();
    }

    public void setStrictness(Strictness strictness) {
        this.strictness = strictness;
    }
}
