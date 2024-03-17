/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.debugging;

import java.io.Serializable;

import org.mockito.internal.exceptions.stacktrace.StackTraceFilter;
import org.mockito.invocation.Location;

class Java8LocationImpl implements Location, Serializable {

    private static final long serialVersionUID = -9054861157390980624L;
    // Limit the amount of objects being created, as this class is heavily instantiated:
    private static final StackTraceFilter stackTraceFilter = new StackTraceFilter();

    private String stackTraceLine;
    private String sourceFile;

    public Java8LocationImpl(Throwable stackTraceHolder, boolean isInline) {
        
    }

    private Java8LocationImpl(
            StackTraceFilter stackTraceFilter, Throwable stackTraceHolder, boolean isInline) {
        
    }

    @Override
    public String toString() {
        
    }

    /**
     * Eagerly compute the stacktrace line from the stackTraceHolder. Storing the Throwable is
     * memory-intensive for tests that have large stacktraces and have a lot of invocations on
     * mocks.
     */
    private void computeStackTraceInformation(
            StackTraceFilter stackTraceFilter, Throwable stackTraceHolder, boolean isInline) {
        
    }

    @Override
    public String getSourceFile() {
        
    }
}
