/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.handler;

import static org.mockito.internal.matchers.Equality.areEqual;

import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.listeners.MethodInvocationReport;

/**
 * Report on a method call
 */
public class NotifiedMethodInvocationReport implements MethodInvocationReport {
    private final Invocation invocation;
    private final Object returnedValue;
    private final Throwable throwable;

    /**
     * Build a new {@link org.mockito.listeners.MethodInvocationReport} with a return value.
     *
     *
     * @param invocation Information on the method call
     * @param returnedValue The value returned by the method invocation
     */
    public NotifiedMethodInvocationReport(Invocation invocation, Object returnedValue) {
        
    }

    /**
     * Build a new {@link org.mockito.listeners.MethodInvocationReport} with a return value.
     *
     *
     * @param invocation Information on the method call
     * @param throwable Tha throwable raised by the method invocation
     */
    public NotifiedMethodInvocationReport(Invocation invocation, Throwable throwable) {
        
    }

    @Override
    public DescribedInvocation getInvocation() {
        
    }

    @Override
    public Object getReturnedValue() {
        
    }

    @Override
    public Throwable getThrowable() {
        
    }

    @Override
    public boolean threwException() {
        
    }

    @Override
    public String getLocationOfStubbing() {
        
    }

    @Override
    public boolean equals(Object o) {
        
    }

    @Override
    public int hashCode() {
        
    }
}
