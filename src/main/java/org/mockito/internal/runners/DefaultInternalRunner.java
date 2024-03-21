/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.runners;

import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.junit.DefaultTestFinishedEvent;
import org.mockito.internal.junit.MockitoTestListener;
import org.mockito.internal.util.Supplier;

public class DefaultInternalRunner implements InternalRunner {

    private final BlockJUnit4ClassRunner runner;

    public DefaultInternalRunner(
            Class<?> testClass, final Supplier<MockitoTestListener> listenerSupplier)
            throws InitializationError {
        MockitoAnnotations.initMocks(testClass);
        Mockito.framework().addListener(listenerSupplier.get());

        runner =
        new BlockJUnit4ClassRunner(testClass) {
            @Override
            protected Statement withAfters(
            final Statement statement, Object target, Description description) {
                // the input target is actually the test instance that has the mocks
                // injected
                // we need to return a statement that has the original test instance
                // therefore we return the statement with the original target (passed to
                // this method)
                final Statement actual = super.withAfters(statement, target, description);
                return new Statement() {
                    @Override
                    public void evaluate() throws Throwable {
                        // i. clear previous failures
                        Mockito.framework().clearInlineMocks();
                        // ii. execute test
                        actual.evaluate();
                        // iii. invoke listener
                        // notifier is null for @BeforeClass/@AfterClass
                        if (notifier != null) {
                            listenerSupplier.get().testFinished(
                            new DefaultTestFinishedEvent(getDescription(), new Runnable() {
                                @Override
                                public void run() {
                                    notifier.fireTestFailure(
                                    new Failure(getDescription(), new AssertionError("Listener intentionally failed the test")));
                                }
                            }));
                        }
                    }
                };
            }

            @Override
            protected void runChild(FrameworkMethod method, RunNotifier notifier) {
                Description methodDescription = describeChild(method);
                // methodDescription can be null if test is ignored, for example
                if (methodDescription != null) {
                    Mockito.framework()
                    .updateMockForMethod(
                    method.getMethod(), methodDescription.isTest());
                }
                super.runChild(method, notifier);
            }
        };
    }

    @Override
    public void run(final RunNotifier notifier) {
        runner.run(notifier);
    }

    @Override
    public Description getDescription() {
        return runner.getDescription();
    }

    @Override
    public void filter(Filter filter) throws NoTestsRemainException {
        runner.filter(filter);
    }
}
