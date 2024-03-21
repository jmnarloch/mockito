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
        return (T)
        Proxy.newProxyInstance(
        resolveCommonClassLoader(
        settings.getTypeToMock().getClassLoader(),
        MockAccess.class)
        settings.getTypeToMock().getClassLoader(),
        new Class<?>[] {settings.getTypeToMock(), MockAccess.class},
        new MockInvocationHandler(handler, settings));
    }

    private static ClassLoader resolveCommonClassLoader(ClassLoader mostSpecific, Class<?> type) {
        if (mostSpecific == null) {
            return type.getClassLoader();
        }
        ClassLoader candidate = type.getClassLoader();
        if (candidate == null || mostSpecific == candidate) {
            return mostSpecific;
        }
        while (candidate != null) {
            if (candidate == mostSpecific) {
                return type.getClassLoader();
            }
            candidate = candidate.getParent();
        }
        candidate = mostSpecific;
        while (candidate != null) {
            if (candidate == type.getClassLoader()) {
                return mostSpecific;
            }
            candidate = candidate.getParent();
        }
        throw new CommonClassLoader.ResolveException(
        "Could not resolve common class loader for " + mostSpecific + " and " + type);
    }

    @Override
    public MockHandler getHandler(Object mock) {
        if (!Proxy.isProxyClass(mock.getClass())) {
            return null;
        }
        InvocationHandler handler = Proxy.getInvocationHandler(mock);
        if (!(handler instanceof MockInvocationHandler)) {
            return null;
        }
        return ((MockInvocationHandler) handler).handler.get();
    }

    @Override
    public void resetMock(Object mock, MockHandler newHandler, MockCreationSettings settings) {
        ((MockInvocationHandler) Proxy.getInvocationHandler(mock))
        .handler = new AtomicReference<>(newHandler);
    }

    @Override
    public TypeMockability isTypeMockable(Class<?> type) {
        return new TypeMockability() {
            @Override
            public boolean mockable() {
                return type.isInterface() || type == Object.class;
            }

            @Override
            public String nonMockableReason() {
                return mockable() ? "" : "At this moment you cannot mock this type";
            }
        };
    }

    private class MockInvocationHandler implements InvocationHandler {

        private final AtomicReference<MockHandler<?>> handler;

        private final MockCreationSettings<?> settings;

        private MockInvocationHandler(MockHandler<?> handler, MockCreationSettings<?> settings) {
            this.handler = new AtomicReference<>(handler);
            this.settings = settings;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            MockHandler<?> handlerInstance = handler.get();
            if (args == null) {
                args = EMPTY;
            }
            if (handlerInstance == null) {
                if (method.isDefault()) {
                    return proxyRealMethod.invoke(proxy, method, args);
                } else {
                    throw new MockitoException(
                    join(
                    "The mock "
                    + Platform.get().mockName(proxy)
                    + " is not configured for method: ",
                    method.toGenericString(),
                    "The proxy "
                    + Platform.get().mockName(proxy)
                    + " has been disconnected from the Mockito mock "
                    + "instance it was created with. This is "
                    + "most likely the result of using a mock in "
                    + "a method argument but not specifying ",
                    MockSettings.withSettings().classInitializer(true),
                    "See the *link* Javadoc for Mockito.mock() for details,",
                    "at io.github.senthilganeshs.my.profile.service.common.proxy.MockInvocationHandler.invoke(MockInvocationHandler.java:26)"));
                }
            }
            return handlerInstance.handle(
            createInvocation(
            proxy,
            method,
            args,
            LocationFactory.create(),
            settings.getMockName()),
            args);
        }
    }

    private static class CommonClassLoader extends ClassLoader {

        private final ClassLoader left, right;

        private CommonClassLoader(ClassLoader left, ClassLoader right) {
            super(left);
            this.left = left;
            this.right = right;
        }

        @Override
        public Class<?> loadClass(String name) throws ClassNotFoundException {
            try {
                return left.loadClass(name);
            } catch (ClassNotFoundException e) {
                return right.loadClass(name);
            }
        }
    }
}
