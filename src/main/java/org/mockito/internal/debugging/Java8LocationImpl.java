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
        this(stackTraceFilter, stackTraceHolder, isInline);
    }

    private Java8LocationImpl(
            StackTraceFilter stackTraceFilter, Throwable stackTraceHolder, boolean isInline) {
        computeStackTraceInformation(stackTraceFilter, stackTraceHolder, isInline);
    }

    @Override
    public String toString() {
        return stackTraceLine;
    }

    /**
     * Eagerly compute the stacktrace line from the stackTraceHolder. Storing the Throwable is
     * memory-intensive for tests that have large stacktraces and have a lot of invocations on
     * mocks.
     */
    private void computeStackTraceInformation(
            StackTraceFilter stackTraceFilter, Throwable stackTraceHolder, boolean isInline) {
        for (StackTraceElement e : stackTraceHolder.getStackTrace()) {
            if (stackTraceFilter.isLineToBeSet(e)) {
                // On some JVMs like Android it's possible for the underlying data e.g.
                // the file name or the line number to be null.
                // For example in rare scenarios when the inlined event is lambda the line
                // number is null (not even -1).
                // To make the logging more robust we gonna defensively read that data and if it's
                // null we will log a message that explains
                // why we couldn't provide the data.
                sourceFile = e.getFileName();
                if (sourceFile == null) {
                    sourceFile = "Unknown source file";
                }
                if (isInline) {
                    stackTraceLine = "-> " + e.getLineNumber() + " : " + e.getClassName() + "." + e.getMethodName();
                } else {
                    stackTraceLine = e.getLineNumber() + " : " + e.getClassName() + "." + e.getMethodName();
                }
                return;
            }
        }
    }

    @Override
    public String getSourceFile() {
        return sourceFile;
    }
}
