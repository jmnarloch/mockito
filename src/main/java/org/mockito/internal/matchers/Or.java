/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import java.io.Serializable;

import org.mockito.ArgumentMatcher;

@SuppressWarnings({"unchecked", "serial", "rawtypes"})
public class Or implements ArgumentMatcher<Object>, Serializable {
    private final ArgumentMatcher m1;
    private final ArgumentMatcher m2;

    public Or(ArgumentMatcher<?> m1, ArgumentMatcher<?> m2) {
        
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
