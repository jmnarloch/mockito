/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.plugins;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.mockito.MockMakers;
import org.mockito.internal.util.MockUtil;
import org.mockito.plugins.AnnotationEngine;
import org.mockito.plugins.DoNotMockEnforcerWithType;
import org.mockito.plugins.InstantiatorProvider2;
import org.mockito.plugins.MemberAccessor;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockitoLogger;
import org.mockito.plugins.MockitoPlugins;
import org.mockito.plugins.PluginSwitch;
import org.mockito.plugins.StackTraceCleanerProvider;

public class DefaultMockitoPlugins implements MockitoPlugins {

    private static final Map<String, String> DEFAULT_PLUGINS = new HashMap<>();
    static final String INLINE_ALIAS = MockMakers.INLINE;
    static final String PROXY_ALIAS = MockMakers.PROXY;
    static final String SUBCLASS_ALIAS = MockMakers.SUBCLASS;
    public static final Set<String> MOCK_MAKER_ALIASES = new HashSet<>();
    static final String MODULE_ALIAS = "member-accessor-module";
    static final String REFLECTION_ALIAS = "member-accessor-reflection";
    public static final Set<String> MEMBER_ACCESSOR_ALIASES = new HashSet<>();

    static {
        // Keep the mapping: plugin interface name -> plugin implementation class name
        DEFAULT_PLUGINS.put(PluginSwitch.class.getName(), DefaultPluginSwitch.class.getName());
        DEFAULT_PLUGINS.put(
                MockMaker.class.getName(),
                "org.mockito.internal.creation.bytebuddy.InlineByteBuddyMockMaker");
        DEFAULT_PLUGINS.put(
                StackTraceCleanerProvider.class.getName(),
                "org.mockito.internal.exceptions.stacktrace.DefaultStackTraceCleanerProvider");
        DEFAULT_PLUGINS.put(
                InstantiatorProvider2.class.getName(),
                "org.mockito.internal.creation.instance.DefaultInstantiatorProvider");
        DEFAULT_PLUGINS.put(
                AnnotationEngine.class.getName(),
                "org.mockito.internal.configuration.InjectingAnnotationEngine");
        DEFAULT_PLUGINS.put(
                INLINE_ALIAS, "org.mockito.internal.creation.bytebuddy.InlineByteBuddyMockMaker");
        DEFAULT_PLUGINS.put(PROXY_ALIAS, "org.mockito.internal.creation.proxy.ProxyMockMaker");
        DEFAULT_PLUGINS.put(
                SUBCLASS_ALIAS, "org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker");
        DEFAULT_PLUGINS.put(
                MockitoLogger.class.getName(), "org.mockito.internal.util.ConsoleMockitoLogger");
        DEFAULT_PLUGINS.put(
                MemberAccessor.class.getName(),
                "org.mockito.internal.util.reflection.ModuleMemberAccessor");
        DEFAULT_PLUGINS.put(
                MODULE_ALIAS, "org.mockito.internal.util.reflection.ModuleMemberAccessor");
        DEFAULT_PLUGINS.put(
                REFLECTION_ALIAS, "org.mockito.internal.util.reflection.ReflectionMemberAccessor");
        DEFAULT_PLUGINS.put(
                DoNotMockEnforcerWithType.class.getName(),
                "org.mockito.internal.configuration.DefaultDoNotMockEnforcer");

        MOCK_MAKER_ALIASES.add(INLINE_ALIAS);
        MOCK_MAKER_ALIASES.add(PROXY_ALIAS);
        MOCK_MAKER_ALIASES.add(SUBCLASS_ALIAS);

        MEMBER_ACCESSOR_ALIASES.add(MODULE_ALIAS);
        MEMBER_ACCESSOR_ALIASES.add(REFLECTION_ALIAS);
    }

    @Override
    public <T> T getDefaultPlugin(Class<T> pluginType) {
        
    }

    public static String getDefaultPluginClass(String classOrAlias) {
        
    }

    /**
     * Creates an instance of given plugin type, using specific implementation class.
     */
    private <T> T create(Class<T> pluginType, String className) {
        
    }

    @Override
    public MockMaker getInlineMockMaker() {
        
    }

    @Override
    public MockMaker getMockMaker(String mockMaker) {
        
    }
}
