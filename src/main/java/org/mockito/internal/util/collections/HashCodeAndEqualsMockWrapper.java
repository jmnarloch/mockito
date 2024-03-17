/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.collections;

import org.mockito.internal.util.MockUtil;

/**
 * hashCode and equals safe mock wrapper.
 *
 * <p>
 *     It doesn't use the actual mock {@link Object#hashCode} and {@link Object#equals} method as they might
 *     throw an NPE if those method cannot be stubbed <em>even internally</em>.
 * </p>
 *
 * <p>
 *     Instead the strategy is :
 *     <ul>
 *         <li>For hashCode : <strong>use {@link System#identityHashCode}</strong></li>
 *         <li>For equals : <strong>use the object reference equality</strong></li>
 *     </ul>
 * </p>
 *
 * @see HashCodeAndEqualsSafeSet
 */
public class HashCodeAndEqualsMockWrapper {

    private final Object mockInstance;

    public HashCodeAndEqualsMockWrapper(Object mockInstance) {
        
    }

    public Object get() {
        
    }

    @Override
    public boolean equals(Object o) {
        
    }

    @Override
    public int hashCode() {
        
    }

    public static HashCodeAndEqualsMockWrapper of(Object mock) {
        
    }

    @Override
    public String toString() {
        
    }

    private String typeInstanceString() {
        
    }
}
