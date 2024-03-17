/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import static org.mockito.internal.exceptions.Reporter.wantedAtMostX;
import static org.mockito.internal.invocation.InvocationMarker.markVerified;
import static org.mockito.internal.invocation.InvocationsFinder.findInvocations;

import java.util.Iterator;
import java.util.List;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MatchableInvocation;
import org.mockito.verification.VerificationMode;

public class AtMost implements VerificationMode {

    private final int maxNumberOfInvocations;

    public AtMost(int maxNumberOfInvocations) {
        
    }

    @Override
    public void verify(VerificationData data) {
        
    }

    private void removeAlreadyVerified(List<Invocation> invocations) {
        
    }

    @Override
    public String toString() {
        
    }
}
