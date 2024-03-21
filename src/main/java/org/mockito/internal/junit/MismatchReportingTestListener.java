/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.MockitoLogger;

/**
 * Reports stubbing argument mismatches to the supplied logger
 */
public class MismatchReportingTestListener implements MockitoTestListener {

    private final MockitoLogger logger;
    private List<Object> mocks = new LinkedList<>();

    public MismatchReportingTestListener(MockitoLogger logger) {
        this.logger = logger;
    }

    @Override
    public void testFinished(TestFinishedEvent event) {}

    @Override
    public void onMockCreated(Object mock, MockCreationSettings settings) {
        this.mocks.add(mock);
    }
}
