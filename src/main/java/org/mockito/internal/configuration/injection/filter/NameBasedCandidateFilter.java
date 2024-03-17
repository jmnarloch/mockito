/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.injection.filter;

import static org.mockito.internal.util.MockUtil.getMockName;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilter implements MockCandidateFilter {
    private final MockCandidateFilter next;

    public NameBasedCandidateFilter(MockCandidateFilter next) {
        
    }

    @Override
    public OngoingInjector filterCandidate(
            final Collection<Object> mocks,
            final Field candidateFieldToBeInjected,
            final List<Field> allRemainingCandidateFields,
            final Object injectee,
            final Field injectMocksField) {
        
    }

    private boolean tooMany(Collection<Object> mocks) {
        
    }

    private List<Object> selectMatchingName(
            Collection<Object> mocks, Field candidateFieldToBeInjected) {
        
    }

    /*
     * In this case we have to check whether we have conflicting naming
     * fields. E.g. 2 fields of the same type, but we have to make sure
     * we match on the correct name.
     *
     * Therefore we have to go through all other fields and make sure
     * whenever we find a field that does match its name with the mock
     * name, we should take that field instead.
     */
    private boolean anotherCandidateMatchesMockName(
            final Collection<Object> mocks,
            final Field candidateFieldToBeInjected,
            final List<Field> allRemainingCandidateFields) {
        
    }
}
