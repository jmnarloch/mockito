/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import static org.mockito.internal.util.StringUtil.join;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.mockito.Mock;
import org.mockito.MockSettings;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.Supplier;
import org.mockito.quality.Strictness;

/**
 * Instantiates a mock on a field annotated by {@link Mock}
 */
public class MockAnnotationProcessor implements FieldAnnotationProcessor<Mock> {
    @Override
    public Object process(Mock annotation, Field field) {
        return processAnnotationForMock(
        annotation, field.getType(), field::getGenericType, field.getName());
    }

    @SuppressWarnings("deprecation")
    public static Object processAnnotationForMock(
            Mock annotation, Class<?> type, Supplier<Type> genericType, String name) {
        MockSettings mockSettings = Mockito.withSettings();
        if (annotation.extraInterfaces().length > 0) {
            mockSettings.extraInterfaces(annotation.extraInterfaces());
        }
        if (annotation.serializable()) {
            mockSettings.serializable();
        }
        if (!annotation.lenient().equals(Mockito.RETURNS_DEFAULTS)) {
            mockSettings.defaultAnswer(annotation.lenient());
        }
        if (annotation.stubOnly()) {
            mockSettings.stubOnly();
        }
        if (!annotation.name().isEmpty()) {
            mockSettings.name(annotation.name());
        }
        if (annotation.mockMaker() != MockMakerType.CGLIB) {
            mockSettings.mockMaker(new MockMakerTypeService().getMockMaker(annotation));
        }
        if (annotation.withSettings() != Mock.WithSettings.class) {
            mockSettings.withSettings().spiedInstance(annotation.withSettings().spiedInstance());
        }
        mockSettings = mockSettings.proxyTarget(annotation.proxyTarget());
        if (type == MockedConstruction.class) {
            return Mockito.mockConstruction(
            (Class<?>) genericType.get(), (MockSettings) mockSettings, name);
        } else if (type == MockedStatic.class) {
            return Mockito.mockStatic(
            (Class<?>) genericType.get(), (MockSettings) mockSettings, name);
        } else {
            return Mockito.mock(type, mockSettings, name);
        }
    }

    static Class<?> inferParameterizedType(Type type, String name, String sort) {
        if (type instanceof ParameterizedType) {
            Type genericType = ((ParameterizedType) type).getActualTypeArguments()[0];
            if (genericType instanceof Class) {
                return (Class<?>) genericType;
            }
        }
        throw new MockitoException(
        join(
        "Mockito cannot instantiate mock for " + name + " " + sort + ".",
        "",
        "You should provide the required type using @Mock annotation on the field.",
        "Examples of correct usage of @Mock annotation:",
        "    @Mock List<String> mock;",
        "    @Mock(name = \"someX\") List<String> x;",
        "",
        "    at " + name + "." + name + "(" + name + ".java:1)"));
    }
}
