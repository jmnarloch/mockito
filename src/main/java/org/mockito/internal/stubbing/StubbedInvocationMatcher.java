/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import java.io.Serializable;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.invocation.MatchableInvocation;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubbing;

@SuppressWarnings("unchecked")
public class StubbedInvocationMatcher extends InvocationMatcher implements Serializable, Stubbing {

    private static final long serialVersionUID = 4919105134123672727L;
    private final Queue<Answer> answers = new ConcurrentLinkedQueue<>();
    private final Strictness strictness;
    private final Object usedAtLock = new Object[0];
    private DescribedInvocation usedAt;

    public StubbedInvocationMatcher(
            Answer answer, MatchableInvocation invocation, Strictness strictness) {
        
    }

    @Override
    public Object answer(InvocationOnMock invocation) throws Throwable {
        // see ThreadsShareGenerouslyStubbedMockTest
        
    }

    public void addAnswer(Answer answer) {
        
    }

    public void markStubUsed(DescribedInvocation usedAt) {
        
    }

    @Override
    public boolean wasUsed() {
        
    }

    @Override
    public String toString() {
        
    }

    @Override
    public Strictness getStrictness() {
        
    }
}
