/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

import static java.util.Collections.emptyList;

import static org.mockito.internal.exceptions.Reporter.incorrectUseOfAdditionalMatchers;
import static org.mockito.internal.exceptions.Reporter.misplacedArgumentMatcher;
import static org.mockito.internal.exceptions.Reporter.reportNoSubMatchersFound;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.And;
import org.mockito.internal.matchers.LocalizedMatcher;
import org.mockito.internal.matchers.Not;
import org.mockito.internal.matchers.Or;

public class ArgumentMatcherStorageImpl implements ArgumentMatcherStorage {

    private static final int TWO_SUB_MATCHERS = 2;
    private static final int ONE_SUB_MATCHER = 1;
    private final Stack<LocalizedMatcher> matcherStack = new Stack<>();

    @Override
    public void reportMatcher(ArgumentMatcher<?> matcher) {
        
    }

    @Override
    public List<LocalizedMatcher> pullLocalizedMatchers() {
        
    }

    public void reportAnd() {
        
    }

    @Override
    public void reportOr() {
        
    }

    @Override
    public void reportNot() {
        
    }

    @Override
    public void validateState() {
        
    }

    @Override
    public void reset() {
        
    }

    private void assertStateFor(String additionalMatcherName, int subMatchersCount) {
        
    }

    private ArgumentMatcher<?> popMatcher() {
        
    }

    private List<LocalizedMatcher> resetStack() {
        
    }
}
