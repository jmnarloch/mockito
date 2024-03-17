/*
 * Copyright (c) 2022 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

class StackWalkerChecker implements Predicate<Class<?>> {

    private final Method stackWalkerGetInstance;
    private final Method stackWalkerWalk;
    private final Method stackWalkerStackFrameGetDeclaringClass;
    private final Enum<?> stackWalkerOptionRetainClassReference;

    StackWalkerChecker() throws Exception {
        
    }

    static Predicate<Class<?>> orFallback() {
        
    }

    @Override
    public boolean test(Class<?> type) {
        
    }
}
