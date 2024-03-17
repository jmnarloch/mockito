/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import static org.mockito.internal.verification.checkers.AtLeastXNumberOfInvocationsChecker.checkAtLeastNumberOfInvocations;
import static org.mockito.internal.verification.checkers.MissingInvocationChecker.checkMissingInvocation;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.api.VerificationDataInOrder;
import org.mockito.internal.verification.api.VerificationInOrderMode;
import org.mockito.verification.VerificationMode;

public class AtLeast implements VerificationInOrderMode, VerificationMode {

    final int wantedCount;

    public AtLeast(int wantedNumberOfInvocations) {
        
    }

    @Override
    public void verify(VerificationData data) {
        
    }

    @Override
    public void verifyInOrder(VerificationDataInOrder data) {
        
    }

    @Override
    public String toString() {
        
    }
}
