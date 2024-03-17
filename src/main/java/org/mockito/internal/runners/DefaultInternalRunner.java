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
        
    }

    @Override
    public void run(final RunNotifier notifier) {
        
    }

    @Override
    public Description getDescription() {
        
    }

    @Override
    public void filter(Filter filter) throws NoTestsRemainException {
        
    }
}
