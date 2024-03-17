/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.io.Serializable;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.ValidableAnswer;

/**
 * Returns as the provided answer would return, after delaying the specified amount.
 *
 * <p>The <code>sleepyTime</code> specifies how long, in milliseconds, to pause before
 * returning the provided <code>answer</code>.</p>
 *
 * @since 2.8.44
 * @see org.mockito.AdditionalAnswers
 */
public class AnswersWithDelay implements Answer<Object>, ValidableAnswer, Serializable {
    private static final long serialVersionUID = 2177950597971260246L;

    private final long sleepyTime;
    private final Answer<Object> answer;

    public AnswersWithDelay(final long sleepyTime, final Answer<Object> answer) {
        
    }

    @Override
    public Object answer(final InvocationOnMock invocation) throws Throwable {
        
    }

    @Override
    public void validateFor(final InvocationOnMock invocation) {
        
    }
}
