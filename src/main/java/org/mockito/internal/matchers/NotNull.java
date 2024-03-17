/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import java.io.Serializable;
import java.util.Objects;

import org.mockito.ArgumentMatcher;

public class NotNull<T> implements ArgumentMatcher<T>, Serializable {

    public static final NotNull<Object> NOT_NULL = new NotNull<>(Object.class);

    private final Class<T> type;

    public NotNull(Class<T> type) {
        
    }

    @Override
    public boolean matches(Object actual) {
        
    }

    @Override
    public Class<T> type() {
        
    }

    @Override
    public String toString() { }
}
