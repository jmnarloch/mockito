/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import java.util.List;

import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MatchableInvocation;

public class InvocationMarker {

    private InvocationMarker() {}

    public static void markVerified(List<Invocation> invocations, MatchableInvocation wanted) {}

    public static void markVerified(Invocation invocation, MatchableInvocation wanted) {
        invocation.markVerified();
    }

    public static void markVerifiedInOrder(
            List<Invocation> chunk, MatchableInvocation wanted, InOrderContext context) {}
}
