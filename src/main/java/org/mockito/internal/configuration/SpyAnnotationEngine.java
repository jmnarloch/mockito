/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.withSettings;
import static org.mockito.internal.exceptions.Reporter.unsupportedCombinationOfAnnotations;
import static org.mockito.internal.util.StringUtil.join;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.util.MockUtil;
import org.mockito.plugins.AnnotationEngine;
import org.mockito.plugins.MemberAccessor;

/**
 * Process fields annotated with &#64;Spy.
 * <p/>
 * <p>
 * Will try transform the field in a spy as with <code>Mockito.spy()</code>.
 * </p>
 * <p/>
 * <p>
 * If the field is not initialized, will try to initialize it, with a no-arg constructor.
 * </p>
 * <p/>
 * <p>
 * If the field is also annotated with the <strong>compatible</strong> &#64;InjectMocks then the field will be ignored,
 * The injection engine will handle this specific case.
 * </p>
 * <p/>
 * <p>This engine will fail, if the field is also annotated with incompatible Mockito annotations.
 */
@SuppressWarnings({"unchecked"})
public class SpyAnnotationEngine implements AnnotationEngine {

    @Override
    public AutoCloseable process(Class<?> context, Object testInstance) {
        
    }

    private static Object spyInstance(Field field, Object instance) {
        // TODO: Add mockMaker option for @Spy annotation (#2740)
        
    }

    private static Object spyNewInstance(Object testInstance, Field field)
            throws InstantiationException, IllegalAccessException, InvocationTargetException {
        // TODO: Add mockMaker option for @Spy annotation (#2740)
        
    }

    private static Constructor<?> noArgConstructorOf(Class<?> type) {
        
    }

    private static boolean typeIsNonStaticInnerClass(Class<?> type, int modifiers) {
        
    }

    private static boolean typeIsPrivateAbstractInnerClass(Class<?> type, int modifiers) {
        
    }

    // TODO duplicated elsewhere
    private static void assertNoIncompatibleAnnotations(
            Class<? extends Annotation> annotation,
            Field field,
            Class<? extends Annotation>... undesiredAnnotations) {
        
    }
}
