/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.framework;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.MockitoSession;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.misusing.RedundantListenerException;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.internal.junit.TestFinishedEvent;
import org.mockito.internal.junit.UniversalTestListener;
import org.mockito.plugins.MockitoLogger;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;

public class DefaultMockitoSession implements MockitoSession {

    private final String name;
    private final UniversalTestListener listener;

    private final List<AutoCloseable> closeables = new ArrayList<>();

    public DefaultMockitoSession(
            List<Object> testClassInstances,
            String name,
            Strictness strictness,
            MockitoLogger logger) {
        
    }

    @Override
    public void setStrictness(Strictness strictness) {
        
    }

    @Override
    public void finishMocking() {
        
    }

    @Override
    public void finishMocking(final Throwable failure) {
        
    }

    private void release() {
        
    }
}
