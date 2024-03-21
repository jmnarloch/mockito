/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import static org.mockito.internal.exceptions.Reporter.smartNullPointerException;
import static org.mockito.internal.util.ObjectMethodsGuru.isToStringMethod;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.mockito.Mockito;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.bytebuddy.MockAccess;
import org.mockito.internal.debugging.LocationFactory;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.invocation.Location;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stubbing.Answer;

/**
 * Optional Answer that can be used with
 * {@link Mockito#mock(Class, Answer)}
 * <p>
 * This implementation can be helpful when working with legacy code. Un-stubbed
 * methods often return null. If your code uses the object returned by an
 * un-stubbed call, you get a NullPointerException. This implementation of
 * Answer returns SmartNulls instead of nulls.
 * SmartNull gives nicer exception message than NPE because it points out the
 * line where un-stubbed method was called. You just click on the stack trace.
 * <p>
 * ReturnsSmartNulls first tries to return ordinary return values (see
 * {@link ReturnsMoreEmptyValues}) then it tries to return SmartNull. If the
 * return type is not mockable (e.g. final) then ordinary null is returned.
 * <p>
 * ReturnsSmartNulls will be probably the default return values strategy in
 * Mockito 2.1.0
 */
public class ReturnsSmartNulls implements Answer<Object>, Serializable {

    private static final long serialVersionUID = 7618312406617949441L;

    private final Answer<Object> delegate = new ReturnsMoreEmptyValues();

    @Override
    public Object answer(final InvocationOnMock invocation) throws Throwable {
        Object defaultReturnValue = delegate.answer(invocation);

        if (defaultReturnValue != null) {
            return defaultReturnValue;
        }

        return ToNullImpl.INSTANCE.answer(invocation);
    }

    private static class ThrowsSmartNullPointer implements Answer {

        private final InvocationOnMock unstubbedInvocation;

        private final Location location;

        ThrowsSmartNullPointer(InvocationOnMock unstubbedInvocation, Location location) {
            this.unstubbedInvocation = unstubbedInvocation;
            this.location = location;
        }

        @Override
        public Object answer(InvocationOnMock currentInvocation) throws Throwable {
            if (isMethodOf(
            unstubbedInvocation.getMock().getClass(),
            unstubbedInvocation.getMock(),
            currentInvocation.getMethod())) {
                Location selfLocation = LocationFactory.create();
                throw smartNullPointerException(
                "you're invoking "
                + currentInvocation.getMethod()
                + " on "
                + unstubbedInvocation.getMock()
                + " with arguments passed to "
                + unstubbedInvocation.getLocation())
                .addHint(
                "if you don't treat nulls, assert with assertNull() instead.")
                .setSelf(selfLocation)
                .setActual(location);
            }
            throw smartNullPointerException(currentInvocation.getMock(), location);
        }

        private static boolean isMethodOf(Class<?> clazz, Object instance, Method method) {
            if (method.getDeclaringClass() != clazz) {
                return false;
            }
            return !method.isBridge() || instance.getClass().getDeclaredMethod(
            method.getName(),
            method.getParameterTypes()).isBridge();
        }
    }
}
