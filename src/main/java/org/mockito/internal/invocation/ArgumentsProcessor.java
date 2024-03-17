/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.Equals;

/** by Szczepan Faber, created at: 3/31/12 */
public final class ArgumentsProcessor {
    // drops hidden synthetic parameters (last continuation parameter from Kotlin suspending
    // functions)
    // and expands varargs
    public static Object[] expandArgs(MockitoMethod method, Object[] args) {
        
    }

    // expands array varArgs that are given by runtime (1, [a, b]) into true
    // varArgs (1, a, b);
    private static Object[] expandVarArgs(final boolean isVarArgs, final Object[] args) {
        
    }

    private static <T> boolean isNullOrEmpty(T[] array) {
        
    }

    public static List<ArgumentMatcher> argumentsToMatchers(Object[] arguments) {
        
    }

    private ArgumentsProcessor() { }
}
