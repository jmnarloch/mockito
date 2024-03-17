/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import org.mockito.MockedConstruction;
import org.mockito.creation.instance.Instantiator;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.InlineMockMaker;

import java.util.Optional;
import java.util.function.Function;

public class InlineByteBuddyMockMaker
        implements ClassCreatingMockMaker, InlineMockMaker, Instantiator {
    private final InlineDelegateByteBuddyMockMaker inlineDelegateByteBuddyMockMaker;

    public InlineByteBuddyMockMaker() {
        
    }

    InlineByteBuddyMockMaker(InlineDelegateByteBuddyMockMaker inlineDelegateByteBuddyMockMaker) {
        
    }

    @Override
    public <T> T newInstance(Class<T> cls) {
        
    }

    @Override
    public <T> Class<? extends T> createMockType(MockCreationSettings<T> settings) {
        
    }

    @Override
    public void clearMock(Object mock) {
        
    }

    @Override
    public void clearAllMocks() {
        
    }

    @Override
    public <T> T createMock(MockCreationSettings<T> settings, MockHandler handler) {
        
    }

    @Override
    public <T> Optional<T> createSpy(
            MockCreationSettings<T> settings, MockHandler handler, T instance) {
        
    }

    @Override
    public MockHandler getHandler(Object mock) {
        
    }

    @Override
    public void resetMock(Object mock, MockHandler newHandler, MockCreationSettings settings) {
        
    }

    @Override
    public TypeMockability isTypeMockable(Class<?> type) {
        
    }

    @Override
    public <T> StaticMockControl<T> createStaticMock(
            Class<T> type, MockCreationSettings<T> settings, MockHandler handler) {
        
    }

    @Override
    public <T> ConstructionMockControl<T> createConstructionMock(
            Class<T> type,
            Function<MockedConstruction.Context, MockCreationSettings<T>> settingsFactory,
            Function<MockedConstruction.Context, MockHandler<T>> handlerFactory,
            MockedConstruction.MockInitializer<T> mockInitializer) {
        
    }

    @Override
    public void clearAllCaches() {
        
    }
}
