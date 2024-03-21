/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.mockito.internal.exceptions.Reporter;
import org.mockito.invocation.Invocation;
import org.mockito.plugins.MockitoLogger;
import org.mockito.stubbing.Stubbing;

/**
 * Contains unused stubbings, knows how to format them
 */
public class UnusedStubbings {

    private final Collection<? extends Stubbing> unused;

    UnusedStubbings(Collection<? extends Stubbing> unused) {
        this.unused = unused;
    }

    void format(String testName, MockitoLogger logger) {
        if (unused.isEmpty()) {
            return;
        }

        StubbingHint hint = new StubbingHint(testName);
        int x = 1;
        for (Stubbing candidate : unused) {
            hint.appendLine(x++, candidate.getInvocation());
        }

        logger.log(hint.toString());
    }

    public int size() {
        return unused.size();
    }

    @Override
    public String toString() {
        return unused.toString();
    }

    void reportUnused() {
        if (unused.isEmpty()) {
            return;
        }

        for (Stubbing notUsed : unused) {
            notUsed.getInvocation().printInvocLocation();
        }

        throw Reporter.unncessaryStubbingException(unused);
    }
}
