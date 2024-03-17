/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.handler;

import static org.mockito.internal.exceptions.Reporter.invocationListenerThrewException;

import java.util.List;

import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationContainer;
import org.mockito.invocation.MockHandler;
import org.mockito.listeners.InvocationListener;
import org.mockito.mock.MockCreationSettings;

/**
 * Handler, that call all listeners wanted for this mock, before delegating it
 * to the parameterized handler.
 */
class InvocationNotifierHandler<T> implements MockHandler<T> {

    private final List<InvocationListener> invocationListeners;
    private final MockHandler<T> mockHandler;

    public InvocationNotifierHandler(MockHandler<T> mockHandler, MockCreationSettings<T> settings) {
        
    }

    @Override
    public Object handle(Invocation invocation) throws Throwable {
        
    }

    private void notifyMethodCall(Invocation invocation, Object returnValue) {
        
    }

    private void notifyMethodCallException(Invocation invocation, Throwable exception) {
        
    }

    @Override
    public MockCreationSettings<T> getMockSettings() {
        
    }

    @Override
    public InvocationContainer getInvocationContainer() {
        
    }
}
