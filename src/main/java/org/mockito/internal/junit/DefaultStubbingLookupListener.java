/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

import static org.mockito.internal.stubbing.StrictnessSelector.determineStrictness;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.mockito.internal.exceptions.Reporter;
import org.mockito.internal.stubbing.UnusedStubbingReporting;
import org.mockito.invocation.Invocation;
import org.mockito.listeners.StubbingLookupEvent;
import org.mockito.listeners.StubbingLookupListener;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Stubbing;

/**
 * Default implementation of stubbing lookup listener.
 * Fails early if stub called with unexpected arguments, but only if current strictness is set to STRICT_STUBS.
 */
class DefaultStubbingLookupListener implements StubbingLookupListener, Serializable {

    private static final long serialVersionUID = -6789800638070123629L;

    private Strictness currentStrictness;
    private boolean mismatchesReported;

    DefaultStubbingLookupListener(Strictness strictness) {
        this.currentStrictness = strictness;
    }

    @Override
    public void onStubbingLookup(StubbingLookupEvent event) {
        Strictness actualStrictness =
        determineStrictness(
        event.getMockSettings().getStubbingLookupListeners().size(),
        event.getStubbings(),
        event.getTestOnlySuspendFunction());
        if (currentStrictness != actualStrictness) {
            // For now, we need to keep track of the current strictness to avoid reporting
            // unnecessary stubbings in case of strictness change.
            // We plan to rework the strictness concept in the future, to make it less error-prone.
            // See https://github.com/mockito/mockito/issues/2222
            currentStrictness = actualStrictness;
            mismatchesReported = false;
        }
        if (currentStrictness != Strictness.LENIENT && !mismatchesReported) {
            UnusedStubbingReporting.report(
            potentialArgMismatches(event.getInvocation(), event.getStubbings()));
            mismatchesReported = true;
        }
    }

    private static List<Invocation> potentialArgMismatches(
            Invocation invocation, Collection<Stubbing> stubbings) {
        List<Invocation> matchingStubbings = new LinkedList<>();
        for (Stubbing s : stubbings) {
            if (UnusedStubbingReporting.shouldBeReported(s)) {
                matchingStubbings.add(s.getInvocation());
            }
        }
        if (!matchingStubbings.isEmpty()) {
            return matchingStubbings;
        }
        return Reporter.compositeArgumentMismatchStubbings(invocation, stubbings);
    }

    /**
     * Enables resetting the strictness to desired level
     */
    void setCurrentStrictness(Strictness currentStrictness) {
        this.currentStrictness = currentStrictness;
    }

    /**
     * Indicates that stubbing argument mismatch was reported
     */
    boolean isMismatchesReported() {
        return mismatchesReported;
    }
}
