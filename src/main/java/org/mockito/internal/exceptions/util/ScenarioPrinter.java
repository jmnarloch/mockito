/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.exceptions.util;

import java.util.List;

import org.mockito.internal.exceptions.VerificationAwareInvocation;

public class ScenarioPrinter {

    public String print(List<VerificationAwareInvocation> invocations) {
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (VerificationAwareInvocation ian : invocations) {
            sb.append("  ").append(i).append(". ");
            sb.append(new InvocationMatcher(ian).getLocation());
            sb.append(StackTracePrinter.NEWLINE);
            i++;
        }
        return sb.toString();
    }
}
