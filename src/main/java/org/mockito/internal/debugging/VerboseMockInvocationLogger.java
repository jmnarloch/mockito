/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.debugging;

import java.io.PrintStream;

import org.mockito.invocation.DescribedInvocation;
import org.mockito.listeners.InvocationListener;
import org.mockito.listeners.MethodInvocationReport;

/**
 * Logs all invocations to standard output.
 *
 * Used for debugging interactions with a mock.
 */
public class VerboseMockInvocationLogger implements InvocationListener {

    // visible for testing
    final PrintStream printStream;

    private int mockInvocationsCounter = 0;

    public VerboseMockInvocationLogger() {
        
    }

    public VerboseMockInvocationLogger(PrintStream printStream) {
        
    }

    @Override
    public void reportInvocation(MethodInvocationReport methodInvocationReport) {
        
    }

    private void printReturnedValueOrThrowable(MethodInvocationReport methodInvocationReport) {
        
    }

    private void printStubInfo(MethodInvocationReport methodInvocationReport) {
        
    }

    private void printHeader() {
        
    }

    private void printInvocation(DescribedInvocation invocation) {
        
    }

    private void printFooter() {
        
    }

    private void printlnIndented(String message) {
        
    }
}
