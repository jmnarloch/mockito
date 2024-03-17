/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.mockito.invocation.Invocation;
import org.mockito.plugins.MockitoLogger;

/**
 * Contains stubbing arg mismatches, knows how to format them
 */
class StubbingArgMismatches {

    final Map<Invocation, Set<Invocation>> mismatches = new LinkedHashMap<>();

    public void add(Invocation invocation, Invocation stubbing) {
        
    }

    public void format(String testName, MockitoLogger logger) {
        
    }

    public int size() {
        
    }

    @Override
    public String toString() {
        
    }
}
