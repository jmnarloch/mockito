/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.debugging;

import java.util.List;

import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.Invocation;

public class WarningsPrinterImpl {

    private final boolean warnAboutUnstubbed;
    private final WarningsFinder finder;

    public WarningsPrinterImpl(
            List<Invocation> unusedStubs,
            List<InvocationMatcher> allInvocations,
            boolean warnAboutUnstubbed) {
        
    }

    WarningsPrinterImpl(boolean warnAboutUnstubbed, WarningsFinder finder) {
        
    }

    public String print() {
        
    }
}
