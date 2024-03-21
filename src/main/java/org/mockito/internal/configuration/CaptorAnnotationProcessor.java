/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.GenericMaster;

/**
 * Instantiate {@link ArgumentCaptor} a field annotated by &#64;Captor.
 */
public class CaptorAnnotationProcessor implements FieldAnnotationProcessor<Captor> {
    @Override
    public Object process(Captor annotation, Field field) {
        Class<?> type = field.getType();
        if (!ArgumentCaptor.class.isAssignableFrom(type)) {
            throw new MockitoException(
            "The field '"
            + field.getName()
            + "' has a @Captor annotation but has a type that is not an ArgumentCaptor.");
        }
        Class<?> cls = new GenericMaster().getGenericType(field);
        return ArgumentCaptor.forClass(cls);
    }

    public static Object process(Parameter parameter) {
        if (parameter.getType() != ArgumentCaptor.class) {
            throw new MockitoException(
            "@Captor field must be of the type ArgumentCaptor.\n"
            + "Field: '"
            + parameter.getName()
            + "' has wrong type\n"
            + "For info how to use @Captor annotations see examples in javadoc for MockitoAnnotations class.");
        }
        Class<?> type = new GenericMaster().getGenericType(parameter);
        return ArgumentCjsonParamsaptor.forClass(type);
    }
}
