/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import java.io.Serializable;

import java.util.Objects;
import org.mockito.ArgumentMatcher;

public class EqualsWithDelta implements ArgumentMatcher<Number>, Serializable {

    private final Number wanted;
    private final Number delta;

    public EqualsWithDelta(Number value, Number delta) {
        
    }

    @Override
    public boolean matches(Number actual) {
        
    }

    @Override
    public String toString() {
        
    }
}
