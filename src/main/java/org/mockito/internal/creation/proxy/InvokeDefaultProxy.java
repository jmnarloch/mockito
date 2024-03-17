/*
 * Copyright (c) 2021 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.proxy;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.invocation.RealMethod;
import org.mockito.internal.invocation.SerializableMethod;
import org.mockito.internal.util.Platform;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.mockito.internal.util.StringUtil.join;

class InvokeDefaultProxy implements ProxyRealMethod {

    private final Method invokeDefault;

    InvokeDefaultProxy() throws Throwable {
        
    }

    @Override
    public RealMethod resolve(Object proxy, Method method, Object[] args) {
        
    }

    private class InvokeDefaultRealMethod implements RealMethod, Serializable {

        private static final long serialVersionUID = -1;

        private final Object proxy;
        private final SerializableMethod serializableMethod;
        private final Object[] args;

        private InvokeDefaultRealMethod(Object proxy, Method method, Object[] args) {
            
        }

        @Override
        public boolean isInvokable() { }

        @Override
        public Object invoke() throws Throwable {
            
        }
    }
}
