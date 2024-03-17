/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.reporting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.mockito.invocation.Invocation;
import org.mockito.invocation.MatchableInvocation;

/**
 * Makes sure both wanted and actual are printed consistently (single line or multiline)
 * <p>
 * Makes arguments printed with types if necessary
 */
public class SmartPrinter {

    private final String wanted;
    private final List<String> actuals;

    public SmartPrinter(
            MatchableInvocation wanted,
            Invocation actual,
            Integer... indexesOfMatchersToBeDescribedWithExtraTypeInfo) {
        
    }

    public SmartPrinter(
            MatchableInvocation wanted,
            List<Invocation> allActualInvocations,
            Integer[] indexesOfMatchersToBeDescribedWithExtraTypeInfo,
            Set<String> classNamesToBeDescribedWithFullName) {
        
    }

    public String getWanted() {
        
    }

    public List<String> getActuals() {
        
    }

    private static boolean isMultiLine(
            MatchableInvocation wanted, List<Invocation> allActualInvocations) {
        
    }
}
