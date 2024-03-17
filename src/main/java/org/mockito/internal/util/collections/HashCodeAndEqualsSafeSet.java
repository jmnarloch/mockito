/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.collections;

import static java.lang.reflect.Array.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.mockito.internal.util.Checks;

/**
 * hashCode and equals safe hash based set.
 *
 * <p>
 *     Useful for holding mocks that have un-stubbable hashCode or equals method,
 *     meaning that in this scenario the real code is always called and will most probably
 *     cause an {@link NullPointerException}.
 * </p>
 * <p>
 *     This collection wraps the mock in an augmented type {@link HashCodeAndEqualsMockWrapper}
 *     that have his own implementation.
 * </p>
 *
 * @see HashCodeAndEqualsMockWrapper
 */
public class HashCodeAndEqualsSafeSet implements Set<Object> {

    private final HashSet<HashCodeAndEqualsMockWrapper> backingHashSet = new HashSet<>();

    @Override
    public Iterator<Object> iterator() {
        
    }

    @Override
    public int size() {
        
    }

    @Override
    public boolean isEmpty() {
        
    }

    @Override
    public boolean contains(Object mock) {
        
    }

    @Override
    public boolean add(Object mock) {
        
    }

    @Override
    public boolean remove(Object mock) {
        
    }

    @Override
    public void clear() {
        
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        
    }

    @Override
    public boolean equals(Object o) {
        
    }

    @Override
    public int hashCode() {
        
    }

    public Object[] toArray() {
        
    }

    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] typedArray) {
        
    }

    @SuppressWarnings("unchecked")
    private <T> T[] unwrapTo(T[] array) {
        
    }

    public boolean removeAll(Collection<?> mocks) {
        
    }

    @Override
    public boolean containsAll(Collection<?> mocks) {
        
    }

    @Override
    public boolean addAll(Collection<?> mocks) {
        
    }

    @Override
    public boolean retainAll(Collection<?> mocks) {
        
    }

    private HashSet<HashCodeAndEqualsMockWrapper> asWrappedMocks(Collection<?> mocks) {
        
    }

    @Override
    public String toString() {
        
    }

    public static HashCodeAndEqualsSafeSet of(Object... mocks) {
        
    }

    public static HashCodeAndEqualsSafeSet of(Iterable<Object> objects) {
        
    }
}
