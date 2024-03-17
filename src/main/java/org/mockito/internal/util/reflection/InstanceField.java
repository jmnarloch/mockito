/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.util.Checks;
import org.mockito.plugins.MemberAccessor;

/**
 * Represents an accessible instance field.
 *
 * Contains the instance reference on which the field can be read and write.
 */
public class InstanceField {
    private final Field field;
    private final Object instance;
    private FieldReader fieldReader;

    /**
     * Create a new InstanceField.
     *
     * @param field The field that should be accessed, note that no checks are performed to ensure
     *              the field belong to this instance class.
     * @param instance The instance from which the field shall be accessed.
     */
    public InstanceField(Field field, Object instance) {
        
    }

    /**
     * Safely read the field.
     *
     * @return the field value.
     * @see FieldReader
     */
    public Object read() {
        
    }

    /**
     * Set the given value to the field of this instance.
     *
     * @param value The value that should be written to the field.
     */
    public void set(Object value) {
        
    }

    /**
     * Check that the field is not null.
     *
     * @return <code>true</code> if <code>null</code>, else <code>false</code>.
     */
    public boolean isNull() {
        
    }

    /**
     * Check if the field is annotated by the given annotation.
     *
     * @param annotationClass The annotation type to check.
     * @return <code>true</code> if the field is annotated by this annotation, else <code>false</code>.
     */
    public boolean isAnnotatedBy(Class<? extends Annotation> annotationClass) {
        
    }

    /**
     * Check if the field is synthetic.
     *
     * @return <code>true</code> if the field is synthetic, else <code>false</code>.
     */
    public boolean isSynthetic() {
        
    }

    /**
     * Returns the annotation instance for the given annotation type.
     *
     * @param annotationClass Tha annotation type to retrieve.
     * @param <A> Type of the annotation.
     * @return The annotation instance.
     */
    public <A extends Annotation> A annotation(Class<A> annotationClass) {
        
    }

    /**
     * Returns the JDK {@link Field} instance.
     *
     * @return The actual {@link Field} instance.
     */
    public Field jdkField() {
        
    }

    private FieldReader reader() {
        
    }

    /**
     * Returns the name of the field.
     *
     * @return Name of the field.
     */
    public String name() {
        
    }

    @Override
    public String toString() {
        
    }

    @Override
    public boolean equals(Object o) {
        
    }

    @Override
    public int hashCode() {
        
    }
}
