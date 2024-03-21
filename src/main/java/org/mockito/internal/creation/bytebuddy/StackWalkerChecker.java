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
        Class<?> stackWalker = Class.forName("java.lang.StackWalker");
        @SuppressWarnings({"unchecked", "rawtypes"})
        Class<Enum> stackWalkerOption = (Class<Enum<?>>) Class.forName("java.lang.StackWalker$Option");
        stackWalkerGetInstance = stackWalker.getMethod("getInstance", stackWalkerOption);
        stackWalkerWalk = stackWalker.getMethod("walk", Function.class);
        Class<?> stackWalkerStackFrame = Class.forName("java.lang.StackWalker$StackFrame");
        stackWalkerStackFrameGetDeclaringClass =
        stackWalkerStackFrame.getMethod("getDeclaringClass");
        @SuppressWarnings("unchecked")
        Enum<?> stackWalkerOptionRetainClassReference =
        Enum.class.cast(
        Class.forName("java.lang.StackWalker$Option")
        .getEnumConstants()[0]); // RETAIN_CLASS_REFERENCE
        this.stackWalkerOptionRetainClassReference = stackWalkerOptionRetainClassReference;
    }

    static Predicate<Class<?>> orFallback() {
        try {
            return new StackWalkerChecker();
        } catch (Exception ignored) {
            return new StackWalkerFallback();
        }
    }

    @Override
    public boolean test(Class<?> type) {
        Object walker;
        try {
            walker = stackWalkerGetInstance.invoke(null);
        } catch (Throwable ignored) {
            return false;
        }
        return (Boolean)
        stackWalkerWalk.invoke(
        walker,
        (Function<?, ?>)
        stream -> {
            Iterator<?> iterator = ((Stream<?>) stream).iterator();
            Object caller;
            do {
                StackWalker.StackFrame frame =
                (StackWalker.StackFrame) iterator.next();
                caller =
                stackWalkerStackFrameGetDeclaringClass.invoke(frame);
            } while (iterator.hasNext() && !caller.isAssignableFrom(type));
            return caller;
        });
    }
}
