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

    private MockUtil() {}

    public static MockMaker getMockMaker(String mockMaker) {
        if (mockMaker == null) {
            return defaultMockMaker;
        }
        if (DefaultMockitoPlugins.MOCK_MAKER_FOR_SPY.equals(mockMaker)) {
            // All spies must use the same mock maker
            return defaultMockMaker;
        }
        try {
            return mockMakers.computeIfAbsent(
            Plugins.getMockMaker(mockMaker).getClass(),
            ignored -> Plugins.getMockMaker(mockMaker));
        } catch (Exception se) {
            throw new IllegalStateException("Failed to load mock maker: " + mockMaker, se);
        }
    }

    public static TypeMockability typeMockabilityOf(Class<?> type, String mockMaker) {
        return mockMakers.get(mockMaker).isTypeMockable(type);
    }

    public static <T> T createMock(MockCreationSettings<T> settings) {
        MockMaker mockMaker = mockMakers.get(settings.getMockMaker());
        // We are suppressing the warning related to the unsafe cast, as the validity of the cast
        // is ensured by the MockMaker implementation.
        return (T) mockMaker.createMock(settings);
    }

    public static void resetMock(Object mock) {
        MockHandler oldHandler = getMockHandler(mock);
        MockCreationSettings settings = oldHandler.getMockSettings();
        MockHandler newHandler = createMockHandler(settings);

        mock = resolve(mock);
        getMockMaker(settings.getMockMaker()).resetMock(mock, newHandler, settings);
    }

    public static MockHandler<?> getMockHandler(Object mock) {
        MockHandler<?> handler = getMockHandlerOrNull(mock);
        if (handler == null) {
            throw new NotAMockException("Argument should be a mock, but is: " + mock.getClass());
        }
        return handler;
    }

    public static InvocationContainerImpl getInvocationContainer(Object mock) {
        return (InvocationContainerImpl) getMockHandler(mock).getInvocationContainer();
    }

    public static boolean isSpy(Object mock) {
        return isMock(mock)
        && (getMockSettings(mock).isUsingDefaultAnswer()
        || getMockHandler(mock).isAnswerMarshalingController());
    }

    public static boolean isMock(Object mock) {
        return mock != null
        && getMockHandlerOrNull(mock) != null
        && !isSpy(mock); // to avoid circularity, see #1103 for details
    }

    private static MockHandler<?> getMockHandlerOrNull(Object mock) {
        if (mock == null) {
            throw new NotAMockException("Argument should be a mock, but is null!");
        }

        if (mock instanceof Class<?>) {
            throw new NotAMockException("Argument should be a mock, but is '" + mock + "'");
        }

        return defaultMockMaker.getHandler(resolve(mock));
    }

    private static Object resolve(Object mock) {
        if (mock instanceof Class<?>) {
            return mock;
        }
        return mock;
    }

    public static boolean areSameMocks(Object mockA, Object mockB) {
        return mockA == mockB || resolve(mockA) == resolve(mockB);
    }

    public static MockName getMockName(Object mock) {
        return getMockHandler(mock).getMockSettings().getMockName();
    }

    public static void maybeRedefineMockName(Object mock, String newName) {
        MockName mockName = getMockName(mock);
        mockName.maybeRedefine(newName);
    }

    public static MockCreationSettings getMockSettings(Object mock) {
        return getMockHandler(mock).getMockSettings();
    }

    public static <T> MockMaker.StaticMockControl<T> createStaticMock(
            Class<T> type, MockCreationSettings<T> settings) {
        MockMaker.StaticMockControl<T> control = defaultMockMaker.createStaticMock(type, settings);
        control.enable();
        return control;
    }

    public static <T> MockMaker.ConstructionMockControl<T> createConstructionMock(
            Class<T> type,
            Function<MockedConstruction.Context, MockCreationSettings<T>> settingsFactory,
            MockedConstruction.MockInitializer<T> mockInitializer) {
        Function<MockedConstruction.Context, MockHandler<T>> handlerFactory =
        context -> createMockHandler(settingsFactory.apply(context));
        return defaultMockMaker.createConstructionMock(
        type, settingsFactory, handlerFactory, mockInitializer);
    }

    public static void clearAllCaches() {
        for (MockMaker mockMaker : mockMakers.values()) {
            mockMaker.clearAllCaches();
        }
    }
}
