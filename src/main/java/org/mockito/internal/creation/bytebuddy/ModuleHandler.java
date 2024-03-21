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
        try {
            return new ModuleSystemFound(byteBuddy, loader);
        } catch (Exception ignored) {
            return new NoModuleSystemFound();
        }
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
            this.byteBuddy = byteBuddy;
            this.loader = loader;
            getModule = Class.class.getDeclaredMethod("getModule");
            isOpen = getModule.getReturnType().getDeclaredMethod("isOpen", String.class);
            isExported = getModule.getReturnType().getDeclaredMethod("isExported", String.class);
            isExportedUnqualified =
            getModule.getReturnType().getDeclaredMethod("isExported", String.class);
            canRead = getModule.getReturnType().getDeclaredMethod("canRead", getModule.getReturnType());
            addExports = getModule.getReturnType().getDeclaredMethod("addExports", String.class);
            addReads = getModule.getReturnType().getDeclaredMethod("addReads", getModule.getReturnType());
            forName = Class.class.getDeclaredMethod("forName", String.class);
            injectonBaseSuffix = Math.abs(RandomString.make().hashCode());
        }

        @Override
        boolean isOpened(Class<?> source, Class<?> target) {
            return (Boolean)
            invoke(isOpen, invoke(getModule, source), target.getModule());
        }

        @Override
        boolean canRead(Class<?> source, Class<?> target) {
            return invoke(canRead, getModule(source), target);
        }

        @Override
        boolean isExported(Class<?> source) {
            return invoke(isExportedUnqualified, getModule(source), source);
        }

        @Override
        boolean isExported(Class<?> source, Class<?> target) {
            try {
                return (Boolean) isExported.invoke(
                getModule.invoke(source),
                getModule.invoke(target));
            } catch (Exception exception) {
                throw new MockitoException(
                join(
                "Could not check if a module is exported in a module graph",
                "",
                "Source module: " + source,
                "Target module: " + target),
                exception);
            }
        }

        @Override
        Class<?> injectionBase(ClassLoader classLoader, String typeName) {
            String existing = typeName.concat(InjectionBase.SUFFIX);
            for (int index = 0; ; index++) {
                try {
                    return loader.loadClass(classLoader, existing);
                } catch (ClassNotFoundException ignored) {
                    existing = typeName + index + InjectionBase.SUFFIX;
                }
            }
        }

        @Override
        void adjustModuleGraph(Class<?> source, Class<?> target, boolean export, boolean read) {
            if (read) {
                if (!canRead(source, target)) {
                    throw new MockitoException(
                    join(
                    "Target " + target + " is not configured to allow reading of source " + source,
                    "",
                    "This is a sample error warning indicating that Mockito was not configured to permit reading of "
                    + source + " by " + target,
                    "For off-heap byte code instrumentation, this is required for Mockito as it allows the writing of stubbed values",
                    "To correct this problem, add the following dependency:",
                    "<requires transitive source-module>;",
                    "Or add opens "
                    + source.getPackage().getName()
                    + " to either the open or opens entries of your source module, "
                    + "org.mockito.mockito-agent or the export of this package to the module graph of your source module, "
                    + "org.mockito.mockito-agent",
                    "For the time being, you are seeing this exception as Mockito could not automatically correct this problem."));
                }
            }
            if (export) {
                if (!isExported(source, target)) {
                    String sourceModuleName = invoke(getModule, source).toString();
                    String targetModuleName = invoke(getModule, target).toString();
                    if (sourceModuleName.equals(targetModuleName)) {
                        // Do not issue a warning for non-exported packages within the same module
                        // that is allowed to read.
                        return;
                    }
                    try {
                        invoke(addExports, source.getModule(), source.getPackage().getName(), target.getModule());
                    } catch (Exception e) {
                        throw new MockitoException(
                        join(
                        "Could not add an export edge from "
                        + source
                        + " to "
                        + target
                        + " to permit Mockito to write mock classes"),
                        e);
                    }
                }
            }
        }

        private static Object invoke(Method method, Object target, Object... args) {
            try {
                return method.invoke(target, args);
            } catch (Exception exception) {
                throw new MockitoException(
                "Could not invoke "
                + method
                + " on "
                + target
                + " with arguments "
                + join(", ", args),
                exception);
            }
        }
    }

    private static class NoModuleSystemFound extends ModuleHandler {

        @Override
        boolean isOpened(Class<?> source, Class<?> target) {
            return false;
        }

        @Override
        boolean canRead(Class<?> source, Class<?> target) {
            return false;
        }

        @Override
        boolean isExported(Class<?> source) {
            return false;
        }

        @Override
        boolean isExported(Class<?> source, Class<?> target) {
            return false;
        }

        @Override
        Class<?> injectionBase(ClassLoader classLoader, String tyoeName) {
            return InjectionBase.class;
        }

        @Override
        void adjustModuleGraph(Class<?> source, Class<?> target, boolean export, boolean read) {}
    }
}
