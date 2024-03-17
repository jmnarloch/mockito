/*
 * Copyright (c) 2021 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.proxy;

import org.mockito.internal.SuppressSignatureCheck;
import org.mockito.internal.invocation.RealMethod;

import java.io.Serializable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

@SuppressSignatureCheck
class MethodHandleProxy implements ProxyRealMethod {

    private final MethodHandles.Lookup lookup;

    MethodHandleProxy() throws Throwable {
        
    }

    @Override
    public RealMethod resolve(Object proxy, Method method, Object[] args) {
        
    }

    @SuppressSignatureCheck
    static class LegacyVersion implements ProxyRealMethod {

        private final Constructor<MethodHandles.Lookup> constructor;

        LegacyVersion() throws Throwable {
            
        }

        @Override
        public RealMethod resolve(Object proxy, Method method, Object[] args) {
            
        }
    }

    @SuppressSignatureCheck
    private static class MethodHandleRealMethod implements RealMethod, Serializable {

        private static final long serialVersionUID = -1;

        private final MethodHandle handle;
        private final Object[] args;

        private MethodHandleRealMethod(MethodHandle handle, Object[] args) {
            
        }

        @Override
        public boolean isInvokable() { }

        @Override
        public Object invoke() throws Throwable {
            
        }
    }
}
