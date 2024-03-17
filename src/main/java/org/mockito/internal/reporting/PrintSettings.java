/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.reporting;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.text.MatchersPrinter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MatchableInvocation;

public class PrintSettings {

    public static final int MAX_LINE_LENGTH = 45;
    private boolean multiline;
    private List<Integer> withTypeInfo = new LinkedList<>();
    private Set<String> withFullyQualifiedName = Collections.emptySet();

    public void setMultiline(boolean multiline) {
        
    }

    public boolean isMultiline() {
        
    }

    public static PrintSettings verboseMatchers(Integer... indexesOfMatchers) {
        
    }

    public boolean extraTypeInfoFor(int argumentIndex) {
        
    }

    public boolean fullyQualifiedNameFor(String simpleClassName) {
        
    }

    public void setMatchersToBeDescribedWithExtraTypeInfo(Integer[] indexesOfMatchers) {
        
    }

    public void setMatchersToBeDescribedWithFullName(Set<String> indexesOfMatchers) {
        
    }

    public String print(List<ArgumentMatcher> matchers, Invocation invocation) {
        
    }

    public String print(Invocation invocation) {
        
    }

    public String print(MatchableInvocation invocation) {
        
    }
}
