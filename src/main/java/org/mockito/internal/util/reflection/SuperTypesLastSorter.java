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

    private SuperTypesLastSorter() {}

    /**
     * Return a new collection with the fields sorted first by name,
     * then with any fields moved after their supertypes.
     */
    public static List<Field> sortSuperTypesLast(Collection<? extends Field> unsortedFields) {
        List<Field> fields = new ArrayList<>(unsortedFields);

        Collections.sort(fields, compareFieldsByName);

        int i = 0;
        while (i < fields.size() - 1) {
            Field f = fields.get(i);
            Class<?> ft = f.getType();
            for (int j = i + 1; j < fields.size(); j++) {
                if (fields.get(j).getType() == ft) {
                    if (j != i + 1) {
                        // Not a direct neighbor, and types are the same: swap with the latter
                        // type's slot so that
                        // all fields of the same type are together.
                        Field tmp = fields.get(j);
                        fields.set(j, fields.get(i + 1));
                        fields.set(i + 1, tmp);
                    }
                    i++;
                    break;
                }
            }
        }

        return fields;
    }

    private static final Comparator<Field> compareFieldsByName =
            (Field o1, Field o2) -> {
                return o1.getName().compareTo(o2.getName());
            };
}
