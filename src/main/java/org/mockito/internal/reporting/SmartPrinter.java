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
        this(
        wanted,
        Collections.singletonList(actual),
        indexesOfMatchersToBeDescribedWithExtraTypeInfo,
        new AllInvocationsFinder());
    }

    public SmartPrinter(
            MatchableInvocation wanted,
            List<Invocation> allActualInvocations,
            Integer[] indexesOfMatchersToBeDescribedWithExtraTypeInfo,
            Set<String> classNamesToBeDescribedWithFullName) {
        PrintSettings printSettings = new PrintSettings();
        printSettings.setMultiline(isMultiLine(wanted, allActualInvocations));
        printSettings.setMatchersToBeDescribedWithExtraTypeInfo(
        indexesOfMatchersToBeDescribedWithExtraTypeInfo);
        printSettings.setMatchersToBeDescribedWithFullName(classNamesToBeDescribedWithFullName);

        this.wanted = printSettings.print(wanted);

        List<String> actuals = new ArrayList<>();
        for (Invocation actual : allActualInvocations) {
            actuals.add(printSettings.print(actual));
        }
        this.actuals = Collections.unmodifiableList(actuals);
    }

    public String getWanted() {
        return wanted;
    }

    public List<String> getActuals() {
        if (actuals.size() > 1) {
            actuals.add(actuals.size() - 1, "");
            actuals.add(0, "");
        }
        return actuals;
    }

    private static boolean isMultiLine(
            MatchableInvocation wanted, List<Invocation> allActualInvocations) {
        if (wanted.getInvocation().getLocation() == null) {
            return false;
        }
        int wantedLine = wanted.getInvocation().getLocation().getLine();
        List<Integer> actualLines = new ArrayList<>();
        actualLines.add(wantedLine);

        for (Invocation invocation : allActualInvocations) {
            if (invocation.getLocation() != null) {
                actualLines.add(invocation.getLocation().getLine());
            }
        }
        return !containsConsecutiveNumbers(actualLines);
    }
}
