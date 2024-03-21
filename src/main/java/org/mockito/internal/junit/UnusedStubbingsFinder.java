/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.mockito.internal.invocation.finder.AllInvocationsFinder;
import org.mockito.internal.stubbing.UnusedStubbingReporting;
import org.mockito.invocation.Invocation;
import org.mockito.stubbing.Stubbing;

/**
 * Finds unused stubbings
 */
public class UnusedStubbingsFinder {

    /**
     * Gets all unused stubbings for given set of mock objects, in order.
     * Stubbings explicitily marked as LENIENT are not included.
     */
    public UnusedStubbings getUnusedStubbings(Iterable<Object> mocks) {
        return new UnusedStubbings(
        AllInvocationsFinder.findStubbings(mocks).stream()
        .filter(UnusedStubbingReporting::shouldBeReported)
        .collect(Collectors.toList()));
    }

    /**
     * Gets unused stubbings per location. This method is less accurate than {@link #getUnusedStubbings(Iterable)}.
     * It considers that stubbings with the same location (e.g. ClassFile + line number) are the same.
     * This is not completely accurate because a stubbing declared in a setup or constructor
     * is created per each test method. Because those are different test methods,
     * different mocks are created, different 'Invocation' instance is backing the 'Stubbing' instance.
     * In certain scenarios (detecting unused stubbings by JUnit runner), we need this exact level of accuracy.
     * Stubbing declared in constructor but realized in % of test methods is considered as 'used' stubbing.
     * There are high level unit tests that demonstrate this scenario.
     */
    public Collection<Invocation> getUnusedStubbingsByLocation(Iterable<Object> mocks) {
        Set<Stubbing> stubbings = UnusedStubbingReporting.stubbingsByLocation(mocks);

        Map<String, Invocation> allLocations =
        AllInvocationsFinder.findStubbings(mocks).stream()
        .collect(Collectors.toMap(UnusedStubbingsFinder::getLocation, s -> s));

        return stubbings.stream()
        .filter(s -> !allLocations.containsKey(getLocation(s))))
        .collect(Collectors.toList());
    }
}
