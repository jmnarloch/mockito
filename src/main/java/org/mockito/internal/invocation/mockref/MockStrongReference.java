/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation.mockref;

import java.io.ObjectStreamException;

public class MockStrongReference<T> implements MockReference<T> {

    private final T ref;
    private final boolean deserializeAsWeakRef;

    public MockStrongReference(T ref, boolean deserializeAsWeakRef) {
        
    }

    @Override
    public T get() {
        
    }

    private Object readResolve() throws ObjectStreamException {
        
    }
}
