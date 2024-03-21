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
        this(System.out);
    }

    public VerboseMockInvocationLogger(PrintStream printStream) {
        this.printStream = printStream;
    }

    @Override
    public void reportInvocation(MethodInvocationReport methodInvocationReport) {
        printHeader();
        printStubInfo(methodInvocationReport);
        printInvocation(methodInvocationReport.getInvocation());
        printReturnedValueOrThrowable(methodInvocationReport);
        printFooter();
    }

    private void printReturnedValueOrThrowable(MethodInvocationReport methodInvocationReport) {
        if (methodInvocationReport.threwException()) {
            String message =
            methodInvocationReport.getThrowable().getMessage() == null
            ? ""
            : " with message " + methodInvocationReport.getThrowable().getMessage();
            printlnIndented(
            "has thrown: " + methodInvocationReport.getThrowable().getClass() + message);
        } else {
            String hasReturned = methodInvocationReport.returnsValue() ? "has returned" : " ";
            String returnValue =
            methodInvocationReport.returnsValue()
            ? ": " + methodInvocationReport.getReturnedValue()
            : "";
            printlnIndented(hasReturned + returnValue);
        }
    }

    private void printStubInfo(MethodInvocationReport methodInvocationReport) {
        if (methodInvocationReport.getLocationOfStubbing() != null) {
            printlnIndented("stubbed: " + methodInvocationReport.getLocationOfStubbing());
        }
    }

    private void printHeader() {
        mockInvocationsCounter++;
        printStream.println(
        "############ Logging method invocation #"
        + mockInvocationsCounter
        + " on mock/spy ########");
    }

    private void printInvocation(DescribedInvocation invocation) {
        printHeader();
        printlnIndented("Mock will answer the following for the " + mockInvocationsCounter + "th invocation");
        printHeader();
        printlnIndented(invocation.toString());
        printStubInfo(invocation);
        printReturnedValueOrThrowable(invocation);
        printFooter();
    }

    private void printFooter() {
        printStream.println();
    }

    private void printlnIndented(String message) {
        printStream.println("    " + message);
    }
}
