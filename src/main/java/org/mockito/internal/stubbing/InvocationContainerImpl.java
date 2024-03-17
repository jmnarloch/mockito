/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.mockito.internal.invocation.StubInfoImpl;
import org.mockito.internal.verification.DefaultRegisteredInvocations;
import org.mockito.internal.verification.RegisteredInvocations;
import org.mockito.internal.verification.SingleRegisteredInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationContainer;
import org.mockito.invocation.MatchableInvocation;
import org.mockito.mock.MockCreationSettings;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubbing;
import org.mockito.stubbing.ValidableAnswer;

public class InvocationContainerImpl implements InvocationContainer, Serializable {

    private static final long serialVersionUID = -5334301962749537177L;
    private final LinkedList<StubbedInvocationMatcher> stubbed = new LinkedList<>();
    private final DoAnswerStyleStubbing doAnswerStyleStubbing;
    private final RegisteredInvocations registeredInvocations;
    private final Strictness mockStrictness;

    private MatchableInvocation invocationForStubbing;

    public InvocationContainerImpl(MockCreationSettings<?> mockSettings) {
        
    }

    public void setInvocationForPotentialStubbing(MatchableInvocation invocation) {
        
    }

    public void resetInvocationForPotentialStubbing(MatchableInvocation invocationMatcher) {
        
    }

    public void addAnswer(Answer<?> answer, Strictness stubbingStrictness) {
        
    }

    /** Adds new stubbed answer and returns the invocation matcher the answer was added to. */
    public StubbedInvocationMatcher addAnswer(
            Answer<?> answer, boolean isConsecutive, Strictness stubbingStrictness) {
        
    }

    public void addConsecutiveAnswer(Answer<?> answer) {
        
    }

    Object answerTo(Invocation invocation) throws Throwable {
        
    }

    public StubbedInvocationMatcher findAnswerFor(Invocation invocation) {
        
    }

    /**
     * Sets the answers declared with 'doAnswer' style.
     */
    public void setAnswersForStubbing(List<Answer<?>> answers, Strictness strictness) {
        
    }

    public boolean hasAnswersForStubbing() {
        
    }

    public boolean hasInvocationForPotentialStubbing() {
        
    }

    public void setMethodForStubbing(MatchableInvocation invocation) {
        
    }

    @Override
    public String toString() {
        
    }

    public List<Invocation> getInvocations() {
        
    }

    public void clearInvocations() {
        
    }

    /**
     * Stubbings in ascending order, most recent last
     */
    public Collection<Stubbing> getStubbingsAscending() {
        
    }

    public Object invokedMock() {
        
    }

    private RegisteredInvocations createRegisteredInvocations(
            MockCreationSettings<?> mockSettings) {
        
    }

    public Answer<?> findStubbedAnswer() {
        
    }
}
