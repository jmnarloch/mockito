/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import org.mockito.internal.creation.DelegatingMethod;
import org.mockito.internal.debugging.LocationFactory;
import org.mockito.internal.invocation.mockref.MockWeakReference;
import org.mockito.internal.progress.SequenceNumber;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationFactory;
import org.mockito.invocation.Location;
import org.mockito.mock.MockCreationSettings;

public class DefaultInvocationFactory implements InvocationFactory {

    public Invocation createInvocation(
            Object target,
            MockCreationSettings settings,
            Method method,
            final Callable realMethod,
            Object... args) {
        
    }

    @Override
    public Invocation createInvocation(
            Object target,
            MockCreationSettings settings,
            Method method,
            RealMethodBehavior realMethod,
            Object... args) {
        
    }

    private Invocation createInvocation(
            Object target,
            MockCreationSettings settings,
            Method method,
            RealMethod superMethod,
            Object[] args) {
        
    }

    public static InterceptedInvocation createInvocation(
            Object mock,
            Method invokedMethod,
            Object[] arguments,
            RealMethod realMethod,
            MockCreationSettings settings,
            Location location) {
        
    }

    private static InterceptedInvocation createInvocation(
            Object mock,
            Method invokedMethod,
            Object[] arguments,
            RealMethod realMethod,
            MockCreationSettings settings) {
        
    }

    private static MockitoMethod createMockitoMethod(Method method, MockCreationSettings settings) {
        
    }
}
