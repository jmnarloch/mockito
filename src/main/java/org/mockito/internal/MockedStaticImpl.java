/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import static org.mockito.internal.exceptions.Reporter.missingMethodInvocation;
import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;
import static org.mockito.internal.util.MockUtil.getInvocationContainer;
import static org.mockito.internal.util.MockUtil.resetMock;
import static org.mockito.internal.util.StringUtil.join;
import static org.mockito.internal.verification.VerificationModeFactory.noInteractions;
import static org.mockito.internal.verification.VerificationModeFactory.noMoreInteractions;

import org.mockito.MockedStatic;
import org.mockito.MockingDetails;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.debugging.LocationFactory;
import org.mockito.internal.listeners.VerificationStartedNotifier;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.verification.MockAwareVerificationMode;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.invocation.Location;
import org.mockito.invocation.MockHandler;
import org.mockito.plugins.MockMaker;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.verification.VerificationMode;

public final class MockedStaticImpl<T> implements MockedStatic<T> {

    private final MockMaker.StaticMockControl<T> control;

    private boolean closed;

    private final Location location = LocationFactory.create();

    protected MockedStaticImpl(MockMaker.StaticMockControl<T> control) {
        
    }

    @Override
    public <S> OngoingStubbing<S> when(Verification verification) {
        
    }

    @Override
    public void verify(Verification verification, VerificationMode mode) {
        
    }

    @Override
    public void reset() {
        
    }

    @Override
    public void clearInvocations() {
        
    }

    @Override
    public void verifyNoMoreInteractions() {
        
    }

    @Override
    public void verifyNoInteractions() {
        
    }

    @Override
    public boolean isClosed() {
        
    }

    @Override
    public void close() {
        
    }

    @Override
    public void closeOnDemand() {
        
    }

    private void assertNotClosed() {
        
    }

    @Override
    public String toString() {
        
    }
}
