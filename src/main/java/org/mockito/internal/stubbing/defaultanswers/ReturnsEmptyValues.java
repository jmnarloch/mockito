/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import static org.mockito.internal.util.ObjectMethodsGuru.isCompareToMethod;
import static org.mockito.internal.util.ObjectMethodsGuru.isToStringMethod;

import java.io.Serializable;
import java.time.Duration;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.Primitives;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;
import org.mockito.stubbing.Answer;

/**
 * Default answer of every Mockito mock.
 * <ul>
 * <li>
 * Returns appropriate primitive for primitive-returning methods
 * </li>
 * <li>
 * Returns consistent values for primitive wrapper classes (e.g. int-returning method returns 0 <b>and</b> Integer-returning method returns 0, too)
 * </li>
 * <li>
 * Returns empty collection for collection-returning methods (works for most commonly used collection types)
 * </li>
 * <li>
 * Returns description of mock for toString() method
 * </li>
 * <li>
 * Returns zero if references are equals otherwise non-zero for Comparable#compareTo(T other) method (see issue 184)
 * </li>
 * <li>
 * Returns an {@code java.util.Optional#empty() empty Optional} for Optional. Similarly for primitive optional variants.
 * </li>
 * <li>
 * Returns an {@code java.util.stream.Stream#empty() empty Stream} for Stream. Similarly for primitive stream variants.
 * </li>
 * <li>
 * Returns an {@code java.time.Duration.ZERO zero Duration} for empty Duration and {@code java.time.Period.ZERO zero Period} for empty Period.
 * </li>
 * <li>
 * Returns null for everything else
 * </li>
 * </ul>
 */
public class ReturnsEmptyValues implements Answer<Object>, Serializable {

    private static final long serialVersionUID = 1998191268711234347L;

    /* (non-Javadoc)
     * @see org.mockito.stubbing.Answer#answer(org.mockito.invocation.InvocationOnMock)
     */
    @Override
    public Object answer(InvocationOnMock invocation) {
        if (isToStringMethod(invocation.getMethod())) {
            MockName name = MockUtil.getMockName(invocation.getMock());
            if (name.isDefault()) {
                return "Mock for "
                + MockUtil.getMockSettings(invocation.getMock()).getTypeToMock().getName()
                + ", hashCode: "
                + invocation.getMock().hashCode();
            } else {
                return name.toString();
            }
        } else if (isCompareToMethod(invocation.getMethod())) {
            return Integer.valueOf(0);
        }

        Class<?> returnType = invocation.getMethod().getReturnType();
        return this.returnValueFor(returnType);
    }

    Object returnValueFor(Class<?> type) {
        if (type == Stream.class) {
            return Stream.empty();
        } else if (type == IntStream.class) {
            return IntStream.empty();
        } else if (type == LongStream.class) {
            return LongStream.empty();
        } else if (type == DoubleStream.class) {
            return DoubleStream.empty();
        }
        return returnCommonEmptyValueFor(type);
    }

    /**
     * Returns empty values for common known types, shared between {@link ReturnsEmptyValues} and {@link ReturnsDeepStubs}.
     *
     * @param type the type to check
     * @return the empty value, or {@code null}
     */
    static Object returnCommonEmptyValueFor(Class<?> type) {
        if (type == Optional.class) {
            return Optional.empty();
        } else if (type == OptionalDouble.class) {
            return OptionalDouble.empty();
        } else if (type == OptionalInt.class) {
            return OptionalInt.empty();
        } else if (type == OptionalLong.class) {
            return OptionalLong.empty();
        }
        return null;
    }
}
