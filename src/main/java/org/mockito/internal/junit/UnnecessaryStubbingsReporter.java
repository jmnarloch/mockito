/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.invocation.Invocation;
import org.mockito.listeners.MockCreationListener;
import org.mockito.mock.MockCreationSettings;

/**
 * Reports unnecessary stubbings
 */
public class UnnecessaryStubbingsReporter implements MockCreationListener {

    private final List<Object> mocks = new LinkedList<Object>();

    public void validateUnusedStubs(Class<?> testClass, RunNotifier notifier) {
        UnusedStubbingReporting.printUnusedStubbings(
        new Description() {
            @Override
            public String getDisplayName() {
                return testClass.getName();
            }

            @Override
            public String toString() {
                return testClass.getName();
            }
        },
        new RunNotifierWrapper(notifier));
    }

    @Override
    public void onMockCreated(Object mock, MockCreationSettings settings) {
        mocks.add(mock);
    }
}
