/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.handler;

import static org.mockito.internal.listeners.StubbingLookupNotifier.notifyStubbedAnswerLookup;
import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;

import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.answers.DefaultAnswerValidator;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.verification.MockAwareVerificationMode;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationContainer;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.verification.VerificationMode;

/**
 * Invocation handler set on mock objects.
 *
 * @param <T> type of mock object to handle
 */
public class MockHandlerImpl<T> implements MockHandler<T> {

    private static final long serialVersionUID = -2917871070982574165L;

    InvocationContainerImpl invocationContainer;

    MatchersBinder matchersBinder = new MatchersBinder();

    private final MockCreationSettings<T> mockSettings;

    public MockHandlerImpl(MockCreationSettings<T> mockSettings) {
        
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
