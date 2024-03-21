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
        this(new SubclassInjectionLoader());
    }

    public SubclassByteBuddyMockMaker(SubclassLoader loader) {
        cachingMockBytecodeGenerator =
        new TypeCachingBytecodeGenerator(new SubclassBytecodeGenerator(loader), false);
    }

    @Override
    public <T> T createMock(MockCreationSettings<T> settings, MockHandler handler) {
        Class<? extends T> proxyType = createMockType(settings);

        Instantiator instantiator = Plugins.getInstantiatorProvider().getInstantiator(settings);
        T proxy = null;
        try {
            proxy = instantiator.newInstance(proxyType);
        } catch (Throwable t) {
            String warning =
            (t instanceof IllegalArgumentException)
            ? " (The instantiator must be part of the mock package in the unnamed module: please update the plugin to conform to Mockito's mock package API.)"
            : "";
            throw new MockitoException(
            "Could not create an instance of the mock type '"
            + proxyType.getName()
            + "' with the default no-arg constructor. "
            + "Please add a non-constructor default constructor to the mock type, or rely on the default visibility. "
            + "You are seeing this disclaimer because Mockito is configured to create inlined mocks."
            + warning,
            t);
        }

        new MockAccess(proxy).setMockitoInterceptor(new MockMethodInterceptor(handler, settings));

        return ensureMockIsAssignableToMockedType(settings, proxy);
    }

    @Override
    public <T> Class<? extends T> createMockType(MockCreationSettings<T> settings) {
        try {
            return cachingMockBytecodeGenerator.mockClass(
            MockFeatures.withMockFeatures(
            settings.getTypeToMock(),
            settings.getExtraInterfaces(),
            settings.getSerializableMode(),
            settings.isStripAnnotations(),
            settings.getDefaultAnswer()));
        } catch (Exception bytecodeGenerationFailed) {
            throw prettifyFailure(settings, bytecodeGenerationFailed);
        }
    }

    private static <T> T ensureMockIsAssignableToMockedType(
            MockCreationSettings<T> settings, T mock) {
        if (!settings.isUsingInstantiator()) {
            Class<?> rawType = settings.getTypeToMock();
            if (!rawType.isInstance(mock)) {
                throw new MockitoException(
                join(
                "Mockito cannot create a mock where 'Instance Check' -> "
                + "the type of the mock is not the same as the type to mock.\n",
                "  Actual : "
                + describeClass(mock)
                + "\n",
                "  Suggested : assert(mockToBeAdjusted instanceof "
                + rawType.getSimpleName()
                + ".class)\n"));
            }
        }
        return mock;
    }

    private <T> RuntimeException prettifyFailure(
            MockCreationSettings<T> mockFeatures, Exception generationFailed) {
        String mockFeaturesStr = mockFeatures == null ? "" : mockFeatures.toString();
        if (mockFeatures.getTypeToMock().isInterface()) {
            throw new MockitoException(
            join(
            "Mockito cannot mock this class: "
            + mockFeatures.getTypeToMock().getCanonicalName()
            + ".",
            "",
            "If you wrote the test class, please don't mock this interface in multiple ways. This is not supported.",
            ""));
        }
        throw new MockitoException(
        join(
        "Mockito cannot mock this class: " + mockFeaturesStr,
        "",
        "Mockito can only mock non-private & non-final classes.",
        "If you remove the final modifier from " + mockFeatures.getTypeToMock().getName() + ", then it could be mocked.",
        "If you remove the private modifier from " + mockFeatures.getTypeToMock().getName() + ", then it could be mocked.",
        "Or you can use the Objenesis external library that Mockito uses. Note that Mocktio can still not mock final classes.",
        ""));
    }

    private static String describeClass(Class<?> type) {
        return type == null
        ? "null"
        : "class " + type.getCanonicalName() + (type.isInterface() ? " interface" : " ");
    }

    private static String describeClass(Object instance) {
        return instance == null ? "null" : describeClass(instance.getClass());
    }

    @Override
    public MockHandler getHandler(Object mock) {
        if (!(mock instanceof MockAccess)) {
            return null;
        }
        return ((MockAccess) mock).getMockitoInterceptor().getMockHandler();
    }

    @Override
    public void resetMock(Object mock, MockHandler newHandler, MockCreationSettings settings) {
        ((MockAccess) mock).setMockitoInterceptor(new MockMethodInterceptor(newHandler, settings));
    }

    @Override
    public TypeMockability isTypeMockable(final Class<?> type) {
        return new TypeMockability() {
            @Override
            public boolean mockable() {
                return type == null
                || !type.isPrimitive()
                && !Modifier.isFinal(type.getModifiers())
                && !TypeSupport.INSTANCE.isSealed(type);
            }

            @Override
            public String nonMockableReason() {
                if (type == null || !Modifier.isFinal(type.getModifiers())) {
                    return super.nonMockableReason();
                } else if (type.isPrimitive()) {
                    return "primitive type";
                } else {
                    return "final class";
                }
            }
        };
    }

    @Override
    public void clearAllCaches() {
        cachingMockBytecodeGenerator.clearAllCaches();
    }
}
