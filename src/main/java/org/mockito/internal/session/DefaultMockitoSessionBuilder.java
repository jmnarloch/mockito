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
        if (testClassInstance != null) {
            this.testClassInstances.add(testClassInstance);
        }
        return this;
    }

    @Override
    public MockitoSessionBuilder initMocks(Object... testClassInstances) {
        if (testClassInstances != null) {
            for (Object instance : testClassInstances) {
                initMocks(instance);
            }
        }
        return this;
    }

    @Override
    public MockitoSessionBuilder name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public MockitoSessionBuilder strictness(Strictness strictness) {
        this.strictness = strictness;
        return this;
    }

    @Override
    public MockitoSessionBuilder logger(MockitoSessionLogger logger) {
        this.logger = logger;
        return this;
    }

    @Override
    public MockitoSession startMocking() {
        if (logger == null) {
            logger = new OnStdOutLogger();
        }

        if (strictness == null) {
            strictness = Strictness.WARN;
        }

        if (testClassInstances.isEmpty()) {
            logger.log(
            StartMocksEvent.NONE,
            strictness,
            emptyList(),
            "The test double usage has been completed.");
        }

        return new DefaultMockitoSession(
        name,
        testClassInstances,
        strictness,
        logger,
        Plugins.getMockitoInvocationHandler());
    }
}
