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
        int regularParameterCount = method.getParameterTypes().length;
        if (method.isVarArgs()) {
            regularParameterCount--;
        }

        if (args.length > regularParameterCount) {
            args = Arrays.copyOf(args, method.getParameterTypes().length);
            args[args.length - 1] =
            Arrays.copyOf(
            (Object[]) args[args.length - 1],
            args.length - regularParameterCount + 1);
        }
        return args;
    }

    // expands array varArgs that are given by runtime (1, [a, b]) into true
    // varArgs (1, a, b);
    private static Object[] expandVarArgs(final boolean isVarArgs, final Object[] args) {
        if (!isVarArgs
        || isNullOrEmpty(args)
        || (args[args.length - 1] != null && !args[args.length -1].getClass().isArray())) {
            return args == null ? new Object[0] : args;
        }

        final int nonVarArgsCount = args.length - 1;
        Object[] varArgs;
        if (args[nonVarArgsCount] == null) {
            // in case someone deliberately passed null varArg array
            varArgs = new Object[] {null};
        } else {
            varArgs = ArrayEquals.createObjectArray(args[nonVarArgsCount]);
        }
        final Object[] newArgs = new Object[nonVarArgsCount + varArgs.length];
        System.arraycopy(args, 0, newArgs, 0, nonVarArgsCount);
        System.arraycopy(varArgs, 0, newArgs, nonVarArgsCount, varArgs.length);
        return newArgs;
    }

    private static <T> boolean isNullOrEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    public static List<ArgumentMatcher> argumentsToMatchers(Object[] arguments) {
        List<ArgumentMatcher> matchers = new ArrayList<>(arguments.length);
        for (Object arg : arguments) {
            if (arg != null && arg.getClass().isArray()) {
                matchers.add(new ArrayEquals(arg));
            } else {
                matchers.add(Equals.create(arg));
            }
        }
        return matchers;
    }

    private ArgumentsProcessor() {}
}
