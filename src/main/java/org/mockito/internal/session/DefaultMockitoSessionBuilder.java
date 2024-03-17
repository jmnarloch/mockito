/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.session;

import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.List;

import org.mockito.MockitoSession;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.framework.DefaultMockitoSession;
import org.mockito.plugins.MockitoLogger;
import org.mockito.quality.Strictness;
import org.mockito.session.MockitoSessionBuilder;
import org.mockito.session.MockitoSessionLogger;

public class DefaultMockitoSessionBuilder implements MockitoSessionBuilder {

    private final List<Object> testClassInstances = new ArrayList<Object>();
    private String name;
    private Strictness strictness;
    private MockitoSessionLogger logger;

    @Override
    public MockitoSessionBuilder initMocks(Object testClassInstance) {
        
    }

    @Override
    public MockitoSessionBuilder initMocks(Object... testClassInstances) {
        
    }

    @Override
    public MockitoSessionBuilder name(String name) {
        
    }

    @Override
    public MockitoSessionBuilder strictness(Strictness strictness) {
        
    }

    @Override
    public MockitoSessionBuilder logger(MockitoSessionLogger logger) {
        
    }

    @Override
    public MockitoSession startMocking() {
        // Configure default values
        
    }
}
