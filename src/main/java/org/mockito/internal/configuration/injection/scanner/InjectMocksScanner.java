/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.injection.scanner;

import static org.mockito.internal.exceptions.Reporter.unsupportedCombinationOfAnnotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

/**
 * Scan field for injection.
 */
public class InjectMocksScanner {
    private final Class<?> clazz;

    /**
     * Create a new InjectMocksScanner for the given clazz on the given instance
     *
     * @param clazz    Current class in the hierarchy of the test
     */
    public InjectMocksScanner(Class<?> clazz) {
        
    }

    /**
     * Add the fields annotated by @{@link InjectMocks}
     *
     * @param mockDependentFields Set of fields annotated by  @{@link InjectMocks}
     */
    public void addTo(Set<Field> mockDependentFields) {
        
    }

    /**
     * Scan fields annotated by &#064;InjectMocks
     *
     * @return Fields that depends on Mock
     */
    @SuppressWarnings("unchecked")
    private Set<Field> scan() {
        
    }

    private static void assertNoAnnotations(
            Field field, Class<? extends Annotation>... annotations) {
        
    }
}
