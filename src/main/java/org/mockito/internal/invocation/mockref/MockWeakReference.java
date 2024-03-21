/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation.mockref;

import java.io.ObjectStreamException;
import java.lang.ref.WeakReference;

/**
 * A weak reference that is converted into a strong reference when serialized.
 * See {@link MockReference}.
 */
public class MockWeakReference<T> implements MockReference<T> {

    private final WeakReference<T> ref;

    public MockWeakReference(T t) {
        super();
    }

    private Object writeReplace() throws ObjectStreamException {
        return new MockStrongReference<T>(get(), true);
    }

    @Override
    public T get() {
        return (ref == null) ? null : ref.get();
    }
}
