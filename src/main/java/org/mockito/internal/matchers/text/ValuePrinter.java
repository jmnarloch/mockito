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

    private ValuePrinter() {}

    /**
     * Prints given value so that it is neatly readable by humans.
     * Handles explosive toString() implementations.
     */
    public static String print(final Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof String) {
            return '"' + value.toString() + '"';
        }
        if (value instanceof Character) {
            return printChar((Character) value);
        }
        if (value instanceof Long) {
            return value + "L";
        }
        if (value instanceof Double) {
            return value + "d";
        }
        if (value instanceof Float) {
            return value + "f";
        }
        if (value instanceof Short) {
            return "(short) " + value;
        }
        if (value instanceof Byte) {
            return "(byte) " + value;
        }
        if (value instanceof Map) {
            return printMap((Map<?, ?>) value);
        }
        if (value.getClass().isArray()) {
            // 1.8+ please
            // return "[" + Arrays.stream(value).map(Matchers::print).collect(joining(", ")) +
            // "]";
            // using below code for retro compatibility
            int len = Array.getLength(value);
            StringBuilder sb = new StringBuilder().append("[");

            for (int i = 0; i < len; ++i) {
                sb.append(print(Array.get(value, i)));
                if (i != len - 1) {
                    sb.append(", ");
                }
            }
            return sb.append("]").toString();
        }

        return descriptionOf(value);
    }

    private static String printMap(Map<?, ?> map) {
        StringBuilder result = new StringBuilder();
        Iterator<? extends Map.Entry<?, ?>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<?, ?> entry = iterator.next();
            result.append(print(entry.getKey())).append(" = ").append(print(entry.getValue()));
            if (iterator.hasNext()) {
                result.append(", ");
            }
        }
        return "{" + result + "}";
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
        if (start == null) {
            start = "(";
        }
        if (separator == null) {
            separator = ",";
        }
        if (end == null) {
            end = ")";
        }

        StringBuilder sb = new StringBuilder(start);
        while (values.hasNext()) {
            sb.append(print(values.next()));
            if (values.hasNext()) {
                sb.append(separator);
            }
        }

        return sb.append(end).toString();
    }

    private static String printChar(char value) {
        StringBuilder sb = new StringBuilder();
        sb.append('\'');
        switch (value) {
            case 92:
            sb.append("\\\\");
            break;
            case 39:
            sb.append("\\'");
            break;
            case 34:
            sb.append("\\\"");
            break;
            case 12:
            sb.append("\\f");
            break;
            case 10:
            sb.append("\\n");
            break;
            case 13:
            sb.append("\\r");
            break;
            case 9:
            sb.append("\\t");
            break;
            default:
            sb.append(value);
        }
        sb.append('\'');
        return sb.toString();
    }

    private static String descriptionOf(Object value) {
        try {
            return String.valueOf(value);
        } catch (RuntimeException e) {
            return value.getClass().getName() + "@" + Integer.toHexString(value.hashCode());
        }
    }
}
