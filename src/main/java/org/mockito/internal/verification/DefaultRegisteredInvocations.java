/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import static org.mockito.internal.util.ObjectMethodsGuru.isToStringMethod;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.mockito.invocation.Invocation;

public class DefaultRegisteredInvocations implements RegisteredInvocations, Serializable {

    private static final long serialVersionUID = -2674402327380736290L;
    private final LinkedList<Invocation> invocations = new LinkedList<>();

    @Override
    public void add(Invocation invocation) {
        invocations.add(invocation);
    }

    @Override
    public void removeLast() {
        if (!invocations.isEmpty()) {
            invocations.removeLast();
        }
    }

    @Override
    public List<Invocation> getAll() {
        return invocations;
    }

    @Override
    public void clear() {
        invocations.clear();
    }

    @Override
    public boolean isEmpty() {
        return invocations.isEmpty();
    }
}
