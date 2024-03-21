/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.debugging;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.mockito.Mockito;
import org.mockito.invocation.Invocation;
import org.mockito.stubbing.Stubbing;

/**
 * Prints invocations in human-readable, printable way
 */
public class InvocationsPrinter {

    public String printInvocations(Object mock) {
        Collection<Invocation> invocations = Mockito.mockingDetails(mock).getInvocations();
        if (invocations.isEmpty()) {
            return "No interactions available for " + mock;
        }
        List<Stubbing> stubbings = Mockito.mockingDetails(mock).getStubbings();
        String stubbingsLine =
        stubbings.isEmpty()
        ? ""
        : "Stubbings";
        return "Interactions of " + mock + "\n" + stubbingsLine + stubbings.stream().map(s -> "  " + s).collect(Collectors.joining("\n  ", "\n  ", "\n")) + "Invocations" + invocations.stream().map(i -> "  " + i).collect(Collectors.joining("\n  ", "\n  ", "\n"));
    }
}
