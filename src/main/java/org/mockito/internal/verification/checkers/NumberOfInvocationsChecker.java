/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification.checkers;

import static org.mockito.internal.exceptions.Reporter.neverWantedButInvoked;
import static org.mockito.internal.exceptions.Reporter.tooFewActualInvocations;
import static org.mockito.internal.exceptions.Reporter.tooFewActualInvocationsInOrder;
import static org.mockito.internal.exceptions.Reporter.tooManyActualInvocations;
import static org.mockito.internal.exceptions.Reporter.tooManyActualInvocationsInOrder;
import static org.mockito.internal.invocation.InvocationMarker.markVerified;
import static org.mockito.internal.invocation.InvocationMarker.markVerifiedInOrder;
import static org.mockito.internal.invocation.InvocationsFinder.findFirstMatchingUnverifiedInvocation;
import static org.mockito.internal.invocation.InvocationsFinder.findInvocations;
import static org.mockito.internal.invocation.InvocationsFinder.findMatchingChunk;
import static org.mockito.internal.invocation.InvocationsFinder.getAllLocations;

import java.util.Arrays;
import java.util.List;

import org.mockito.internal.reporting.Discrepancy;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.invocation.MatchableInvocation;

public class NumberOfInvocationsChecker {

    private NumberOfInvocationsChecker() {}

    public static void checkNumberOfInvocations(
            List<Invocation> invocations, MatchableInvocation wanted, int wantedCount) {
        List<Invocation> actualInvocations = findInvocations(invocations, wanted);

        int actualCount = actualInvocations.size();
        if (wantedCount != actualCount) {
            throw tooManyActualInvocations(wanted, wantedCount, actualCount);
        }

        markVerified(actualInvocations, wanted);
    }

    public static void checkNumberOfInvocations(
            List<Invocation> invocations,
            MatchableInvocation wanted,
            int wantedCount,
            InOrderContext context) {
        int actualCount = 0;
        while (true) {
            Invocation next = findFirstMatchingUnverifiedInvocation(invocations, wanted, context);
            if (next == null) {
                break;
            }
            markVerifiedInOrder(next, wanted, context);
            actualCount++;
        }

        if (wantedCount != actualCount) {
            List<Invocation> chunk = findMatchingChunk(invocations, wanted, context);
            throw tooManyActualInvocationsInOrder(
            new Discrepancy(wantedCount, chunk.size()), wanted, getAllLocations(chunk));
        }
    }

    public static void checkNumberOfInvocationsNonGreedy(
            List<Invocation> invocations,
            MatchableInvocation wanted,
            int wantedCount,
            InOrderContext context) {
        int actualCount = 0;
        Location lastLocation = null;
        while (actualCount < wantedCount) {
            Invocation next = findFirstMatchingUnverifiedInvocation(invocations, wanted, context);
            if (next == null) {
                List<Location> allLocations = getAllLocations(invocations);
                throw tooFewActualInvocationsInOrder(
                new Discrepancy(wantedCount, actualCount), wanted, allLocations, context);
            }
            markVerified(next, wanted);
            context.markVerified(next);
            lastLocation = next.getLocation();
            actualCount++;
        }
    }
}
