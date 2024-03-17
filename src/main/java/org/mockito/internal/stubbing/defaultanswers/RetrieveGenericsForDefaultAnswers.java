/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockCreationSettings;

final class RetrieveGenericsForDefaultAnswers {

    static Object returnTypeForMockWithCorrectGenerics(
            InvocationOnMock invocation, AnswerCallback answerCallback) {
        
    }

    /**
     * Try to resolve the result value using {@link ReturnsEmptyValues} and {@link ReturnsMoreEmptyValues}.
     *
     * This will try to use all parent class (superclass and interfaces) to retrieve the value..
     *
     * @param type the return type of the method
     * @return a non-null instance if the type has been resolve. Null otherwise.
     */
    private static Object delegateChains(final Class<?> type) {
        
    }

    /**
     * Retrieve the expected type when it came from a primitive. If the type cannot be retrieve, return null.
     *
     * @param invocation the current invocation
     * @param returnType the expected return type
     * @return the type or null if not found
     */
    private static Class<?> findTypeFromGeneric(
            final InvocationOnMock invocation, final TypeVariable returnType) {
        // Class level
        
    }

    /**
     * Find a return type using generic arguments provided by the calling method.
     *
     * @param invocation the current invocation
     * @param returnType the expected return type
     * @return the return type or null if the return type cannot be found
     */
    private static Class<?> findTypeFromGenericInArguments(
            final InvocationOnMock invocation, final TypeVariable returnType) {
        
    }

    interface AnswerCallback {
        Object apply(Class<?> type);
    }

    private RetrieveGenericsForDefaultAnswers() { }
}
