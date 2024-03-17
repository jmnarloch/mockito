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
        
    }

    @Override
    public void onStubbingLookup(StubbingLookupEvent event) {
        
    }

    private static List<Invocation> potentialArgMismatches(
            Invocation invocation, Collection<Stubbing> stubbings) {
        
    }

    /**
     * Enables resetting the strictness to desired level
     */
    void setCurrentStrictness(Strictness currentStrictness) {
        
    }

    /**
     * Indicates that stubbing argument mismatch was reported
     */
    boolean isMismatchesReported() {
        
    }
}
