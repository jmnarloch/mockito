/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.runners;

import static org.mockito.internal.runners.util.TestMethodsFinder.hasTestMethods;

import java.lang.reflect.InvocationTargetException;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.junit.MismatchReportingTestListener;
import org.mockito.internal.junit.MockitoTestListener;
import org.mockito.internal.junit.NoOpTestListener;
import org.mockito.internal.junit.StrictStubsRunnerTestListener;
import org.mockito.internal.runners.util.RunnerProvider;
import org.mockito.internal.util.Supplier;

/**
 * Creates instances of Mockito JUnit Runner in a safe way, e.g. detecting inadequate version of JUnit, etc.
 */
public class RunnerFactory {

    /** Creates silent runner implementation */
    public InternalRunner create(Class<?> klass) throws InvocationTargetException {
        
    }

    /**
     * Creates strict runner implementation
     */
    public InternalRunner createStrict(Class<?> klass) throws InvocationTargetException {
        
    }

    /**
     * Creates strict stubs runner implementation
     *
     * TODO, let's try to apply Brice suggestion and use switch + Strictness
     */
    public InternalRunner createStrictStubs(Class<?> klass) throws InvocationTargetException {
        
    }

    /**
     * Creates runner implementation with provided listener supplier
     */
    public InternalRunner create(Class<?> klass, Supplier<MockitoTestListener> listenerSupplier)
            throws InvocationTargetException {
        
    }
}
