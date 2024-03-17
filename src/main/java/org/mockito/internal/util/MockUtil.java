/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.configuration.plugins.DefaultMockitoPlugins;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.MockName;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMaker.TypeMockability;
import org.mockito.plugins.MockResolver;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static org.mockito.internal.handler.MockHandlerFactory.createMockHandler;

@SuppressWarnings("unchecked")
public class MockUtil {

    private static final MockMaker defaultMockMaker = Plugins.getMockMaker();
    private static final Map<Class<? extends MockMaker>, MockMaker> mockMakers =
            new ConcurrentHashMap<>(
                    Collections.singletonMap(defaultMockMaker.getClass(), defaultMockMaker));

    private MockUtil() { }

    public static MockMaker getMockMaker(String mockMaker) {
        
    }

    public static TypeMockability typeMockabilityOf(Class<?> type, String mockMaker) {
        
    }

    public static <T> T createMock(MockCreationSettings<T> settings) {
        
    }

    public static void resetMock(Object mock) {
        
    }

    public static MockHandler<?> getMockHandler(Object mock) {
        
    }

    public static InvocationContainerImpl getInvocationContainer(Object mock) {
        
    }

    public static boolean isSpy(Object mock) {
        
    }

    public static boolean isMock(Object mock) {
        // TODO SF (perf tweak) in our codebase we call mockMaker.getHandler() multiple times
        // unnecessarily
        // This is not ideal because getHandler() can be expensive (reflective calls inside mock
        // maker)
        // The frequent pattern in the codebase are separate calls to: 1) isMock(mock) then 2)
        // getMockHandler(mock)
        // We could replace it with using mockingDetails().isMock()
        // Let's refactor the codebase and use new mockingDetails() in all relevant places.
        // Potentially we could also move other methods to MockitoMock, some other candidates:
        // getInvocationContainer, isSpy, etc.
        // This also allows us to reuse our public API MockingDetails
        
    }

    private static MockHandler<?> getMockHandlerOrNull(Object mock) {
        
    }

    private static Object resolve(Object mock) {
        
    }

    public static boolean areSameMocks(Object mockA, Object mockB) {
        
    }

    public static MockName getMockName(Object mock) {
        
    }

    public static void maybeRedefineMockName(Object mock, String newName) {
        
    }

    public static MockCreationSettings getMockSettings(Object mock) {
        
    }

    public static <T> MockMaker.StaticMockControl<T> createStaticMock(
            Class<T> type, MockCreationSettings<T> settings) {
        
    }

    public static <T> MockMaker.ConstructionMockControl<T> createConstructionMock(
            Class<T> type,
            Function<MockedConstruction.Context, MockCreationSettings<T>> settingsFactory,
            MockedConstruction.MockInitializer<T> mockInitializer) {
        
    }

    public static void clearAllCaches() {
        
    }
}
