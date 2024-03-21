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

    private InvocationsFinder() {}

    public static List<Invocation> findInvocations(
            List<Invocation> invocations, MatchableInvocation wanted) {
        return invocations.stream().filter(wanted::matches).collect(Collectors.toList());
    }

    public static List<Invocation> findAllMatchingUnverifiedChunks(
            List<Invocation> invocations,
            MatchableInvocation wanted,
            InOrderContext orderingContext) {
        List<Invocation> unverified = removeVerifiedInOrder(invocations, orderingContext);
        return unverified.stream().filter(wanted::matches).collect(Collectors.toList());
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
        List<Invocation> chunk = new LinkedList<>();
        for (Invocation i : invocations) {
            if (context.isVerified(i)) {
                chunk.add(i);
                if (wanted.matches(i) && wantedCount == chunk.size()) {
                    break;
                }
            } else if (!chunk.isEmpty()) {
                if (wanted.matches(i)) {
                    chunk.add(i);
                } else {
                    chunk.clear();
                }
            }
        }
        return chunk;
    }

    private static List<Invocation> getFirstMatchingChunk(
            MatchableInvocation wanted, List<Invocation> unverified) {
        return unverified.stream().takeWhile(wanted::matches).collect(Collectors.toList());
    }

    public static Invocation findFirstMatchingUnverifiedInvocation(
            List<Invocation> invocations, MatchableInvocation wanted, InOrderContext context) {
        for (Invocation invocation : removeVerifiedInOrder(invocations, context)) {
            if (wanted.matches(invocation)) {
                return invocation;
            }
        }
        return null;
    }

    public static Invocation findSimilarInvocation(
            List<Invocation> invocations, MatchableInvocation wanted) {
        Invocation bestMatch = null;
        Integer bestDistance = null;
        for (Invocation invocation : invocations) {
            if (!wanted.hasSimilarMethod(invocation)) {
                continue;
            }
            int distance = wanted.distanceOfClosestMatch(invocation);
            if (bestDistance == null || distance < bestDistance) {
                bestDistance = distance;
                bestMatch = invocation;
            }
        }

        return bestMatch;
    }

    public static Invocation findFirstUnverified(List<Invocation> invocations) {
        for (Invocation i : invocations) {
            if (!i.isVerified()) {
                return i;
            }
        }
        return null;
    }

    static Invocation findFirstUnverified(List<Invocation> invocations, Object mock) {
        for (Invocation i : invocations) {
            boolean mockIsValid = mock == null || i.getMock() == mock;
            if (mockIsValid && !i.isVerified()) {
                return i;
            }
        }
        return null;
    }

    public static Location getLastLocation(List<Invocation> invocations) {
        Invocation last = invocations.get(invocations.size() - 1);
        return last.getLocation();
    }

    public static Invocation findPreviousVerifiedInOrder(
            List<Invocation> invocations, InOrderContext context) {
        List<Invocation> verifiedOnly =
        invocations.stream().filter(context::isVerified).collect(Collectors.toList());

        if (verifiedOnly.isEmpty()) {
            return null;
        }
        if (verifiedOnly.size() < 2) {
            return verifiedOnly.get(0);
        }
        return verifiedOnly.get(verifiedOnly.size() - 2);
    }

    private static List<Invocation> removeVerifiedInOrder(
            List<Invocation> invocations, InOrderContext orderingContext) {
        return invocations.stream()
        .filter(orderingContext::isVerified)
        .collect(Collectors.toList());
    }

    public static List<Location> getAllLocations(List<Invocation> invocations) {
        List<Location> locations = new LinkedList<>();
        for (Invocation i : invocations) {
            locations.add(i.getLocation());
        }
        return locations;
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
        Invocation candidate = null;
        for (Invocation i : orderedInvocations) {
            if (context.isVerified(i)) {
                candidate = null;
            } else if (candidate == null) {
                candidate = i;
            }
        }
        return candidate;
    }
}
