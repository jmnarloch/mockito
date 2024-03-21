/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers.text;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.ContainsExtraTypeInfo;
import org.mockito.internal.reporting.PrintSettings;

@SuppressWarnings("unchecked")
public class MatchersPrinter {

    public String getArgumentsLine(List<ArgumentMatcher> matchers, PrintSettings printSettings) {
        Iterator args = applyPrintSettings(matchers, printSettings);
        return ValuePrinter.printValues("(", ", ", ");", args);
    }

    public String getArgumentsBlock(List<ArgumentMatcher> matchers, PrintSettings printSettings) {
        Iterator args = applyPrintSettings(matchers, printSettings);
        return ValuePrinter.printValues("(\n    ", ",\n    ", "\n);", args);
    }

    private Iterator<FormattedText> applyPrintSettings(
            List<ArgumentMatcher> matchers, PrintSettings printSettings) {
        return new FormattedMatcherIterator(
        matchers, new LinkedList<>(matchers).iterator(), printSettings, this);
    }
}
