/*
 * Copyright (c) 2020 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

import org.junit.runners.model.Statement;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.MockitoSession;
import org.mockito.internal.session.MockitoSessionLoggerAdapter;
import org.mockito.plugins.MockitoLogger;
import org.mockito.quality.Strictness;

class JUnitSessionStore {

    private final MockitoLogger logger;
    private MockitoSession session;
    protected Strictness strictness;

    JUnitSessionStore(MockitoLogger logger, Strictness strictness) {
        
    }

    Statement createStatement(final Statement base, final String methodName, final Object target) {
        
    }

    void setStrictness(Strictness strictness) {
        
    }
}
