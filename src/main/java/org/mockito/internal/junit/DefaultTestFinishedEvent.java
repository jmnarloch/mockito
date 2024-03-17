/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

public class DefaultTestFinishedEvent implements TestFinishedEvent {
    private final Object testClassInstance;
    private final String testMethodName;
    private final Throwable testFailure;

    public DefaultTestFinishedEvent(
            Object testClassInstance, String testMethodName, Throwable testFailure) {
        
    }

    @Override
    public Throwable getFailure() {
        
    }

    @Override
    public String getTestName() {
        
    }
}
