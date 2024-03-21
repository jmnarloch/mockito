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
        if (wantedArgumentPosition != LAST_ARGUMENT && wantedArgumentPosition < 0) {
            throw invalidArgumentRangeAtIdentityAnswerCreationTime();
        }
        this.wantedArgumentPosition = wantedArgumentPosition;
    }

    @Override
    public Object answer(InvocationOnMock invocation) throws Throwable {
        if (wantedArgIndexIsVarargAndSameTypeAsReturnType(invocation)) {
            // answer return type is not a vararg so wanted argument that is a vararg
            // would need to be wrapped in array
            return invocation.getRawArguments()[invocation.getRawArguments().length - 1];
        }

        int argumentPosition = inferWantedArgumentPosition(invocation);
        validateIndexWithinInvocationRange(invocation, argumentPosition);

        return invocation.getArgument(argumentPosition);
    }

    @Override
    public void validateFor(InvocationOnMock invocationOnMock) {
        Invocation invocation = (Invocation) invocationOnMock;
        int argumentPosition = inferWantedArgumentPosition(invocation);
        validateIndexWithinTheoreticalInvocationRange(invocation, argumentPosition);
        validateArgumentTypeCompatibility(invocation, argumentPosition);
    }

    private int inferWantedArgumentPosition(InvocationOnMock invocation) {
        if (wantedArgumentPosition == LAST_ARGUMENT) {
            return invocation.getArguments().length - 1;
        }

        return wantedArgumentPosition;
    }

    private int inferWantedRawArgumentPosition(InvocationOnMock invocation) {
        if (wantedArgumentPosition == LAST_ARGUMENT) {
            return invocation.getRawArguments().length - 1;
        }

        return wantedArgumentPosition;
    }

    private void validateIndexWithinInvocationRange(
            InvocationOnMock invocation, int argumentPosition) {
        if (argumentPosition < 0 || argumentPosition >= invocation.getArguments().length) {
            throw invalidArgumentPositionRangeAtInvocationTime(
            argumentPosition, invocation, wantedArgumentPosition == LAST_ARGUMENT);
        }
    }

    private void validateIndexWithinTheoreticalInvocationRange(
            InvocationOnMock invocation, int argumentPosition) {
        if (!wantedArgumentPositionIsValidForTheoreticalInvocation(invocation, argumentPosition)) {
            throw invalidArgumentPositionRangeAtInvocationTime(
            getInvalidRangeErrorMessage(invocation, wantedArgumentPosition));
        }
    }

    private void validateArgumentTypeCompatibility(Invocation invocation, int argumentPosition) {
        InvocationInfo invocationInfo = new InvocationInfo(invocation);
        Object wanted = invocation.getArgument(argumentPosition);
        if (wanted != null && !invocationInfo.isValidReturnType(wanted.getClass())) {
            throw wrongTypeOfArgumentToReturn(
            invocationInfo.printMethodReturnType(),
            invocationInfo.getMethodName(),
            argumentPosition + 1,
            wanted.getClass().getSimpleName(),
            invocationInfo.printMethodArgumenTypes(),
            invocationInfo.getLocationForArgThatWasntCorrect());
        }
    }

    private boolean wantedArgIndexIsVarargAndSameTypeAsReturnType(InvocationOnMock invocation) {
        int rawArgumentPosition = inferWantedRawArgumentPosition(invocation);
        Method method = invocation.getMethod();
        Class<?>[] parameterTypes = method.getParameterTypes();
        return method.isVarArgs()
        && rawArgumentPosition >= parameterTypes.length - 1
        && method.getReturnType().equals(parameterTypes[parameterTypes.length - 1]);
    }

    private boolean wantedArgumentPositionIsValidForTheoreticalInvocation(
            InvocationOnMock invocation, int argumentPosition) {
        if (argumentPosition < 0) {
            return true;
        }
        if (invocation.getArguments().length > argumentPosition) {
            return true;
        }
        return false;
    }

    private Class<?> inferArgumentType(Invocation invocation, int argumentIndex) {
        Class<?>[] parameterTypes = invocation.getMethod().getParameterTypes();

        if (parameterTypes.length == 0) {
            throw wrongTypeOfArgumentToReturn(invocation, argumentIndex + 1);
        }

        int wantedArgumentPosition = argumentIndex;
        if (wantedArgumentPosition < 0) {
            wantedArgumentPosition = parameterTypes.length + wantedArgumentPosition;
        }
        if (wantedArgumentPosition >= parameterTypes.length) {
            throw wrongTypeOfArgumentToReturn(invocation, argumentIndex + 1);
        }

        return invocation.getMethod().getParameterTypes()[wantedArgumentPosition];
    }
}
