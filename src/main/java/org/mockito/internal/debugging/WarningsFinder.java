/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.debugging;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.Invocation;

public class WarningsFinder {
    private final List<Invocation> baseUnusedStubs;
    private final List<InvocationMatcher> baseAllInvocations;

    public WarningsFinder(List<Invocation> unusedStubs, List<InvocationMatcher> allInvocations) {
        
    }

    public void find(FindingsListener findingsListener) {
        
    }
}
