/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.invocation.MatchableInvocation;

public class InvocationsFinder {

    private InvocationsFinder() { }

    public static List<Invocation> findInvocations(
            List<Invocation> invocations, MatchableInvocation wanted) {
        
    }

    public static List<Invocation> findAllMatchingUnverifiedChunks(
            List<Invocation> invocations,
            MatchableInvocation wanted,
            InOrderContext orderingContext) {
        
    }

    /**
     * some examples how it works:
     *
     * Given invocations sequence:
     * 1,1,2,1
     *
     * if wanted is 1 and mode is times(2) then returns
     * 1,1
     *
     * if wanted is 1 and mode is atLeast() then returns
     * 1,1,1
     *
     * if wanted is 1 and mode is times(x), where x != 2 then returns
     * 1,1,1
     */
    public static List<Invocation> findMatchingChunk(
            List<Invocation> invocations,
            MatchableInvocation wanted,
            int wantedCount,
            InOrderContext context) {
        
    }

    private static List<Invocation> getFirstMatchingChunk(
            MatchableInvocation wanted, List<Invocation> unverified) {
        
    }

    public static Invocation findFirstMatchingUnverifiedInvocation(
            List<Invocation> invocations, MatchableInvocation wanted, InOrderContext context) {
        
    }

    public static Invocation findSimilarInvocation(
            List<Invocation> invocations, MatchableInvocation wanted) {
        
    }

    public static Invocation findFirstUnverified(List<Invocation> invocations) {
        
    }

    static Invocation findFirstUnverified(List<Invocation> invocations, Object mock) {
        
    }

    public static Location getLastLocation(List<Invocation> invocations) {
        
    }

    public static Invocation findPreviousVerifiedInOrder(
            List<Invocation> invocations, InOrderContext context) {
        
    }

    private static List<Invocation> removeVerifiedInOrder(
            List<Invocation> invocations, InOrderContext orderingContext) {
        
    }

    public static List<Location> getAllLocations(List<Invocation> invocations) {
        
    }

    /**
     * i3 is unverified here:
     *
     * i1, i2, i3
     *     v
     *
     * all good here:
     *
     * i1, i2, i3
     *     v   v
     *
     * @param context
     * @param orderedInvocations
     */
    public static Invocation findFirstUnverifiedInOrder(
            InOrderContext context, List<Invocation> orderedInvocations) {
        
    }
}
