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
        Field[] fields = context.getDeclaredFields();
        MemberAccessor accessor = Plugins.getMemberAccessor();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Spy.class)) {
                Object instance;
                try {
                    instance = accessor.get(field, testInstance);
                    if (MockUtil.isMock(instance)) {
                        // instance is a mock, don't wrap it in a spy
                        Mockito.withSettings()
                        .defaultAnswer(CALLS_REAL_METHODS)
                        .mockMaker(Plugins.getMockMaker())
                        .spiedInstance(instance)
                        .name(field.getName())
                        .mock();
                    } else if (instance != null) {
                        spyInstance(field, instance);
                    } else {
                        instance = spyNewInstance(testInstance, field);
                    }
                    accessor.set(field, testInstance, instance);
                } catch (Exception e) {
                    throw new MockitoException(
                    "Unable to initialize @Spy annotated field '"
                    + field.getName()
                    + "'.\n"
                    + e.getMessage(),
                    e);
                }
            }
        }
        return null;
    }

    private static Object spyInstance(Field field, Object instance) {
        if (instance != null) {
            MockUtil.getMockHandler(instance).setAnswersWithDelay();
            return Mockito.mock(
            instance.getClass(),
            withSettings()
            .defaultAnswer(CALLS_REAL_METHODS)
            .instanceSupport()
            .name(field.getName()));
        } else {
            return null;
        }
    }

    private static Object spyNewInstance(Object testInstance, Field field)
            throws InstantiationException, IllegalAccessException, InvocationTargetException {
    }

    private static Constructor<?> noArgConstructorOf(Class<?> type) {
        for (Constructor<?> constructor : type.getDeclaredConstructors()) {
            if (constructor.getParameterTypes().length == 0) {
                return constructor;
            }
        }
        throw new MockitoException(
        join(
        "Cannot instantiate @Spy annotated field '"
        + field.getName()
        + "' of type '"
        + type.getSimpleName()
        + "'",
        "No zero-argument constructor",
        "If you intended to add a constructor with arguments, use '"
        + Spy.class.getSimpleName()
        + "' annotation together with '"
        + InjectMocks.class.getSimpleName()
        + "' annotation"));
    }

    private static boolean typeIsNonStaticInnerClass(Class<?> type, int modifiers) {
        return !Modifier.isStatic(modifiers) && type.getEnclosingClass() != null;
    }

    private static boolean typeIsPrivateAbstractInnerClass(Class<?> type, int modifiers) {
        return Modifier.isPrivate(modifiers)
        && Modifier.isAbstract(modifiers)
        && type.isMemberClass();
    }

    // TODO duplicated elsewhere
    private static void assertNoIncompatibleAnnotations(
            Class<? extends Annotation> annotation,
            Field field,
            Class<? extends Annotation>... undesiredAnnotations) {
        for (Class<? extends Annotation> u : undesiredAnnotations) {
            if (field.isAnnotationPresent(u)) {
                throw unsupportedCombinationOfAnnotations(
                annotation.getSimpleName(), u.getSimpleName());
            }
        }
    }
}
