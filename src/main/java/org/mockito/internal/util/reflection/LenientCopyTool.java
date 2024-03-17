/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.plugins.MemberAccessor;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class LenientCopyTool {

    MemberAccessor accessor = Plugins.getMemberAccessor();

    public <T> void copyToMock(T from, T mock) {
        
    }

    public <T> void copyToRealObject(T from, T to) {
        
    }

    private <T> void copy(T from, T to, Class<?> fromClazz) {
        
    }

    private <T> void copyValues(T from, T mock, Class<?> classFrom) {
        
    }
}
