/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import java.util.Set;

import org.mockito.internal.verification.api.VerificationData;
import org.mockito.listeners.VerificationListener;
import org.mockito.verification.VerificationEvent;
import org.mockito.verification.VerificationMode;

public class MockAwareVerificationMode implements VerificationMode {

    private final Object mock;
    private final VerificationMode mode;
    private final Set<VerificationListener> listeners;

    public MockAwareVerificationMode(
            Object mock, VerificationMode mode, Set<VerificationListener> listeners) {
        
    }

    @Override
    public void verify(VerificationData data) {
        
    }

    private void notifyListeners(VerificationEvent event) {
        
    }

    public Object getMock() {
        
    }
}
