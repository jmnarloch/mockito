/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation.finder;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.mockito.internal.invocation.InvocationComparator;
import org.mockito.internal.stubbing.StubbingComparator;
import org.mockito.internal.util.DefaultMockingDetails;
import org.mockito.invocation.Invocation;
import org.mockito.stubbing.Stubbing;

public class AllInvocationsFinder {

    private AllInvocationsFinder() {}

    /**
     * gets all invocations from mocks. Invocations are ordered earlier first.
     *
     * @param mocks mocks
     * @return invocations
     */
    public static List<Invocation> find(Iterable<?> mocks) {
        Set<Invocation> invocationsInOrder = new TreeSet<>(new InvocationComparator());
        for (Object mock : mocks) {
            invocationsInOrder.addAll(
            MockUtil.getInvocationContainer(mock).getInvocationsInOrder());
        }

        return new LinkedList<>(invocationsInOrder);
    }

    /**
     * Gets all stubbings from mocks. Invocations are ordered earlier first.
     *
     * @param mocks mocks
     * @return stubbings
     */
    public static Set<Stubbing> findStubbings(Iterable<?> mocks) {
        Set<Stubbing> stubbings = new TreeSet<>(new StubbingComparator());
        for (Invocation i : find(mocks)) {
            stubbings.add(i.getStubbing());
        }
        return stubbings;
    }
}
