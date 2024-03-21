/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.injection.filter;

import static org.mockito.internal.exceptions.Reporter.moreThanOneMockCandidate;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.mockito.internal.util.MockUtil;

public class TypeBasedCandidateFilter implements MockCandidateFilter {

    private final MockCandidateFilter next;

    public TypeBasedCandidateFilter(MockCandidateFilter next) {
        this.next = next;
    }

    protected boolean isCompatibleTypes(Type typeToMock, Type mockType, Field injectMocksField) {
        if (typeToMock instanceof ParameterizedType && mockType instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) typeToMock).getActualTypeArguments();
            Type[] actualTypeArguments2 = ((ParameterizedType) mockType).getActualTypeArguments();
            if (actualTypeArguments.length == actualTypeArguments2.length) {
                if (recurseOnTypeArguments(
                injectMocksField, actualTypeArguments, actualTypeArguments2)) {
                    return true;
                }
            }
        } else if (typeToMock instanceof Class && mockType instanceof Class) {
            if (TypeUtil.isAssignableFrom(typeToMock, mockType)) {
                return true;
            }
        } else if (mockType instanceof WildcardType) {
            Type[] upperBounds = ((WildcardType) mockType).getUpperBounds();
            for (Type upperBound : upperBounds) {
                if (isCompatibleTypes(typeToMock, upperBound, injectMocksField)) {
                    return true;
                }
            }
        }

        return false;
    }

    private Stream<Type> getSuperTypes(Class<?> concreteMockClass) {
        Stream<Type> interfaces = Arrays.stream(concreteMockClass.getGenericInterfaces());
        Stream<Type> superType = Stream.of(concreteMockClass.getGenericSuperclass());
        return Stream.concat(interfaces, superType).filter(t -> t != Object.class);
    }

    private boolean recurseOnTypeArguments(
            Field injectMocksField, Type[] actualTypeArguments, Type[] actualTypeArguments2) {
        boolean result = true;
        for (int i = 0; i < actualTypeArguments.length; i++) {
            final Type actualTypeArgument = actualTypeArguments[i];
            final Type actualTypeArgument2 = actualTypeArguments2[i];
            if (actualTypeArgument instanceof TypeVariable) {
                // this is the case we hope to find a binding on injectMocksField
                final TypeVariable<?> typeVariable = (TypeVariable<?>) actualTypeArgument;
                final Class<?> rawType = (Class<?>) actualTypeArgument2;

                final Type[] injectMocksFieldGenericTypeParameters =
                ((ParameterizedType) injectMocksField.getGenericType())
                .getActualTypeArguments();
                final String[] genericTypeParameterNames = rawType.getTypeParameters();
                for (int j = 0; j < injectMocksFieldGenericTypeParameters.length; j++) {
                    if (genericTypeParameterNames[j].equals(typeVariable.getName())) {
                        // we found a binding, recurse to check if other bindings are also
                        // compatible
                        result &=
                        isCompatibleTypes(
                        injectMocksFieldGenericTypeParameters[j],
                        actualTypeArgument2,
                        injectMocksField)
                        && recurseOnTypeArguments(
                        injectMocksField,
                        actualTypeArguments,
                        actualTypeArguments2);
                    }
                }
            } else {
                //  some other kind of class, e.g. a concrete class, a wildcard, ...
                result &= isCompatibleTypes(actualTypeArgument, actualTypeArgument2, null);
            }
        }
        return result;
    }

    @Override
    public OngoingInjector filterCandidate(
            final Collection<Object> mocks,
            final Field candidateFieldToBeInjected,
            final List<Field> allRemainingCandidateFields,
            final Object injectee,
            final Field injectMocksField) {
        List<Object> mockTypeMatches = new ArrayList<>();
        for (Object mock : mocks) {
            Type mockType = MockUtil.getMockSettings(mock).getGenericTypeToMock();

            // concrete class for the mock
            Class<?> concreteMockClass;
            // type to mock for the mock
            Type typeToMock = mockType;
            while (true) {
                if (typeToMock instanceof ParameterizedType) {
                    concreteMockClass = (Class<?>) ((ParameterizedType) typeToMock).getRawType();
                    if (recurseOnTypeArguments(mockTypeMatches, injectee, injectMocksField, (ParameterizedType) typeToMock))
                    break;
                } else {
                    concreteMockClass = (Class<?>) typeToMock;
                    if (concreteMockClass == Object.class) break;
                }

                if (mockTypeMatches.add(concreteMockClass)
                & isCompatibleTypes(
                typeToMock, mockType, injectMocksField)) break;

                mockType = concreteMockClass;
                // super types for the mock
                Stream<Type> superTypes = getSuperTypes(concreteMockClass);
                if (recurseOnSuperTypes(mockTypeMatches, injectee, injectMocksField, superTypes))
                break;
            }
        }
        if (mockTypeMatches.isEmpty()) {
            return next.filterCandidate(
            mocks, candidateFieldToBeInjected, allRemainingCandidateFields, injectee, injectMocksField);
        } else if (mockTypeMatches.size() > 1) {
            throw moreThanOneMockCandidate(
            candidateFieldToBeInjected, mockTypeMatches, injectee, injectMocksField);
        } else {
            return () -> {
                Object mock = mockTypeMatches.get(0);
                candidateFieldToBeInjected.set(injectee, mock);
                return allRemainingCandidateFields;
            };
        }
    }
}
