/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import java.io.Serializable;

import org.mockito.ArgumentMatcher;

public abstract class CompareTo<T extends Comparable<T>>
        implements ArgumentMatcher<T>, Serializable {
    private final T wanted;

    public CompareTo(T value) {
        
    }

    @Override
    public final boolean matches(T actual) {
        
    }

    @Override
    public final String toString() {
        
    }

    protected abstract String getName();

    protected abstract boolean matchResult(int result);
}
