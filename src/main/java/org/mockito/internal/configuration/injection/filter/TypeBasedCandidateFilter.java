/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.injection.filter;

import static org.mockito.internal.exceptions.Reporter.moreThanOneMockCandidate;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.mockito.internal.util.MockUtil;

public class TypeBasedCandidateFilter implements MockCandidateFilter {

    private final MockCandidateFilter next;

    public TypeBasedCandidateFilter(MockCandidateFilter next) {
        
    }

    protected boolean isCompatibleTypes(Type typeToMock, Type mockType, Field injectMocksField) {
        
    }

    private Stream<Type> getSuperTypes(Class<?> concreteMockClass) {
        
    }

    private boolean recurseOnTypeArguments(
            Field injectMocksField, Type[] actualTypeArguments, Type[] actualTypeArguments2) {
        
    }

    @Override
    public OngoingInjector filterCandidate(
            final Collection<Object> mocks,
            final Field candidateFieldToBeInjected,
            final List<Field> allRemainingCandidateFields,
            final Object injectee,
            final Field injectMocksField) {
        
    }
}
