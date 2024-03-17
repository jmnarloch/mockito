/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import static org.mockito.internal.exceptions.Reporter.moreThanOneAnnotationNotAllowed;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.ScopedMock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.plugins.AnnotationEngine;
import org.mockito.plugins.MemberAccessor;

/**
 * Initializes fields annotated with &#64;{@link org.mockito.Mock} or &#64;{@link org.mockito.Captor}.
 *
 * <p>
 * The {@link #process(Class, Object)} method implementation <strong>does not</strong> process super classes!
 *
 * @see MockitoAnnotations
 */
@SuppressWarnings("unchecked")
public class IndependentAnnotationEngine implements AnnotationEngine {
    private final Map<Class<? extends Annotation>, FieldAnnotationProcessor<?>>
            annotationProcessorMap = new HashMap<>();

    public IndependentAnnotationEngine() {
        
    }

    private Object createMockFor(Annotation annotation, Field field) {
        
    }

    private <A extends Annotation> FieldAnnotationProcessor<A> forAnnotation(A annotation) {
        
    }

    private <A extends Annotation> void registerAnnotationProcessor(
            Class<A> annotationClass, FieldAnnotationProcessor<A> fieldAnnotationProcessor) {
        
    }

    @Override
    public AutoCloseable process(Class<?> clazz, Object testInstance) {
        
    }

    void throwIfAlreadyAssigned(Field field, boolean alreadyAssigned) {
        
    }
}
