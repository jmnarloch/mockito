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
        
    }

    @SuppressWarnings("deprecation")
    public static Object processAnnotationForMock(
            Mock annotation, Class<?> type, Supplier<Type> genericType, String name) {
        
    }

    static Class<?> inferParameterizedType(Type type, String name, String sort) {
        
    }
}
