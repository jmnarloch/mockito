/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import org.mockito.MockedConstruction;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;

import java.util.Optional;
import java.util.function.Function;

/**
 * ByteBuddy MockMaker.
 *
 * This class will serve as the programmatic entry point to all mockito internal MockMakers.
 * Currently the default mock maker is the inlining engine.
 *
 * The programmatic API could look like {@code mock(Final.class, withSettings().finalClasses())}.
 */
public class ByteBuddyMockMaker implements ClassCreatingMockMaker {
    private final SubclassByteBuddyMockMaker subclassByteBuddyMockMaker;

    public ByteBuddyMockMaker() {
        
    }

    ByteBuddyMockMaker(SubclassByteBuddyMockMaker subclassByteBuddyMockMaker) {
        
    }

    @Override
    public <T> T createMock(MockCreationSettings<T> settings, MockHandler handler) {
        
    }

    @Override
    public <T> Optional<T> createSpy(
            MockCreationSettings<T> settings, MockHandler handler, T object) {
        
    }

    @Override
    public <T> Class<? extends T> createMockType(MockCreationSettings<T> creationSettings) {
        
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
