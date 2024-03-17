/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.runners;

import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunNotifier;
import org.mockito.Mockito;
import org.mockito.internal.junit.UnnecessaryStubbingsReporter;
import org.mockito.internal.runners.util.FailureDetector;

public class StrictRunner implements InternalRunner {

    private final Class<?> testClass;
    private final InternalRunner runner;
    private boolean testsSkipped;

    /**
     * @param runner - the runner to wrap around
     * @param testClass - for reporting purposes
     */
    public StrictRunner(InternalRunner runner, Class<?> testClass) {
        
    }

    @Override
    public void run(RunNotifier notifier) {
        // TODO need to be able to opt in for full stack trace instead of just relying on the stack
        // trace filter
        
    }

    @Override
    public Description getDescription() {
        
    }

    @Override
    public void filter(Filter filter) throws NoTestsRemainException {
        
    }

    private class RecordingFilter extends Filter {

        private final Filter delegate;

        public RecordingFilter(Filter delegate) {
            
        }

        @Override
        public void apply(Object child) throws NoTestsRemainException {
            
        }

        @Override
        public Filter intersect(Filter second) {
            
        }

        @Override
        public boolean shouldRun(Description description) {
            
        }

        @Override
        public String describe() {
            
        }
    }
}
