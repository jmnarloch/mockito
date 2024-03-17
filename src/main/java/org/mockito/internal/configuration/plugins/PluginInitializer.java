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
        
    }

    /**
     * Equivalent to {@link java.util.ServiceLoader#load} but without requiring
     * Java 6 / Android 2.3 (Gingerbread).
     */
    public <T> T loadImpl(Class<T> service) {
        
    }

    public <T> List<T> loadImpls(Class<T> service) {
        
    }
}
