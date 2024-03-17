/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import static org.mockito.internal.invocation.MatcherApplicationStrategy.getMatcherApplicationStrategyFor;
import static org.mockito.internal.invocation.TypeSafeMatching.matchesTypeSafe;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.invocation.MatchableInvocation;

/**
 * In addition to all content of the invocation, the invocation matcher contains the argument matchers. Invocation matcher is used during verification and stubbing. In those cases, the user can provide argument matchers instead of 'raw' arguments. Raw arguments are converted to 'equals' matchers anyway.
 */
@SuppressWarnings("serial")
public class InvocationMatcher implements MatchableInvocation, DescribedInvocation, Serializable {

    private final Invocation invocation;
    private final List<ArgumentMatcher<?>> matchers;

    @SuppressWarnings({"rawtypes", "unchecked"})
    public InvocationMatcher(Invocation invocation, List<ArgumentMatcher> matchers) {
        
    }

    @SuppressWarnings("rawtypes")
    public InvocationMatcher(Invocation invocation) {
        
    }

    public static List<InvocationMatcher> createFrom(List<Invocation> invocations) {
        
    }

    public Method getMethod() {
        
    }

    @Override
    public Invocation getInvocation() {
        
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<ArgumentMatcher> getMatchers() {
        
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public String toString() {
        
    }

    @Override
    public boolean matches(Invocation candidate) {
        
    }

    /**
     * similar means the same method name, same mock, unverified and: if arguments are the same cannot be overloaded
     */
    @Override
    public boolean hasSimilarMethod(Invocation candidate) {
        
    }

    @Override
    public boolean hasSameMethod(Invocation candidate) {
        // not using method.equals() for 1 good reason:
        // sometimes java generates forwarding methods when generics are in play see
        // JavaGenericsForwardingMethodsTest
        
    }

    @Override
    public Location getLocation() {
        
    }

    @Override
    public void captureArgumentsFrom(Invocation invocation) {
        
    }

    private ArgumentMatcherAction captureArgument() {
        
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private boolean argumentsMatch(Invocation actual) {
        
    }
}
