/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import static org.mockito.internal.exceptions.Reporter.notAMockPassedToWhenMethod;
import static org.mockito.internal.exceptions.Reporter.notAnException;
import static org.mockito.internal.exceptions.Reporter.nullPassedToWhenMethod;
import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;
import static org.mockito.internal.stubbing.answers.DoesNothing.doesNothing;
import static org.mockito.internal.util.MockUtil.isMock;

import java.util.LinkedList;
import java.util.List;

import org.mockito.internal.stubbing.answers.CallsRealMethods;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.internal.stubbing.answers.ThrowsException;
import org.mockito.internal.stubbing.answers.ThrowsExceptionForClassType;
import org.mockito.internal.util.MockUtil;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubber;

public class StubberImpl implements Stubber {

    private final Strictness strictness;

    public StubberImpl(Strictness strictness) {
        
    }

    private final List<Answer<?>> answers = new LinkedList<>();

    @Override
    public <T> T when(T mock) {
        
    }

    @Override
    public Stubber doReturn(Object toBeReturned) {
        
    }

    @Override
    public Stubber doReturn(Object toBeReturned, Object... nextToBeReturned) {
        
    }

    private StubberImpl doReturnValues(Object... toBeReturned) {
        
    }

    @Override
    public Stubber doThrow(Throwable... toBeThrown) {
        
    }

    @Override
    public Stubber doThrow(Class<? extends Throwable> toBeThrown) {
        
    }

    @Override
    public Stubber doThrow(
            Class<? extends Throwable> toBeThrown, Class<? extends Throwable>... nextToBeThrown) {
        
    }

    @Override
    public Stubber doNothing() {
        
    }

    @Override
    public Stubber doAnswer(Answer answer) {
        
    }

    @Override
    public Stubber doCallRealMethod() {
        
    }
}
