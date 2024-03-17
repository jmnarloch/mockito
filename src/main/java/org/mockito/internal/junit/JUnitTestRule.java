/*
 * Copyright (c) 2020 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.mockito.junit.MockitoTestRule;
import org.mockito.plugins.MockitoLogger;
import org.mockito.quality.Strictness;

public final class JUnitTestRule implements MockitoTestRule {

    private final Object testInstance;
    private final JUnitSessionStore sessionStore;

    public JUnitTestRule(MockitoLogger logger, Strictness strictness, Object testInstance) {
        
    }

    @Override
    public Statement apply(Statement base, Description description) {
        
    }

    @Override
    public MockitoTestRule silent() {
        
    }

    @Override
    public MockitoTestRule strictness(Strictness strictness) {
        
    }
}
