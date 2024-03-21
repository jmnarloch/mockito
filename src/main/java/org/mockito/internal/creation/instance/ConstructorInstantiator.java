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
        this.hasOuterClassInstance = hasOuterClassInstance;
        this.constructorArgs = constructorArgs;
    }

    @Override
    public <T> T newInstance(Class<T> cls) {
        if (hasOuterClassInstance) {
            return withParams(cls, Arrays.copyOfRange(constructorArgs, 1, constructorArgs.length));
        }
        return withParams(cls, constructorArgs);
    }

    private <T> T withParams(Class<T> cls, Object... params) {
        List<Constructor<?>> matchingConstructors = new LinkedList<>();
        for (Constructor<?> constructor : cls.getDeclaredConstructors()) {
            Class<?>[] types = constructor.getParameterTypes();
            if (paramsMatch(types, params)) {
                evaluateConstructor(matchingConstructors, constructor);
            }
        }
        if (matchingConstructors.size() == 1) {
            //noinspection unchecked
            return (T) invokeConstructor(matchingConstructors.get(0), params);
        }
        if (matchingConstructors.isEmpty()) {
            throw noMatchingConstructor(cls);
        }
        throw multipleMatchingConstructors(cls, matchingConstructors);
    }

    @SuppressWarnings("unchecked")
    private static <T> T invokeConstructor(Constructor<?> constructor, Object... params)
            throws java.lang.InstantiationException,
                    IllegalAccessException,
                    InvocationTargetException {
        MemberAccessor accessor = Plugins.getMemberAccessor();
        constructor.setAccessible(true);
        try {
            return (T) accessor.newInstance(constructor, params);
        } catch (InvocationTargetException
        | InstantiationException
        | IllegalAccessException
        | IllegalArgumentException e) {
            throw e;
        } finally {
            constructor.setAccessible(false);
        }
    }

    private InstantiationException paramsException(Class<?> cls, Exception e) {
        return new InstantiationException(
        join(
        "Unable to create instance of '" + cls.getSimpleName() + "'.",
        "Please ensure the target class has "
        + constructorArgsString()
        + " and executes cleanly."),
        e);
    }

    private String constructorArgTypes() {
        StringBuilder argTypes = new StringBuilder();
        for (Object arg : constructorArgs) {
            argTypes.append(arg == null ? "null" : arg.getClass().getName()).append(", ");
        }
        if (argTypes.length() > 0) {
            argTypes.setLength(argTypes.length() - 2);
        }
        return argTypes.toString();
    }

    private InstantiationException noMatchingConstructor(Class<?> cls) {
        String outerOrEmpty = hasOuterClassInstance ? " (whilst outer object was provided)" : "";
        return new InstantiationException(
        join(
        "Unable to create instance of '" + cls.getCanonicalName() + outerOrEmpty + "'.",
        "",
        "No suitable constructor found for class " + cls + ".",
        "Requested constructor is not available.",
        "The following constructors are applicable...",
        constructorArgTypes(),
        "...but their parameters do not match those requested...",
        constructorArgsString(),
        "Consider specifying parameters or omitting parameters with @Inject.",
        ""),
        null);
    }

    private String constructorArgsString() {
        String constructorString;
        if (constructorArgs.length == 0 || (hasOuterClassInstance && constructorArgs.length == 1)) {
            constructorString = "a 0-arg constructor";
        } else {
            constructorString = "a constructor that matches these argument types: " + constructorArgTypes();
        }
        return constructorString;
    }

    private InstantiationException multipleMatchingConstructors(
            Class<?> cls, List<Constructor<?>> constructors) {
        return new InstantiationException(
        join(
        "Unable to create instance of '" + cls.getSimpleName() + "'",
        "Multiple constructors could be matched to arguments of types "
        + constructorArgTypes()
        + ":",
        join("", " - ", constructors),
        "If you believe that Mockito could do a better job deciding "
        + "which constructor to use, please let us know ().
        + "Be aware that we do not support spying on a real instance with matching leaf "
        + "constructor for deep hierarchy chains."),
        "");
    }

    private static boolean paramsMatch(Class<?>[] types, Object[] params) {
        if (params.length != types.length) {
            return false;
        }
        for (int i = 0; i < params.length; i++) {
            if (params[i] == null) {
                if (types[i].isPrimitive()) {
                    return false;
                }
            } else if ((!types[i].isPrimitive() && !types[i].isInstance(params[i]))
            || (types[i].isPrimitive()
            && !types[i].equals(Primitives.unbox(params[i].getClass())))) {
                return false;
            }
        }
        return true;
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
        boolean isBetterMatch = true;
        for (int i = 0, constructorCount = constructor.getParameterTypes().length;
        i < constructorCount;
        i++) {
            if (isBetterMatch) {
                Class<?>[] constructorParams = constructor.getParameterTypes();
                Class<?>[] existingParams =
                matchingConstructors.get(0).getParameterTypes(); // 0th, as we know there
                // is at least one

                if (constructorParams[i].isAssignableFrom(existingParams[i])) {
                    // Ok, good so far
                } else {
                    // Uh-oh, current constructor is not a better match than the previous best
                    // matching one
                    isBetterMatch = false;

                    // Let's check if the current constructor is an equivalently good match as the
                    // previous one
                    for (int m = 1, matchingCount = matchingConstructors.size();
                    m < matchingCount;
                    m++) {
                        if (matchingConstructors
                        .get(m)
                        .getParameterTypes()[i]
                        .isAssignableFrom(existingParams[i])) {
                            // Yup, current constructor is an equivalently good match as the
                            // previous best matching one
                            isBetterMatch = true;
                            break;
                        }
                    }
                }
            }
        }
        if (isBetterMatch) {
            matchingConstructors.clear();
        }
        if (isBetterMatch || !matchingConstructors.contains(constructor)) {
            matchingConstructors.add(constructor);
        }
    }
}
