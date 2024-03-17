/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/** Attempts to extract generic type of given target base class or target interface */
public final class GenericTypeExtractor {

    /**
     * Extract generic type of root class either from the target base class or from target base interface.
     * Examples:
     *  <p>
     *  1. Foo implements IFoo[Integer]:
     *      genericTypeOf(Foo.class, Object.class, IFoo.class) returns Integer
     *  <p>
     *  2. Foo extends BaseFoo[String]:
     *      genericTypeOf(Foo.class, BaseFoo.class, IFoo.class) returns String
     *  <p>
     *  3. Foo extends BaseFoo; BaseFoo implements IFoo[String]:
     *      genericTypeOf(Foo.class, BaseFoo.class, Object.class) returns String
     *  <p>
     *  Does not support nested generics, only supports single type parameter.
     *
     * @param rootClass - the root class that the search begins from
     * @param targetBaseClass - if one of the classes in the root class' hierarchy extends this base class
     *                        it will be used for generic type extraction
     * @param targetBaseInterface - if one of the interfaces in the root class' hierarchy implements this interface
     *                            it will be used for generic type extraction
     * @return generic interface if found, Object.class if not found.
     */
    public static Class<?> genericTypeOf(
            Class<?> rootClass, Class<?> targetBaseClass, Class<?> targetBaseInterface) {
        // looking for candidates in the hierarchy of rootClass
        
    }

    /**
     * Finds generic interface implementation based on the source class and the target interface.
     * Returns null if not found. Recurses the interface hierarchy.
     */
    private static Type findGenericInterface(Class<?> sourceClass, Class<?> targetBaseInterface) {
        
    }

    /**
     * Attempts to extract generic parameter type of given type.
     * If there is no generic parameter it returns Object.class
     */
    private static Class<?> extractGeneric(Type type) {
        
    }

    private GenericTypeExtractor() { }
}
