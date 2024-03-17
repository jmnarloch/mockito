/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import java.io.Serializable;

import org.mockito.configuration.DefaultMockitoConfiguration;
import org.mockito.configuration.IMockitoConfiguration;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.stubbing.Answer;

/**
 * Thread-safe wrapper on user-defined org.mockito.configuration.MockitoConfiguration implementation
 */
public class GlobalConfiguration implements IMockitoConfiguration, Serializable {
    private static final long serialVersionUID = -2860353062105505938L;

    private static final ThreadLocal<IMockitoConfiguration> GLOBAL_CONFIGURATION =
            new ThreadLocal<>();

    // back door for testing
    IMockitoConfiguration getIt() {
        
    }

    public GlobalConfiguration() {
        // Configuration should be loaded only once but I cannot really test it
        
    }

    private IMockitoConfiguration createConfig() {
        
    }

    public static void validate() {
        
    }

    public org.mockito.plugins.AnnotationEngine tryGetPluginAnnotationEngine() {
        
    }

    @Override
    public boolean cleansStackTrace() {
        
    }

    @Override
    public boolean enableClassCache() {
        
    }

    @Override
    public Answer<Object> getDefaultAnswer() {
        
    }
}
