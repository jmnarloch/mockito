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
        this.m1 = m1;
        this.m2 = m2;
    }

    @Override
    public boolean matches(Object actual) {
        return m1.matches(actual) || m2.matches(actual);
    }

    @Override
    public Class<?> type() {
        Class<?> type1 = m1.type();
        Class<?> type2 = m2.type();
        if (type1.isAssignableFrom(type2)) {
            return type1;
        } else if (type2.isAssignableFrom(type1)) {
            return type2;
        } else {
            // It is safe to return Object.class
            return Object.class;
        }
    }

    @Override
    public String toString() {
        return "or(" + m1 + ", " + m2 + ")";
    }
}
