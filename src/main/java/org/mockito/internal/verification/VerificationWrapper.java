/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

public abstract class VerificationWrapper<WrapperT extends VerificationMode>
        implements VerificationMode {

    protected final WrapperT wrappedVerification;

    public VerificationWrapper(WrapperT wrappedVerification) {
        
    }

    @Override
    public void verify(VerificationData data) {
        
    }

    protected abstract VerificationMode copySelfWithNewVerificationMode(
            VerificationMode verificationMode);

    public VerificationMode times(int wantedNumberOfInvocations) {
        
    }

    public VerificationMode never() {
        
    }

    public VerificationMode atLeastOnce() {
        
    }

    public VerificationMode atLeast(int minNumberOfInvocations) {
        
    }

    public VerificationMode atMostOnce() {
        
    }

    public VerificationMode atMost(int maxNumberOfInvocations) {
        
    }

    public VerificationMode only() {
        
    }
}
