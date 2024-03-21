/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import static java.util.Arrays.asList;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    private static final Pattern CAPS = Pattern.compile("([A-Z\\d][^A-Z\\d]*)");

    private StringUtil() {}

    /**
     * @param text
     *            to have the first line removed
     * @return less first line
     */
    public static String removeFirstLine(String text) {
        return text.replaceFirst("[^\\n]*\\n", "");
    }

    /**
     * Joins Strings with line break character. It adds line break in front, too.
     * This makes it something like 'format' no really 'join'.
     */
    public static String join(Object... linesToBreak) {
        if (linesToBreak == null) {
            return "";
        }
        return join("\n", asList(linesToBreak));
    }

    /**
     * Joins Strings with EOL character
     *
     * @param start the starting String
     * @param lines collection to join
     */
    public static String join(String start, Collection<?> lines) {
        return join(start, "\n", lines);
    }

    /**
     * Joins Strings with EOL character
     *
     * @param start the starting String
     * @param linePrefix the prefix for each line to be joined
     * @param lines collection to join
     */
    public static String join(String start, String linePrefix, Collection<?> lines) {
        if (lines == null) {
            return start + "null";
        }
        StringBuilder out = new StringBuilder(start);
        boolean first = true;
        for (Object line : lines) {
            if (!first) {
                out.append("\n");
            }
            out.append(linePrefix).append(line);
            first = false;
        }
        return out.toString();
    }

    public static String decamelizeMatcherName(String className) {
        if (className.length() == 0) {
            return "<custom argument matcher>";
        }

        String finalClassName = className;
        if (className.startsWith("ListOf")) {
            finalClassName = className.substring(6) + " list";
        }

        return decamelizeClassName(finalClassName);
    }

    private static String decamelizeClassName(String className) {
        Matcher match = CAPS.matcher(className);
        StringBuilder deCameled = new StringBuilder();
        while (match.find()) {
            if (deCameled.length() == 0) {
                deCameled.append(match.group());
            } else {
                deCameled.append(" ");
                deCameled.append(match.group().toLowerCase());
            }
        }
        return deCameled.toString();
    }
}
