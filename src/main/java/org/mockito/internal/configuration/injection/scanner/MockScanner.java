/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.injection.scanner;

import static org.mockito.internal.util.collections.Sets.newMockSafeHashSet;

import java.lang.reflect.Field;
import java.util.Set;

import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.FieldReader;

/**
 * Scan mocks, and prepare them if needed.
 */
public class MockScanner {
    private final Object instance;
    private final Class<?> clazz;

    /**
     * Creates a MockScanner.
     *
     * @param instance The test instance
     * @param clazz    The class in the type hierarchy of this instance.
     */
    public MockScanner(Object instance, Class<?> clazz) {
        
    }

    /**
     * Add the scanned and prepared mock instance to the given collection.
     *
     * <p>
     * The preparation of mocks consists only in defining a MockName if not already set.
     * </p>
     *
     * @param mocks Set of mocks
     */
    public void addPreparedMocks(Set<Object> mocks) {
        
    }

    /**
     * Scan and prepare mocks for the given <code>testClassInstance</code> and <code>clazz</code> in the type hierarchy.
     *
     * @return A prepared set of mock
     */
    private Set<Object> scan() {
        
    }

    private Object preparedMock(Object instance, Field field) {
        
    }

    private boolean isAnnotatedByMockOrSpy(Field field) {
        
    }

    private boolean isMockOrSpy(Object instance) {
        
    }
}
