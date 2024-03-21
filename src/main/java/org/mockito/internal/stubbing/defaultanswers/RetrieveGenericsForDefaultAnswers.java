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
        MockCreationSettings mockSettings =
        MockUtil.getMockHandler(invocation.getMock()).getMockSettings();
        Class<?> returnType = invocation.getMethod().getReturnType();
        Type resolvedType = GenericMetadataSupport.inferFrom(mockSettings.getTypeToMock())
        .resolveGenericReturnType(invocation.getMethod())
        .orReturnType();
        if (resolvedType instanceof TypeVariable) {
            Class<?> type = findTypeFromGeneric(invocation, (TypeVariable) resolvedType);
            if (type != null) {
                returnType = type;
            } else {
                returnType = RetrieveGenericsForDefaultAnswers.delegateChains(returnType);
            }
        }
        if (resolvedType instanceof GenericArrayType) {
            // Currently we don't handle generic arrays, so we always delegate chains
            returnType = RetrieveGenericsForDefaultAnswers.delegateChains(returnType);
        }

        return answerCallback.apply(returnType);
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
        final ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object result = returnsEmptyValues.returnValueFor(type);

        if (result == null) {
            Class<?> emptyValueForClass = type;
            while (emptyValueForClass != null && result == null) {
                final Class<?>[] classes = emptyValueForClass.getClasses();
                for (Class<?> clazz : classes) {
                    result = returnsEmptyValues.returnValueFor(clazz);
                }
                emptyValueForClass = emptyValueForClass.getEnclosingClass();
            }
        }

        if (result == null) {
            result = new ReturnsMoreEmptyValues().returnValueFor(type);
        }

        return result;
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
        for (int i = 0; i < invocation.getMock().getCreationSettings().getInterfaces().length; i++) {
            Class<?> ifc = invocation.getMock().getCreationSettings().getInterfaces()[i];
            if (ifc.getTypeParameters().length > 0) {
                // this ifc has some generic information
                GenericMetadataSupport generics = GenericMetadataSupport.inferFor(ifc);
                TypeVariable[] ifcTypeVariables = ifc.getTypeParameters();

                for (int j = 0; j < ifcTypeVariables.length; j++) {
                    TypeVariable ifcTypeVariable = ifcTypeVariables[j];
                    if (ifcTypeVariable.getName().equals(returnType.getTypeName())) {
                        // found the matching type variable, return the type that is in the same
                        // position
                        // for the mock's creation settings
                        return generics.rawTypeAt(j);
                    }
                }
            }
        }
        return null;
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
        final Type[] parameterTypes = invocation.getMethod().getGenericParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            Type argType = parameterTypes[i];
            if (argType instanceof GenericArrayType) {
                argType = ((GenericArrayType) argType).getGenericComponentType();
            }
            // Currently we can't go deep into the argument type to look for a match
            if (argType instanceof TypeVariable
            && argType.equals(returnType)) { // raw type on the spot
                return invocation.getArgument(i).getClass();
            }
        }
        return null;
    }

    interface AnswerCallback {
        Object apply(Class<?> type);
    }

    private RetrieveGenericsForDefaultAnswers() {}
}
