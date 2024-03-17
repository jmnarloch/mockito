/*
 * Copyright (c) 2021 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import org.mockito.exceptions.base.MockitoException;

import java.lang.reflect.Method;

class TypeSupport {

    static final TypeSupport INSTANCE;

    static {
        Method isSealed;
        try {
            isSealed = Class.class.getMethod("isSealed");
        } catch (NoSuchMethodException ignored) {
            isSealed = null;
        }
        INSTANCE = new TypeSupport(isSealed);
    }

    private final Method isSealed;

    private TypeSupport(Method isSealed) {
        
    }

    boolean isSealed(Class<?> type) {
        
    }
}
