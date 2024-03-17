/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.debugging;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.stacktrace.StackTraceCleaner;
import org.mockito.exceptions.stacktrace.StackTraceCleaner.StackFrameMetadata;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.exceptions.stacktrace.DefaultStackTraceCleaner;
import org.mockito.invocation.Location;

import java.io.Serializable;
import java.lang.StackWalker.Option;
import java.lang.StackWalker.StackFrame;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class LocationImpl implements Location, Serializable {
    private static final long serialVersionUID = 2954388321980069195L;

    private static final String UNEXPECTED_ERROR_SUFFIX =
            "\nThis is unexpected and is likely due to a change in either Java's StackWalker or Reflection APIs."
                    + "\nIt's worth trying to upgrade to a newer version of Mockito, or otherwise to file a bug report.";

    /**
     * This is an unfortunate buffer. Inside StackWalker, a buffer is created, which is resized by
     * doubling. The resizing also allocates a tonne of StackFrame elements. If we traverse more than
     * BUFFER_SIZE elements, the resulting resize can significantly affect the overall cost of the operation.
     * If we traverse fewer than this number, we are inefficient. Empirically, 16 is enough stack frames
     * for a simple stub+call operation to succeed without resizing, as measured on Java 11.
     */
    private static final int BUFFER_SIZE = 16;

    private static final StackWalker STACK_WALKER = stackWalker();

    private static final String PREFIX = "-> at ";

    private static final StackTraceCleaner CLEANER =
            Plugins.getStackTraceCleanerProvider()
                    .getStackTraceCleaner(new DefaultStackTraceCleaner());

    /**
     * In Java, allocating lambdas is cheap, but not free. stream.map(this::doSomething)
     * will allocate a Function object each time the function is called (although not
     * per element). By assigning these Functions and Predicates to variables, we can
     * avoid the memory allocation.
     */
    private static final Function<StackFrame, StackFrameMetadata> toStackFrameMetadata =
            MetadataShim::new;

    private static final Predicate<StackFrameMetadata> cleanerIsIn = CLEANER::isIn;

    private static final int FRAMES_TO_SKIP = framesToSkip();

    private final StackFrameMetadata sfm;
    private volatile String stackTraceLine;

    LocationImpl(boolean isInline) {
        
    }

    @Override
    public String getSourceFile() {
        
    }

    @Override
    public String toString() {
        
    }

    private String stackTraceLine() {
        
    }

    private static StackFrameMetadata getStackFrame(boolean isInline) {
        
    }

    private static boolean usingDefaultStackTraceCleaner() {
        
    }

    private static String noStackTraceFailureMessage() {
        
    }

    /**
     * In order to trigger the stack walker, we create some reflective frames. These need to be skipped so as to
     * ensure there are no non-Mockito frames at the top of the stack trace.
     */
    private static int framesToSkip() {
        
    }

    private static <T> T stackWalk(Function<Stream<StackFrame>, T> function) {
        
    }

    private static StackWalker stackWalker() {
        
    }

    private static final class MetadataShim implements StackFrameMetadata, Serializable {
        private static final long serialVersionUID = 8491903719411428648L;
        private final StackFrame stackFrame;

        private MetadataShim(StackFrame stackFrame) {
            
        }

        @Override
        public String getClassName() {
            
        }

        @Override
        public String getMethodName() {
            
        }

        @Override
        public String getFileName() {
            
        }

        @Override
        public int getLineNumber() {
            
        }

        @Override
        public String toString() {
            
        }

        /**
         * Ensure that this type remains serializable.
         */
        private Object writeReplace() {
            
        }
    }

    private static final class SerializableShim implements StackFrameMetadata, Serializable {
        private static final long serialVersionUID = 7908320459080898690L;
        private final StackTraceElement ste;

        private SerializableShim(StackTraceElement ste) {
            
        }

        @Override
        public String getClassName() {
            
        }

        @Override
        public String getMethodName() {
            
        }

        @Override
        public String getFileName() {
            
        }

        @Override
        public int getLineNumber() {
            
        }
    }
}
