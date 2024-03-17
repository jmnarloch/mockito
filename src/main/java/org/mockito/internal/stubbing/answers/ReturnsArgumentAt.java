/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import static org.mockito.internal.exceptions.Reporter.invalidArgumentPositionRangeAtInvocationTime;
import static org.mockito.internal.exceptions.Reporter.invalidArgumentRangeAtIdentityAnswerCreationTime;
import static org.mockito.internal.exceptions.Reporter.wrongTypeOfArgumentToReturn;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.ValidableAnswer;

/**
 * Returns the passed parameter identity at specified index.
 * <p>
 * <p>
 * The <code>argumentIndex</code> represents the index in the argument array of the invocation.
 * </p>
 * <p>
 * If this number equals -1 then the last argument is returned.
 * </p>
 *
 * @see org.mockito.AdditionalAnswers
 * @since 1.9.5
 */
public class ReturnsArgumentAt implements Answer<Object>, ValidableAnswer, Serializable {

    private static final long serialVersionUID = -589315085166295101L;

    public static final int LAST_ARGUMENT = -1;

    private final int wantedArgumentPosition;

    /**
     * Build the identity answer to return the argument at the given position in the argument array.
     *
     * @param wantedArgumentPosition
     *            The position of the argument identity to return in the invocation. Using <code>-1</code> indicates the last argument ({@link #LAST_ARGUMENT}).
     */
    public ReturnsArgumentAt(int wantedArgumentPosition) {
        
    }

    @Override
    public Object answer(InvocationOnMock invocation) throws Throwable {
        
    }

    @Override
    public void validateFor(InvocationOnMock invocationOnMock) {
        
    }

    private int inferWantedArgumentPosition(InvocationOnMock invocation) {
        
    }

    private int inferWantedRawArgumentPosition(InvocationOnMock invocation) {
        
    }

    private void validateIndexWithinInvocationRange(
            InvocationOnMock invocation, int argumentPosition) {

        
    }

    private void validateIndexWithinTheoreticalInvocationRange(
            InvocationOnMock invocation, int argumentPosition) {
        
    }

    private void validateArgumentTypeCompatibility(Invocation invocation, int argumentPosition) {
        
    }

    private boolean wantedArgIndexIsVarargAndSameTypeAsReturnType(InvocationOnMock invocation) {
        
    }

    private boolean wantedArgumentPositionIsValidForTheoreticalInvocation(
            InvocationOnMock invocation, int argumentPosition) {
        
    }

    private Class<?> inferArgumentType(Invocation invocation, int argumentIndex) {
        
    }
}
