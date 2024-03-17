/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import java.io.Serializable;

import org.mockito.ArgumentMatcher;
import org.mockito.internal.util.Primitives;

public class InstanceOf implements ArgumentMatcher<Object>, Serializable {

    final Class<?> clazz;
    private final String description;

    public InstanceOf(Class<?> clazz) {
        
    }

    public InstanceOf(Class<?> clazz, String describedAs) {
        
    }

    @Override
    public boolean matches(Object actual) {
        
    }

    @Override
    public Class<?> type() {
        
    }

    @Override
    public String toString() {
        
    }
}
