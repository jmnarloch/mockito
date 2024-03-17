/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.plugins;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.mockito.plugins.PluginSwitch;

class PluginLoader {

    private final DefaultMockitoPlugins plugins;
    private final PluginInitializer initializer;

    PluginLoader(DefaultMockitoPlugins plugins, PluginInitializer initializer) {
        
    }

    PluginLoader(PluginSwitch pluginSwitch) {
        
    }

    /**
     * Adds an alias for a class name to this plugin loader. Instead of the fully qualified type name,
     * the alias can be used as a convenience name for a known plugin. This avoids exposing API that is
     * explicitly marked as <i>internal</i> through the package name. Without such aliases, we would need
     * to make internal packages part of the API, not by code but by configuration file.
     */
    PluginLoader(PluginSwitch pluginSwitch, String... alias) {
        
    }

    /**
     * Scans the classpath for given pluginType. If not found, default class is used.
     */
    <T> T loadPlugin(final Class<T> pluginType) {
        
    }

    /**
     * Scans the classpath for given {@code preferredPluginType}. If not found scan for {@code
     * alternatePluginType}. If neither a preferred or alternate plugin is found, default to default
     * class of {@code preferredPluginType}.
     *
     * @return An object of either {@code preferredPluginType} or {@code alternatePluginType},
     * cast to the lowest common denominator in the chain of inheritance
     */
    @SuppressWarnings("unchecked")
    <ReturnT, PreferredT extends ReturnT, AlternateType extends ReturnT> ReturnT loadPlugin(
            final Class<PreferredT> preferredPluginType,
            final Class<AlternateType> alternatePluginType) {
        
    }

    /**
     * Scans the classpath for given {@code pluginType} and returns a list of its instances.
     *
     * @return An list of {@code pluginType} or an empty list if none was found.
     */
    @SuppressWarnings("unchecked")
    <T> List<T> loadPlugins(final Class<T> pluginType) {
        
    }
}
