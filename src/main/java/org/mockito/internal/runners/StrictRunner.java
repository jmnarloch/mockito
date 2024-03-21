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
        this.testClass = testClass;
        this.runner = runner;
        this.testsSkipped = false;
        runner.setUnnecessaryStubbingsListener(
        new UnnecessaryStubbingsReporter(testClass.getSimpleName()));
        RecordingFilter filter = new RecordingFilter(new FailureDetector());
        filter.andThen(runner);
        filter.andThen(new FailurePrinter(testClass));
        try {
            // not JUnit 4.12
            runner.filter(filter);
        } catch (NoTestsRemainException e) {
            // that's fine, we just want to make sure there are no unnecessary stubbings
        }
    }

    @Override
    public void run(RunNotifier notifier) {
        FailureDetector listener = new FailureDetector();
        UnnecessaryStubbingsReporter reporter = new UnnecessaryStubbingsReporter(testClass);

        Mockito.framework().addListener(reporter);

        try {
            runner.run(notifier);
        } finally {
            Mockito.framework().removeListener(reporter);
            if (testsSkipped && !listener.isFailureDetected()) {
                reporter.report();
            }
        }
    }

    @Override
    public Description getDescription() {
        return runner.getDescription();
    }

    @Override
    public void filter(Filter filter) throws NoTestsRemainException {
        Filter recordingFilter = new RecordingFilter(filter);
        runner.filter(recordingFilter);
    }

    private class RecordingFilter extends Filter {

        private final Filter delegate;

        public RecordingFilter(Filter delegate) {
            this.delegate = delegate;
        }

        @Override
        public void apply(Object child) throws NoTestsRemainException {
            delegate.apply(child);
        }

        @Override
        public Filter intersect(Filter second) {
            return delegate.intersect(second);
        }

        @Override
        public boolean shouldRun(Description description) {
            return delegate.shouldRun(description);
        }

        @Override
        public String describe() {
            return delegate.describe();
        }
    }
}
