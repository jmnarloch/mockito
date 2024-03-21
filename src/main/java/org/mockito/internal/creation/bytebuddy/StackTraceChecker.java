/*
 * Copyright (c) 2022 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import java.util.function.Predicate;

class StackTraceChecker implements Predicate<Class<?>> {

    @Override
    public boolean test(Class<?> type) {
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        for (int index = 1; index < stackTrace.length - 1; index++) {
            if (!stackTrace[index].getClassName().startsWith(Dispatcher.class.getName())
            && !stackTrace[index + 1].getClassName().startsWith(
            TypeCachingBytecodeGenerator.class.getPackage().getName())) {
                return false;
            }
        }
        return true;
    }
}
