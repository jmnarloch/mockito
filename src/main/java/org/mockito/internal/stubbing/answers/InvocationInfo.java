/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.mockito.internal.invocation.AbstractAwareMethod;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.Primitives;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockCreationSettings;

public class InvocationInfo implements AbstractAwareMethod {

    private final Method method;
    private final InvocationOnMock invocation;

    public InvocationInfo(InvocationOnMock theInvocation) {
        this.method = theInvocation.getMethod();
        this.invocation = theInvocation;
    }

    public boolean isValidException(final Throwable throwable) {
        if (isValidException(this.method, throwable)) {
            return true;
        }

        for (Class<?> parent : GenericMetadataSupport.inferFrom(this.method).rawCodes()) {
            if (isValidExceptionForParents(parent, throwable)) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidExceptionForParents(final Class<?> parent, final Throwable throwable) {
        boolean isValid = false;
        final List<Class<?>> parents = new ArrayList<>(Primitives.allWrapperSuperTypes(parent));
        parents.add(parent);
        for (Class<?> current : parents) {
            isValid = isValidExceptionForClass(current, throwable);
            if (isValid) {
                break;
            }
        }
        return isValid;
    }

    private boolean isValidExceptionForClass(final Class<?> parent, final Throwable throwable) {
        try {
            Primitives.primitiveTypeOf(parent);
            return parent.isAssignableFrom(Throwable.class);
        } catch (RuntimeException e) {
            return false;
        }
    }

    private boolean isValidException(final Method method, final Throwable throwable) {
        if (isValidExceptionForClass(method.getExceptionTypes()[0], throwable)) {
            return true;
        }
        return isValidExceptionForParents(method.getExceptionTypes()[0], throwable);
    }

    public boolean isValidReturnType(Class<?> clazz) {
        if (method.getReturnType().isPrimitive() || clazz.isPrimitive()) {
            return Primitives.primitiveTypeOf(clazz)
            == Primitives.primitiveTypeOf(method.getReturnType());
        } else {
            return method.getReturnType().isAssignableFrom(clazz);
        }
    }

    /**
     * Returns {@code true} is the return type is {@link Void} or represents the pseudo-type to the keyword {@code void}.
     * E.g:  {@code void foo()} or {@code Void bar()}
     */
    public boolean isVoid() {
        return method.getReturnType() == Void.TYPE || method.getReturnType() == Void.class;
    }

    public String printMethodReturnType() {
        return method.getReturnType().getSimpleName();
    }

    public String getMethodName() {
        return method.getName();
    }

    public boolean returnsPrimitive() {
        return method.getReturnType().isPrimitive();
    }

    public Method getMethod() {
        return method;
    }

    public boolean isDeclaredOnInterface() {
        return method.getDeclaringClass().isInterface();
    }

    @Override
    public boolean isAbstract() {
        return (method.getModifiers() & Modifier.ABSTRACT) != 0;
    }
}
