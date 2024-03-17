/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import java.util.Collection;

import org.mockito.MockingDetails;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.debugging.InvocationsPrinter;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stubbing.Stubbing;

/**
 * Class to inspect any object, and identify whether a particular object is either a mock or a spy.  This is
 * a wrapper for {@link org.mockito.internal.util.MockUtil}.
 */
public class DefaultMockingDetails implements MockingDetails {

    private final Object toInspect;

    public DefaultMockingDetails(Object toInspect) {
        
    }

    @Override
    public boolean isMock() {
        
    }

    @Override
    public boolean isSpy() {
        
    }

    @Override
    public Collection<Invocation> getInvocations() {
        
    }

    private InvocationContainerImpl getInvocationContainer() {
        
    }

    @Override
    public MockCreationSettings<?> getMockCreationSettings() {
        
    }

    @Override
    public Collection<Stubbing> getStubbings() {
        
    }

    @Override
    public String printInvocations() {
        
    }

    @Override
    public MockHandler getMockHandler() {
        
    }

    @Override
    public Object getMock() {
        
    }

    private MockHandler<?> mockHandler() {
        
    }

    private void assertGoodMock() {
        
    }
}
