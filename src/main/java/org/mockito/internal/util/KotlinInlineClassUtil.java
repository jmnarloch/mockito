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
        
    }

    private static Object unboxInlineClassIfPossible(Object boxedValue) {
        
    }

    public static Object unboxUnderlyingValueIfNeeded(InvocationOnMock invocation, Object value) {
        // Short path - Kotlin 1.5+ is not present.
        
    }
}
