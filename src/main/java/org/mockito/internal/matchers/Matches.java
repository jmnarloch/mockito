/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import java.io.Serializable;
import java.util.regex.Pattern;

import org.mockito.ArgumentMatcher;

public class Matches implements ArgumentMatcher<Object>, Serializable {

    private final Pattern pattern;

    public Matches(String regex) {
        
    }

    public Matches(Pattern pattern) {
        
    }

    @Override
    public boolean matches(Object actual) {
        
    }

    @Override
    public String toString() {
        
    }
}
