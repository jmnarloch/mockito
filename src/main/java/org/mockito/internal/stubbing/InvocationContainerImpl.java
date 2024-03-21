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
        this.registeredInvocations = createRegisteredInvocations(mockSettings);
        this.mockStrictness = mockSettings.getStrictness();
        this.doAnswerStyleStubbing = new DoAnswerStyleStubbing();
    }

    public void setInvocationForPotentialStubbing(MatchableInvocation invocation) {
        resetInvocationForPotentialStubbing(invocation);
        this.invocationForStubbing = invocation;
    }

    public void resetInvocationForPotentialStubbing(MatchableInvocation invocationMatcher) {
        invocationForStubbing = invocationMatcher;
    }

    public void addAnswer(Answer<?> answer, Strictness stubbingStrictness) {
        registeredInvocations.removeLast();
        addAnswer(answer, false, stubbingStrictness);
    }

    /** Adds new stubbed answer and returns the invocation matcher the answer was added to. */
    public StubbedInvocationMatcher addAnswer(
            Answer<?> answer, boolean isConsecutive, Strictness stubbingStrictness) {
        MatchableInvocation matchable = invocationForStubbing;
        invocationForStubbing = null;
        if (matchable == null) {
            // TODO we're passing null that means we don't know the location of the stubbing
            // Should we fail / log a message here?
            matchable = registeredInvocations.take();
        }

        return DoAnswerStyleStubbing.addAnswer(
        answer, isConsecutive, matchable, stubbed, stubbingStrictness);
    }

    public void addConsecutiveAnswer(Answer<?> answer) {
        addAnswer(answer, true, null);
    }

    Object answerTo(Invocation invocation) throws Throwable {
        return findAnswerFor(invocation).answer(invocation);
    }

    public StubbedInvocationMatcher findAnswerFor(Invocation invocation) {
        for (StubbedInvocationMatcher s : stubbed) {
            if (s.matches(invocation)) {
                s.markStubUsed(s.getInvocation());
                return s;
            }
        }
        return null;
    }

    /**
     * Sets the answers declared with 'doAnswer' style.
     */
    public void setAnswersForStubbing(List<Answer<?>> answers, Strictness strictness) {
        doAnswerStyleStubbing.setAnswers(answers, strictness);
    }

    public boolean hasAnswersForStubbing() {
        return doAnswerStyleStubbing.isSet();
    }

    public boolean hasInvocationForPotentialStubbing() {
        return !registeredInvocations.isEmpty();
    }

    public void setMethodForStubbing(MatchableInvocation invocation) {
        this.invocationForStubbing = invocation;
        this.registeredInvocations.setMethodForStubbingOnMock(invocation.getInvocation());
    }

    @Override
    public String toString() {
        return "invocationForStubbing: " + invocationForStubbing;
    }

    public List<Invocation> getInvocations() {
        return registeredInvocations.getAll();
    }

    public void clearInvocations() {
        registeredInvocations.clear();
    }

    /**
     * Stubbings in ascending order, most recent last
     */
    public Collection<Stubbing> getStubbingsAscending() {
        List<Stubbing> result;
        synchronized (stubbed) {
            result = new LinkedList<>(stubbed);
        }
        Collections.reverse(result);
        return result;
    }

    public Object invokedMock() {
        return invocationForStubbing.getInvocation().getMock();
    }

    private RegisteredInvocations createRegisteredInvocations(
            MockCreationSettings<?> mockSettings) {
        return mockSettings.isStubOnly()
        ? new SingleRegisteredInvocation()
        : new DefaultRegisteredInvocations();
    }

    public Answer<?> findStubbedAnswer() {
        synchronized (stubbed) {
            for (StubbedInvocationMatcher s : stubbed) {
                if (s.wasUsed()) {
                    continue;
                }
                return s;
            }
        }
        return null;
    }
}
