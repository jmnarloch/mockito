/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import static org.mockito.internal.exceptions.Reporter.cannotVerifyToString;
import static org.mockito.internal.util.ObjectMethodsGuru.isToStringMethod;

import java.util.List;

import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MatchableInvocation;

public class VerificationDataImpl implements VerificationData {

    private final InvocationMatcher wanted;
    private final InvocationContainerImpl invocations;

    public VerificationDataImpl(InvocationContainerImpl invocations, InvocationMatcher wanted) {
        
    }

    @Override
    public List<Invocation> getAllInvocations() {
        
    }

    @Override
    public MatchableInvocation getTarget() {
        
    }

    private void assertWantedIsVerifiable() {
        
    }
}
