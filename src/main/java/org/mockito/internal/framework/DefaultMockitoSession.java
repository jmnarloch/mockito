/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.framework;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.MockitoSession;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.misusing.RedundantListenerException;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.internal.junit.TestFinishedEvent;
import org.mockito.internal.junit.UniversalTestListener;
import org.mockito.plugins.MockitoLogger;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;

public class DefaultMockitoSession implements MockitoSession {

    private final String name;
    private final UniversalTestListener listener;

    private final List<AutoCloseable> closeables = new ArrayList<>();

    public DefaultMockitoSession(
            List<Object> testClassInstances,
            String name,
            Strictness strictness,
            MockitoLogger logger) {
        this.name = name;
        listener = new UniversalTestListener(strictness, logger);
        try {
            for (Object testClassInstance : testClassInstances) {
                MockitoSession oldSession = Mockito.framework().getMockitoSession(testClassInstance);
                if (oldSession != null && oldSession.isOpen()) {
                    throw new RedundantListenerException(
                    "This could mean that you already initiated (or finished) mocking in this test method - "
                    + "you should keep mocking in one place to keep test code clean and reliable.");
                }
                MockitoAnnotations.openMocks(testClassInstance);
                closeables.add(
                () -> {
                    try {
                        MockitoAnnotations.closeMocks(testClassInstance);
                    } finally {
                        testClassInstance = null;
                    }
                });
            }
        } catch (RuntimeException | Error e) {
            // something wrong happened, we need to guarantee that we release resources
            listener.testFinished(new TestFinishedEvent(name, e, false));
            throw e;
        }
    }

    @Override
    public void setStrictness(Strictness strictness) {
        listener.setStrictness(strictness);
    }

    @Override
    public void finishMocking() {
        finishMocking(null);
    }

    @Override
    public void finishMocking(final Throwable failure) {
        try {
            // Unregister all closeable and then release
            for (AutoCloseable closeable : closeables) {
                try {
                    closeable.close();
                } catch (Exception e) {
                    if (failure != null) {
                        failure.addSuppressed(e);
                    } else {
                        throw new RedundantListenerException(
                        "Test completed with failure, please see test details for stack trace on redundant listener problem.",
                        e);
                    }
                }
            }
            // release resources
            release();
        } finally {
            // Validate all stubs and auto-close mocks
            List<Throwable> errors = new ArrayList<>();
            for (MockitoSessionValidator validator : validators) {
                validator.validateAllStubsAndMocks(errors, failure);
            }
            for (Throwable error : errors) {
                Reporter.unexpectedMockitoValidationException(error, error.getMessage());
            }
        }
    }

    private void release() {
        for (AutoCloseable closeable : closeables) {
            try {
                closeable.close();
            } catch (Exception e) {
                throw new MockitoException("Failed to release " + closeable, e);
            }
        }
    }
}
