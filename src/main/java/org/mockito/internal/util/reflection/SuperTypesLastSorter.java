/*
 * Copyright (c) 2015 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Sort fields in an order suitable for injection, by name with superclasses
 * moved after their subclasses.
 */
public class SuperTypesLastSorter {

    private SuperTypesLastSorter() { }

    /**
     * Return a new collection with the fields sorted first by name,
     * then with any fields moved after their supertypes.
     */
    public static List<Field> sortSuperTypesLast(Collection<? extends Field> unsortedFields) {
        
    }

    private static final Comparator<Field> compareFieldsByName =
            (Field o1, Field o2) -> {
                return o1.getName().compareTo(o2.getName());
            };
}
