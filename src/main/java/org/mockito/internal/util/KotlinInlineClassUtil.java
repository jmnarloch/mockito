/*
 * Copyright (c) 2021 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import org.mockito.internal.exceptions.Reporter;
import org.mockito.internal.stubbing.answers.InvocationInfo;
import org.mockito.invocation.InvocationOnMock;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class KotlinInlineClassUtil {
    private static Class<Annotation> jvmInlineAnnotation;

    static {
        try {
            jvmInlineAnnotation = (Class<Annotation>) Class.forName("kotlin.jvm.JvmInline");
        } catch (ClassNotFoundException e) {
            // Do nothing: kotlin is pre 1.5.0
        }
    }

    // When mocking function, returning inline class, its return type is
    // underlying type.
    // So, `thenReturn` calls fails, because of non-compatible types.
    public static boolean isInlineClassWithAssignableUnderlyingType(
            Class<?> inlineClass, Class<?> underlyingType) {
        try {
            if (inlineClass.isAnnotationPresent(jvmInlineAnnotation)) {
                Method boxMethod = inlineClass.getDeclaredMethod("box-impl", inlineClass);
                Class<?> boxedType = boxMethod.getReturnType();
                return underlyingType.isAssignableFrom(boxedType);
            }
        } catch (NoSuchMethodException | NoSuchMethodError | NoSuchFieldError e) {
            // `box-impl` is not guaranteed to be present, so ignore it if not
            // present
        }

        return false;
    }

    private static Object unboxInlineClassIfPossible(Object boxedValue) {
        Class<?> inlineClass = boxedValue.getClass();
        try {
            Method unboxImpl = inlineClass.getDeclaredMethod("unbox-impl");
            return unboxImpl.invoke(boxedValue);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw Reporter.inlineClassWithoutUnboxImpl(inlineClass, e);
        }
    }

    public static Object unboxUnderlyingValueIfNeeded(InvocationOnMock invocation, Object value) {
        Class<?> returnType = invocation.getMethod().getReturnType();
        if (returnType.isAnnotationPresent(jvmInlineAnnotation)) {
            return unboxInlineClassIfPossible(value);
        }
        return value;
    }
}
