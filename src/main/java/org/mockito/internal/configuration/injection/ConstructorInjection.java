/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.injection;

import static org.mockito.internal.exceptions.Reporter.fieldInitialisationThrewException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.FieldInitializer.ConstructorArgumentResolver;

/**
 * Injection strategy based on constructor.
 *
 * <p>
 * The strategy will search for the constructor with most parameters
 * and try to resolve mocks by type.
 * </p>
 *
 * <blockquote>
 * TODO on missing mock type, shall it abandon or create "noname" mocks.
 * TODO and what if the arg type is not mockable.
 * </blockquote>
 *
 * <p>
 * For now the algorithm tries to create anonymous mocks if an argument type is missing.
 * If not possible the algorithm abandon resolution.
 * </p>
 */
public class ConstructorInjection extends MockInjectionStrategy {

    public ConstructorInjection() { }

    @Override
    public boolean processInjection(Field field, Object fieldOwner, Set<Object> mockCandidates) {
        
    }

    /**
     * Returns mocks that match the argument type, if not possible assigns null.
     */
    static class SimpleArgumentResolver implements ConstructorArgumentResolver {
        final Set<Object> objects;

        public SimpleArgumentResolver(Set<Object> objects) {
            
        }

        @Override
        public Object[] resolveTypeInstances(Class<?>... argTypes) {
            
        }

        private Object objectThatIsAssignableFrom(Class<?> argType) {
            
        }
    }
}
