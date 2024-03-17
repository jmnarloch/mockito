/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.Ownership;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.MethodCall;
import net.bytebuddy.implementation.StubMethod;
import net.bytebuddy.utility.GraalImageCode;
import net.bytebuddy.utility.RandomString;
import org.mockito.Mockito;
import org.mockito.codegen.InjectionBase;
import org.mockito.exceptions.base.MockitoException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static net.bytebuddy.matcher.ElementMatchers.isTypeInitializer;
import static org.mockito.internal.util.StringUtil.join;

abstract class ModuleHandler {

    abstract boolean isOpened(Class<?> source, Class<?> target);

    abstract boolean canRead(Class<?> source, Class<?> target);

    abstract boolean isExported(Class<?> source);

    abstract boolean isExported(Class<?> source, Class<?> target);

    abstract Class<?> injectionBase(ClassLoader classLoader, String tyoeName);

    abstract void adjustModuleGraph(Class<?> source, Class<?> target, boolean export, boolean read);

    static ModuleHandler make(ByteBuddy byteBuddy, SubclassLoader loader) {
        
    }

    private static class ModuleSystemFound extends ModuleHandler {

        private final ByteBuddy byteBuddy;
        private final SubclassLoader loader;

        private final int injectonBaseSuffix;

        private final Method getModule,
                isOpen,
                isExported,
                isExportedUnqualified,
                canRead,
                addExports,
                addReads,
                forName;

        private ModuleSystemFound(ByteBuddy byteBuddy, SubclassLoader loader) throws Exception {
            
        }

        @Override
        boolean isOpened(Class<?> source, Class<?> target) {
            
        }

        @Override
        boolean canRead(Class<?> source, Class<?> target) {
            
        }

        @Override
        boolean isExported(Class<?> source) {
            
        }

        @Override
        boolean isExported(Class<?> source, Class<?> target) {
            
        }

        @Override
        Class<?> injectionBase(ClassLoader classLoader, String typeName) {
            
        }

        @Override
        void adjustModuleGraph(Class<?> source, Class<?> target, boolean export, boolean read) {
            
        }

        private static Object invoke(Method method, Object target, Object... args) {
            
        }
    }

    private static class NoModuleSystemFound extends ModuleHandler {

        @Override
        boolean isOpened(Class<?> source, Class<?> target) { }

        @Override
        boolean canRead(Class<?> source, Class<?> target) { }

        @Override
        boolean isExported(Class<?> source) { }

        @Override
        boolean isExported(Class<?> source, Class<?> target) { }

        @Override
        Class<?> injectionBase(ClassLoader classLoader, String tyoeName) {
            
        }

        @Override
        void adjustModuleGraph(Class<?> source, Class<?> target, boolean export, boolean read) { }
    }
}
