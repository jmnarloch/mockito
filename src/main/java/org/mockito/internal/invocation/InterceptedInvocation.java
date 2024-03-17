/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import static org.mockito.internal.exceptions.Reporter.cannotCallAbstractRealMethod;
import static org.mockito.internal.invocation.ArgumentsProcessor.argumentsToMatchers;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.mockito.ArgumentMatcher;
import org.mockito.internal.exceptions.VerificationAwareInvocation;
import org.mockito.internal.invocation.mockref.MockReference;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.invocation.StubInfo;

public class InterceptedInvocation implements Invocation, VerificationAwareInvocation {

    private static final long serialVersionUID = 475027563923510472L;

    private final MockReference<Object> mockRef;
    private final MockitoMethod mockitoMethod;
    private final Object[] arguments;
    private final Object[] rawArguments;
    private final RealMethod realMethod;

    private final int sequenceNumber;

    private final Location location;

    private boolean verified;
    private boolean isIgnoredForVerification;
    private StubInfo stubInfo;

    public InterceptedInvocation(
            MockReference<Object> mockRef,
            MockitoMethod mockitoMethod,
            Object[] arguments,
            RealMethod realMethod,
            Location location,
            int sequenceNumber) {
        
    }

    @Override
    public boolean isVerified() {
        
    }

    @Override
    public int getSequenceNumber() {
        
    }

    @Override
    public Location getLocation() {
        
    }

    @Override
    public Object[] getRawArguments() {
        
    }

    @Override
    public Class<?> getRawReturnType() {
        
    }

    @Override
    public void markVerified() {
        
    }

    @Override
    public StubInfo stubInfo() {
        
    }

    @Override
    public void markStubbed(StubInfo stubInfo) {
        
    }

    @Override
    public boolean isIgnoredForVerification() {
        
    }

    @Override
    public void ignoreForVerification() {
        
    }

    @Override
    public Object getMock() {
        
    }

    @Override
    public Method getMethod() {
        
    }

    @Override
    public Object[] getArguments() {
        
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getArgument(int index) {
        
    }

    @Override
    public <T> T getArgument(int index, Class<T> clazz) {
        
    }

    @Override
    public List<ArgumentMatcher> getArgumentsAsMatchers() {
        
    }

    @Override
    public Object callRealMethod() throws Throwable {
        
    }

    /**
     * @deprecated Not used by Mockito but by mockito-scala
     */
    @Deprecated
    public MockReference<Object> getMockRef() {
        
    }

    /**
     * @deprecated Not used by Mockito but by mockito-scala
     */
    @Deprecated
    public MockitoMethod getMockitoMethod() {
        
    }

    /**
     * @deprecated Not used by Mockito but by mockito-scala
     */
    @Deprecated
    public RealMethod getRealMethod() {
        
    }

    @Override
    public int hashCode() { }

    @Override
    public boolean equals(Object o) {
        
    }

    private boolean equalArguments(Object[] arguments) {
        
    }

    @Override
    public String toString() {
        
    }

    public static final RealMethod NO_OP =
            new RealMethod() {
                @Override
                public boolean isInvokable() { }

                public Object invoke() throws Throwable { }
            };
}
