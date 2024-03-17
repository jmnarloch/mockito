/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.exceptions.stacktrace;

import org.mockito.exceptions.stacktrace.StackTraceCleaner;

/**
 * This predicate is used to filter "good" {@link StackTraceElement}. Good
 */
public class DefaultStackTraceCleaner implements StackTraceCleaner {

    @Override
    public boolean isIn(StackTraceElement e) {
        
    }

    @Override
    public boolean isIn(StackFrameMetadata e) {
        
    }

    private boolean isIn(String className) {
        
    }

    /* Some mock makers (like inline) use java.lang.invoke.MethodHandle to dispatch calls */
    private static boolean isMethodHandle(String className) {
        
    }

    private static boolean isMockDispatcher(String className) {
        
    }

    private static boolean isFromMockito(String className) {
        
    }

    private static boolean isFromMockitoRule(String className) {
        
    }

    private static boolean isFromMockitoRunner(String className) {
        
    }
}
