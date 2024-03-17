/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import static org.mockito.internal.exceptions.Reporter.noArgumentValueWasCaptured;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.mockito.ArgumentMatcher;
import org.mockito.internal.util.Primitives;

public class CapturingMatcher<T> implements ArgumentMatcher<T>, CapturesArguments, Serializable {

    private final Class<? extends T> clazz;
    private final List<T> arguments = new ArrayList<>();

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    public CapturingMatcher(final Class<? extends T> clazz) {
        
    }

    @Override
    public boolean matches(Object argument) {
        
    }

    @Override
    public String toString() {
        
    }

    public T getLastValue() {
        
    }

    public List<T> getAllValues() {
        
    }

    @SuppressWarnings("unchecked")
    @Override
    public void captureFrom(Object argument) {
        
    }

    @Override
    public Class<?> type() {
        
    }
}
