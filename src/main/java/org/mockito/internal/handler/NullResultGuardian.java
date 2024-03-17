/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.handler;

import static org.mockito.internal.util.Primitives.defaultValue;

import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationContainer;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;

/**
 * Protects the results from delegate MockHandler. Makes sure the results are valid.
 *
 * by Szczepan Faber, created at: 5/22/12
 */
class NullResultGuardian<T> implements MockHandler<T> {

    private final MockHandler<T> delegate;

    public NullResultGuardian(MockHandler<T> delegate) {
        
    }

    @Override
    public Object handle(Invocation invocation) throws Throwable {
        
    }

    @Override
    public MockCreationSettings<T> getMockSettings() {
        
    }

    @Override
    public InvocationContainer getInvocationContainer() {
        
    }
}
