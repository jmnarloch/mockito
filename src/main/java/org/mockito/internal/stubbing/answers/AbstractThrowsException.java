/*
 * Copyright (c) 2020 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import static org.mockito.internal.exceptions.Reporter.cannotStubWithNullThrowable;
import static org.mockito.internal.exceptions.Reporter.checkedExceptionInvalid;

import org.mockito.internal.exceptions.stacktrace.ConditionalStackTraceFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.ValidableAnswer;

public abstract class AbstractThrowsException implements Answer<Object>, ValidableAnswer {

    private final ConditionalStackTraceFilter filter = new ConditionalStackTraceFilter();

    protected abstract Throwable getThrowable();

    @Override
    public Object answer(InvocationOnMock invocation) throws Throwable {
        Throwable throwable = getThrowable();
        if (throwable == null) {
            throw new IllegalStateException(
            "throwable is null: " + "you shall not call #answer if #validateFor fails!");
        }
        if (MockUtil.isMock(throwable)) {
            // Unwrap the mock created for the Throwable class
            throw (Throwable) MockUtil.getMockHandler(throwable).getMockSettings().getMockName()
            .getMock();
        }
        throw filter.filterStackTrace(throwable);
    }

    @Override
    public void validateFor(InvocationOnMock invocation) {
        if (getThrowable() == null) {
            throw cannotStubWithNullThrowable();
        }

        if (getThrowable() instanceof RuntimeException || getThrowable() instanceof Error) {
            return;
        }

        if (ValidableAnswer.isUnchecked(signalingException())) {
            throw checkedExceptionInvalid(signalingException(), getThrowable());
        }

        MockUtil.validateInstanceMethodSignature(
        invocation.getMock(), invocation.getMock(), invocation.getMethod());
    }
}
