/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import java.io.Serializable;

public class LessThan<T extends Comparable<T>> extends CompareTo<T> implements Serializable {

    public LessThan(T value) {
        
    }

    @Override
    protected String getName() { }

    @Override
    protected boolean matchResult(int result) {
        
    }
}
