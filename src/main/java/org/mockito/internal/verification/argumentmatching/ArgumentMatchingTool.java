/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification.argumentmatching;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.ContainsExtraTypeInfo;

@SuppressWarnings("rawtypes")
public class ArgumentMatchingTool {

    private ArgumentMatchingTool() { }

    /**
     * Suspiciously not matching arguments are those that don't match, the toString() representation is the same but types are different.
     */
    public static Integer[] getSuspiciouslyNotMatchingArgsIndexes(
            List<ArgumentMatcher> matchers, Object[] arguments) {
        
    }

    /**
     * Returns indexes of arguments not matching the provided matchers.
     */
    public static List<Integer> getNotMatchingArgsIndexes(
            List<ArgumentMatcher> matchers, Object[] arguments) {
        
    }

    private static boolean safelyMatches(ArgumentMatcher m, Object arg) {
        
    }

    private static boolean toStringEquals(ArgumentMatcher m, Object arg) {
        
    }

    /**
     * Suspiciously not matching arguments are those that don't match, and the classes have same simple name.
     */
    public static Set<String> getNotMatchingArgsWithSameName(
            List<ArgumentMatcher> matchers, Object[] arguments) {
        
    }
}
