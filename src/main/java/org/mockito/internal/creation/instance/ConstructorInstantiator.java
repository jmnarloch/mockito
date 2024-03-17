/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.instance;

import static org.mockito.internal.util.StringUtil.join;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.mockito.creation.instance.InstantiationException;
import org.mockito.creation.instance.Instantiator;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.util.Primitives;
import org.mockito.plugins.MemberAccessor;

public class ConstructorInstantiator implements Instantiator {

    /**
     * Whether or not the constructors used for creating an object refer to an outer instance or not.
     * This member is only used to for constructing error messages.
     * If an outer inject exists, it would be the first ([0]) element of the {@link #constructorArgs} array.
     */
    private final boolean hasOuterClassInstance;

    private final Object[] constructorArgs;

    public ConstructorInstantiator(boolean hasOuterClassInstance, Object... constructorArgs) {
        
    }

    @Override
    public <T> T newInstance(Class<T> cls) {
        
    }

    private <T> T withParams(Class<T> cls, Object... params) {
        
    }

    @SuppressWarnings("unchecked")
    private static <T> T invokeConstructor(Constructor<?> constructor, Object... params)
            throws java.lang.InstantiationException,
                    IllegalAccessException,
                    InvocationTargetException {
        
    }

    private InstantiationException paramsException(Class<?> cls, Exception e) {
        
    }

    private String constructorArgTypes() {
        
    }

    private InstantiationException noMatchingConstructor(Class<?> cls) {
        
    }

    private String constructorArgsString() {
        
    }

    private InstantiationException multipleMatchingConstructors(
            Class<?> cls, List<Constructor<?>> constructors) {
        
    }

    private static boolean paramsMatch(Class<?>[] types, Object[] params) {
        
    }

    /**
     * Evalutes {@code constructor} against the currently found {@code matchingConstructors} and determines if
     * it's a better match to the given arguments, a worse match, or an equivalently good match.
     * <p>
     * This method tries to emulate the behavior specified in
     * <a href="https://docs.oracle.com/javase/specs/jls/se8/html/jls-15.html#jls-15.12.2">JLS 15.12.2. Compile-Time
     * Step 2: Determine Method Signature</a>. A constructor X is deemed to be a better match than constructor Y to the
     * given argument list if they are both applicable, constructor X has at least one parameter than is more specific
     * than the corresponding parameter of constructor Y, and constructor Y has no parameter than is more specific than
     * the corresponding parameter in constructor X.
     * </p>
     * <p>
     * If {@code constructor} is a better match than the constructors in the {@code matchingConstructors} list, the list
     * is cleared, and it's added to the list as a singular best matching constructor (so far).<br/>
     * If {@code constructor} is an equivalently good of a match as the constructors in the {@code matchingConstructors}
     * list, it's added to the list.<br/>
     * If {@code constructor} is a worse match than the constructors in the {@code matchingConstructors} list, the list
     * will remain unchanged.
     * </p>
     *
     * @param matchingConstructors A list of equivalently best matching constructors found so far
     * @param constructor The constructor to be evaluated against this list
     */
    private void evaluateConstructor(
            List<Constructor<?>> matchingConstructors, Constructor<?> constructor) {
        
    }
}
