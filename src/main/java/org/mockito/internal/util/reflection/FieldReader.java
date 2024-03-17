/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import java.lang.reflect.Field;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.plugins.MemberAccessor;

public class FieldReader {

    final Object target;
    final Field field;
    final MemberAccessor accessor = Plugins.getMemberAccessor();

    public FieldReader(Object target, Field field) {
        
    }

    public boolean isNull() {
        
    }

    public Object read() {
        
    }
}
