/*
 * Copyright (c) 2023 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import java.util.function.Consumer;

@SuppressWarnings("FunctionalInterfaceMethodChanged")
@FunctionalInterface
public interface ThrowingConsumer<T> extends Consumer<T> {
    @Override
    default void accept(final T input) {}

    void acceptThrows(T input) throws Throwable;
}
