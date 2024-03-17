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
        
    }

    public static Object process(Parameter parameter) {
        
    }
}
