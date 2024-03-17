/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import static org.mockito.internal.exceptions.Reporter.notAnException;
import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;

import org.mockito.internal.stubbing.answers.CallsRealMethods;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.internal.stubbing.answers.ThrowsException;
import org.mockito.internal.stubbing.answers.ThrowsExceptionForClassType;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;

public abstract class BaseStubbing<T> implements OngoingStubbing<T> {

    // Keep strong ref to mock preventing premature garbage collection when using 'One-liner stubs'.
    // See #1541.
    private final Object strongMockRef;

    BaseStubbing(Object mock) {
        
    }

    @Override
    public OngoingStubbing<T> then(Answer<?> answer) {
        
    }

    @Override
    public OngoingStubbing<T> thenReturn(T value) {
        
    }

    @Override
    public OngoingStubbing<T> thenReturn(T value, T... values) {
        
    }

    private OngoingStubbing<T> thenThrow(Throwable throwable) {
        
    }

    @Override
    public OngoingStubbing<T> thenThrow(Throwable... throwables) {
        
    }

    @Override
    public OngoingStubbing<T> thenThrow(Class<? extends Throwable> throwableType) {
        
    }

    @Override
    public OngoingStubbing<T> thenThrow(
            Class<? extends Throwable> toBeThrown, Class<? extends Throwable>... nextToBeThrown) {
        
    }

    @Override
    public OngoingStubbing<T> thenCallRealMethod() {
        
    }

    @Override
    @SuppressWarnings("unchecked")
    public <M> M getMock() {
        
    }
}
