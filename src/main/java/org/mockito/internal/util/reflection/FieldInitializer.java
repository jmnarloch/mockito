/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.util.MockUtil;
import org.mockito.plugins.MemberAccessor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.lang.reflect.Modifier.isStatic;

/**
 * Initialize a field with type instance if a default constructor can be found.
 *
 * <p>
 * If the given field is already initialized, then <strong>the actual instance is returned</strong>.
 * This initializer doesn't work with inner classes, local classes, interfaces or abstract types.
 * </p>
 *
 */
public class FieldInitializer {

    private final Object fieldOwner;
    private final Field field;
    private final ConstructorInstantiator instantiator;

    /**
     * Prepare initializer with the given field on the given instance.
     *
     * <p>
     * This constructor fail fast if the field type cannot be handled.
     * </p>
     *
     * @param fieldOwner Instance of the test.
     * @param field Field to be initialize.
     */
    public FieldInitializer(Object fieldOwner, Field field) {
        
    }

    /**
     * Prepare initializer with the given field on the given instance.
     *
     * <p>
     * This constructor fail fast if the field type cannot be handled.
     * </p>
     *
     * @param fieldOwner Instance of the test.
     * @param field Field to be initialize.
     * @param argResolver Constructor parameters resolver
     */
    public FieldInitializer(
            Object fieldOwner, Field field, ConstructorArgumentResolver argResolver) {
        
    }

    private FieldInitializer(Object fieldOwner, Field field, ConstructorInstantiator instantiator) {
        
    }

    /**
     * Initialize field if not initialized and return the actual instance.
     *
     * @return Actual field instance.
     */
    public FieldInitializationReport initialize() {
        
    }

    private void checkNotLocal(Field field) {
        
    }

    private void checkNotInner(Field field) {
        
    }

    private void checkNotInterface(Field field) {
        
    }

    private void checkNotAbstract(Field field) {
        
    }

    private void checkNotEnum(Field field) {
        
    }

    private FieldInitializationReport acquireFieldInstance() throws IllegalAccessException {
        
    }

    /**
     * Represents the strategy used to resolve actual instances
     * to be given to a constructor given the argument types.
     */
    public interface ConstructorArgumentResolver {

        /**
         * Try to resolve instances from types.
         *
         * <p>
         * Checks on the real argument type or on the correct argument number
         * will happen during the field initialization {@link FieldInitializer#initialize()}.
         * I.e the only responsibility of this method, is to provide instances <strong>if possible</strong>.
         * </p>
         *
         * @param argTypes Constructor argument types, should not be null.
         * @return The argument instances to be given to the constructor, should not be null.
         */
        Object[] resolveTypeInstances(Class<?>... argTypes);
    }

    private interface ConstructorInstantiator {
        FieldInitializationReport instantiate();
    }

    /**
     * Constructor instantiating strategy for no-arg constructor.
     *
     * <p>
     * If a no-arg constructor can be found then the instance is created using
     * this constructor.
     * Otherwise a technical MockitoException is thrown.
     * </p>
     */
    static class NoArgConstructorInstantiator implements ConstructorInstantiator {
        private final Object testClass;
        private final Field field;

        /**
         * Internal, checks are done by FieldInitializer.
         * Fields are assumed to be accessible.
         */
        NoArgConstructorInstantiator(Object testClass, Field field) {
            
        }

        @Override
        public FieldInitializationReport instantiate() {
            
        }
    }

    /**
     * Constructor instantiating strategy for parameterized constructors.
     *
     * <p>
     * Choose the constructor with the highest number of parameters, then
     * call the ConstructorArgResolver to get actual argument instances.
     * If the argResolver fail, then a technical MockitoException is thrown is thrown.
     * Otherwise the instance is created with the resolved arguments.
     * </p>
     */
    static class ParameterizedConstructorInstantiator implements ConstructorInstantiator {
        private final Object testClass;
        private final Field field;
        private final ConstructorArgumentResolver argResolver;
        private final Comparator<Constructor<?>> byParameterNumber =
                new Comparator<Constructor<?>>() {
                    @Override
                    public int compare(Constructor<?> constructorA, Constructor<?> constructorB) {
                        
                    }

                    private int countMockableParams(Constructor<?> constructor) {
                        
                    }
                };

        /**
         * Internal, checks are done by FieldInitializer.
         * Fields are assumed to be accessible.
         */
        ParameterizedConstructorInstantiator(
                Object testClass, Field field, ConstructorArgumentResolver argumentResolver) {
            
        }

        @Override
        public FieldInitializationReport instantiate() {
            
        }

        private void checkParameterized(Constructor<?> constructor, Field field) {
            
        }

        private Constructor<?> biggestConstructor(Class<?> clazz) {
            
        }
    }
}
