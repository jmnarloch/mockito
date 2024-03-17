/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import static org.mockito.internal.util.StringUtil.join;

import java.lang.reflect.Modifier;

import org.mockito.creation.instance.Instantiator;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.util.Platform;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;

/**
 * Subclass based mock maker.
 *
 * This mock maker tries to create a subclass to represent a mock. It uses the given mock settings, that contains
 * the type to mock, extra interfaces, and serialization support.
 *
 * <p>
 * The type to mock has to be not final and not part of the JDK. The created mock will implement extra interfaces
 * if any. And will implement <code>Serializable</code> if this settings is explicitly set.
 */
public class SubclassByteBuddyMockMaker implements ClassCreatingMockMaker {

    private final BytecodeGenerator cachingMockBytecodeGenerator;

    public SubclassByteBuddyMockMaker() {
        
    }

    public SubclassByteBuddyMockMaker(SubclassLoader loader) {
        
    }

    @Override
    public <T> T createMock(MockCreationSettings<T> settings, MockHandler handler) {
        
    }

    @Override
    public <T> Class<? extends T> createMockType(MockCreationSettings<T> settings) {
        
    }

    private static <T> T ensureMockIsAssignableToMockedType(
            MockCreationSettings<T> settings, T mock) {
        // Force explicit cast to mocked type here, instead of
        // relying on the JVM to implicitly cast on the client call site.
        // This allows us to catch earlier the ClassCastException earlier
        
    }

    private <T> RuntimeException prettifyFailure(
            MockCreationSettings<T> mockFeatures, Exception generationFailed) {
        
    }

    private static String describeClass(Class<?> type) {
        
    }

    private static String describeClass(Object instance) {
        
    }

    @Override
    public MockHandler getHandler(Object mock) {
        
    }

    @Override
    public void resetMock(Object mock, MockHandler newHandler, MockCreationSettings settings) {
        
    }

    @Override
    public TypeMockability isTypeMockable(final Class<?> type) {
        
    }

    @Override
    public void clearAllCaches() {
        
    }
}
