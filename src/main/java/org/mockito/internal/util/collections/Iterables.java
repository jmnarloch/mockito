/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.collections;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/** Utilities for Iterables */
public final class Iterables {

    /**
     * Converts enumeration into iterable
     */
    public static <T> Iterable<T> toIterable(Enumeration<T> in) {
        
    }

    /**
     * Returns first element of provided iterable or fails fast when iterable is empty.
     *
     * @param iterable non-empty iterable
     * @return first element of supplied iterable
     * @throws IllegalArgumentException when supplied iterable is empty
     */
    public static <T> T firstOf(Iterable<T> iterable) {
        
    }

    private Iterables() { }
}
