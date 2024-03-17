/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import java.io.Serializable;
import java.util.concurrent.Callable;

import org.mockito.internal.exceptions.stacktrace.ConditionalStackTraceFilter;
import org.mockito.invocation.InvocationFactory;
import org.mockito.invocation.InvocationOnMock;

/**
 * Interface that wraps a 'real' method of the mock object.
 * Needed for test spies or {@link InvocationOnMock#callRealMethod()}.
 */
public interface RealMethod extends Serializable {

    enum IsIllegal implements RealMethod {
        INSTANCE;

        @Override
        public boolean isInvokable() { }

        @Override
        public Object invoke() {
            
        }
    }

    class FromCallable extends FromBehavior implements RealMethod {
        public FromCallable(final Callable<?> callable) {
            
        }
    }

    class FromBehavior implements RealMethod {

        private final InvocationFactory.RealMethodBehavior<?> behavior;

        FromBehavior(InvocationFactory.RealMethodBehavior<?> behavior) {
            
        }

        @Override
        public boolean isInvokable() { }

        @Override
        public Object invoke() throws Throwable {
            
        }
    }

    boolean isInvokable();

    Object invoke() throws Throwable;
}
