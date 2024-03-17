/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification.checkers;

import static org.mockito.internal.exceptions.Reporter.argumentsAreDifferent;
import static org.mockito.internal.exceptions.Reporter.wantedButNotInvoked;
import static org.mockito.internal.exceptions.Reporter.wantedButNotInvokedInOrder;
import static org.mockito.internal.invocation.InvocationsFinder.findAllMatchingUnverifiedChunks;
import static org.mockito.internal.invocation.InvocationsFinder.findInvocations;
import static org.mockito.internal.invocation.InvocationsFinder.findPreviousVerifiedInOrder;
import static org.mockito.internal.invocation.InvocationsFinder.findSimilarInvocation;
import static org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool.getNotMatchingArgsWithSameName;
import static org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mockito.internal.reporting.SmartPrinter;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.invocation.MatchableInvocation;

public class MissingInvocationChecker {

    private MissingInvocationChecker() { }

    public static void checkMissingInvocation(
            List<Invocation> invocations, MatchableInvocation wanted) {
        
    }

    public static void checkMissingInvocation(
            List<Invocation> invocations, MatchableInvocation wanted, InOrderContext context) {
        
    }
}
