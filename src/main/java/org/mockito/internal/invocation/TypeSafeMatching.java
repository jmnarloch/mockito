/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import org.mockito.ArgumentMatcher;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@SuppressWarnings({"unchecked", "rawtypes"})
public class TypeSafeMatching implements ArgumentMatcherAction {

    private static final ArgumentMatcherAction TYPE_SAFE_MATCHING_ACTION = new TypeSafeMatching();

    /**
     * This cache is in theory unbounded. However, its max size is bounded by the number of types of argument matchers
     * that are both in the system and being used, which is expected to bound the cache's size to a low number
     * (<200) in all but the most contrived cases, and form a small percentage of the overall memory usage of those
     * classes.
     */
    private static final ConcurrentMap<Class<?>, Class<?>> argumentTypeCache =
            new ConcurrentHashMap<>();

    private TypeSafeMatching() { }

    public static ArgumentMatcherAction matchesTypeSafe() {
        
    }

    @Override
    public boolean apply(ArgumentMatcher matcher, Object argument) {
        
    }

    /**
     * Returns <code>true</code> if the given <b>argument</b> can be passed to
     * the given <code>argumentMatcher</code> without causing a
     * {@link ClassCastException}.
     */
    private static boolean isCompatible(ArgumentMatcher<?> argumentMatcher, Object argument) {
        
    }

    private static Class<?> getArgumentType(ArgumentMatcher<?> matcher) {
        
    }

    /**
     * Returns the type of {@link ArgumentMatcher#matches(Object)} of the given
     * {@link ArgumentMatcher} implementation.
     */
    private static Class<?> getArgumentTypeUncached(ArgumentMatcher<?> argumentMatcher) {
        
    }

    /**
     * Returns <code>true</code> if the given method is
     * {@link ArgumentMatcher#matches(Object)}
     */
    private static boolean isMatchesMethod(Method method) {
        
    }
}
