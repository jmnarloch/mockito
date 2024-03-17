/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.framework;

import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;

import org.mockito.MockitoFramework;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.invocation.DefaultInvocationFactory;
import org.mockito.internal.util.Checks;
import org.mockito.invocation.InvocationFactory;
import org.mockito.listeners.MockitoListener;
import org.mockito.plugins.InlineMockMaker;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockitoPlugins;

public class DefaultMockitoFramework implements MockitoFramework {

    @Override
    public MockitoFramework addListener(MockitoListener listener) {
        
    }

    @Override
    public MockitoFramework removeListener(MockitoListener listener) {
        
    }

    @Override
    public MockitoPlugins getPlugins() {
        
    }

    @Override
    public InvocationFactory getInvocationFactory() {
        
    }

    private InlineMockMaker getInlineMockMaker() {
        
    }

    @Override
    public void clearInlineMocks() {
        
    }

    @Override
    public void clearInlineMock(Object mock) {
        
    }
}
