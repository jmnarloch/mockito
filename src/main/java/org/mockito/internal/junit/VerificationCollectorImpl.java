/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.progress.MockingProgressImpl;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.junit.VerificationCollector;
import org.mockito.verification.VerificationMode;
import org.mockito.verification.VerificationStrategy;

/**
 * Mockito implementation of VerificationCollector.
 */
public class VerificationCollectorImpl implements VerificationCollector {

    private StringBuilder builder;
    private int numberOfFailures;

    public VerificationCollectorImpl() {
        
    }

    @Override
    public Statement apply(final Statement base, final Description description) {
        
    }

    public void collectAndReport() throws MockitoAssertionError {
        
    }

    @Override
    public VerificationCollector assertLazily() {
        
    }

    private void resetBuilder() {
        
    }

    private void append(String message) {
        
    }

    private class VerificationWrapper implements VerificationMode {

        private final VerificationMode delegate;

        private VerificationWrapper(VerificationMode delegate) {
            
        }

        @Override
        public void verify(VerificationData data) {
            
        }

        @Override
        public VerificationMode description(String description) {
            
        }
    }
}
