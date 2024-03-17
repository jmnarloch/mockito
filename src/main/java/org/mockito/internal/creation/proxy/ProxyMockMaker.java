/*
 * Copyright (c) 2021 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.proxy;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.debugging.LocationFactory;
import org.mockito.internal.invocation.RealMethod;
import org.mockito.internal.util.Platform;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.MockMaker;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicReference;

import static org.mockito.internal.invocation.DefaultInvocationFactory.createInvocation;
import static org.mockito.internal.util.StringUtil.join;

/**
 * A mock maker that is using the {@link Proxy} utility and is therefore only capable of mocking interfaces but
 * does not rely on manual byte code generation but only uses official and public Java API.
 */
public class ProxyMockMaker implements MockMaker {

    private static final Object[] EMPTY = new Object[0];

    private final ProxyRealMethod proxyRealMethod = ProxyRealMethod.make();

    @Override
    @SuppressWarnings("unchecked")
    public <T> T createMock(MockCreationSettings<T> settings, MockHandler handler) {
        
    }

    private static ClassLoader resolveCommonClassLoader(ClassLoader mostSpecific, Class<?> type) {
        
    }

    @Override
    public MockHandler getHandler(Object mock) {
        
    }

    @Override
    public void resetMock(Object mock, MockHandler newHandler, MockCreationSettings settings) {
        
    }

    @Override
    public TypeMockability isTypeMockable(Class<?> type) {
        
    }

    private class MockInvocationHandler implements InvocationHandler {

        private final AtomicReference<MockHandler<?>> handler;

        private final MockCreationSettings<?> settings;

        private MockInvocationHandler(MockHandler<?> handler, MockCreationSettings<?> settings) {
            
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            
        }
    }

    private static class CommonClassLoader extends ClassLoader {

        private final ClassLoader left, right;

        private CommonClassLoader(ClassLoader left, ClassLoader right) {
            
        }

        @Override
        public Class<?> loadClass(String name) throws ClassNotFoundException {
            
        }
    }
}
