/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericMaster {

    /**
     * Finds the generic type (parametrized type) of the field. If the field is not generic it returns Object.class.
     *
     * @param field the field to inspect
     */
    public Class<?> getGenericType(Field field) {
        
    }

    /**
     * Resolves the type (parametrized type) of the parameter. If the field is not generic it returns Object.class.
     *
     * @param parameter the parameter to inspect
     */
    public Class<?> getGenericType(Parameter parameter) {
        
    }

    private Class<?> getaClass(Type generic) {
        
    }
}
