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
        this.builder = new StringBuilder();
    }

    @Override
    public Statement apply(final Statement base, final Description description) {

    }

    public void collectAndReport() throws MockitoAssertionError {
        MockingProgressImpl mockingProgress = mockingProgress();
        VerificationData data = mockingProgress.pullVerificationData();
        data.setFailuresParentFirst(this.numberOfFailures);
        mockingProgress.verificationFinished(data, data.getVerificationMode());
    }

    @Override
    public VerificationCollector assertLazily() {
        mockingProgress()
        .setVerificationStrategy(
        new VerificationStrategy() {
            @Override
            public VerificationMode maybeVerifyLazily(VerificationMode mode) {
                return new VerificationWrapper(mode);
            }
        });
        return this;
    }

    private void resetBuilder() {
        this.builder = new StringBuilder();
        this.numberOfFailures = 0;
    }

    private void append(String message) {
        this.numberOfFailures++;
        this.builder.append(this.numberOfFailures).append(". ").append(message).append('\n');
    }

    private class VerificationWrapper implements VerificationMode {

        private final VerificationMode delegate;

        private VerificationWrapper(VerificationMode delegate) {
            this.delegate = delegate;
        }

        @Override
        public void verify(VerificationData data) {
            try {
                delegate.verify(data);
            } catch (MockitoAssertionError e) {
                numberOfFailures++;
                append("\n* " + e.getMessage());
            }
        }

        @Override
        public VerificationMode description(String description) {
            return delegate.description(description);
        }
    }
}
