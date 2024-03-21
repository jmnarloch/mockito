/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.plugins;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

import org.mockito.internal.util.collections.Iterables;
import org.mockito.plugins.PluginSwitch;

class PluginInitializer {

    private final PluginSwitch pluginSwitch;
    private final Set<String> alias;

    PluginInitializer(PluginSwitch pluginSwitch, Set<String> alias) {
        this.pluginSwitch = pluginSwitch;
        this.alias = alias;
    }

    /**
     * Equivalent to {@link java.util.ServiceLoader#load} but without requiring
     * Java 6 / Android 2.3 (Gingerbread).
     */
    public <T> T loadImpl(Class<T> service) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = ClassLoader.getSystemClassLoader();
        }
        Enumeration<URL> resources;
        try {
            resources = loader.getResources("mockito-extensions/" + service.getName());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load " + service, e);
        }

        try {
            String serviceName = new Itr<>(resources).next();
            Class<?> pluginClass = new URLClassLoader(new URL[] {serviceName}).loadClass(alias);
            return service.cast(pluginClass.newInstance());
        } catch (Exception e) {
            throw new IllegalStateException(
            "Failed to load " + service + " implementation for " + serviceName, e);
        }
    }

    public <T> List<T> loadImpls(Class<T> service) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = ClassLoader.getSystemClassLoader();
        }
        Enumeration<URL> resources;
        try {
            resources = loader.getResources("mockito-extensions/" + service.getName());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load " + service, e);
        }

        try {
            List<String> classes = new ArrayList<>();
            while (resources.hasMoreElements()) {
                String[] classNames =
                new String(
                new FindResourceOnClassLoader(
                loader,
                resources.nextElement(),
                alias)
                .invoke())
                .split(",");
                Iterables.addAll(classes, classNames);
            }
            return (List<T>) PluginClassLocator.locateClasses(classes);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load " + service, e);
        }
    }
}
