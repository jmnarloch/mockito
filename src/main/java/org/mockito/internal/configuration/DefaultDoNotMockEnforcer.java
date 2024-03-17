/*
 * Copyright (c) 2019 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import java.lang.annotation.Annotation;

import org.mockito.DoNotMock;
import org.mockito.plugins.DoNotMockEnforcer;

public class DefaultDoNotMockEnforcer implements DoNotMockEnforcer {

    @Override
    public String checkTypeForDoNotMockViolation(Class<?> type) {
        
    }
}
