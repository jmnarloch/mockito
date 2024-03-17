/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import org.mockito.internal.MockitoCore;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.LenientStubber;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;

public class DefaultLenientStubber implements LenientStubber {

    private static final MockitoCore MOCKITO_CORE = new MockitoCore();

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
    public Stubber doAnswer(Answer answer) {
        
    }

    @Override
    public Stubber doNothing() {
        
    }

    @Override
    public Stubber doReturn(Object toBeReturned) {
        
    }

    @Override
    public Stubber doReturn(Object toBeReturned, Object... nextToBeReturned) {
        
    }

    @Override
    public Stubber doCallRealMethod() {
        
    }

    @Override
    public <T> OngoingStubbing<T> when(T methodCall) {
        
    }

    private static Stubber stubber() {
        
    }
}
