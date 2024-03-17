/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers.text;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Map;

/**
 * Prints a Java object value in a way humans can read it neatly.
 * Inspired on hamcrest. Used for printing arguments in verification errors.
 */
public class ValuePrinter {

    private ValuePrinter() { }

    /**
     * Prints given value so that it is neatly readable by humans.
     * Handles explosive toString() implementations.
     */
    public static String print(final Object value) {
        
    }

    private static String printMap(Map<?, ?> map) {
        
    }

    /**
     * Print values in a nice format, e.g. (1, 2, 3)
     *
     * @param start the beginning of the values, e.g. "("
     * @param separator the separator of values, e.g. ", "
     * @param end the end of the values, e.g. ")"
     * @param values the values to print
     *
     * @return neatly formatted value list
     */
    public static String printValues(
            String start, String separator, String end, Iterator<?> values) {
        
    }

    private static String printChar(char value) {
        
    }

    private static String descriptionOf(Object value) {
        
    }
}
