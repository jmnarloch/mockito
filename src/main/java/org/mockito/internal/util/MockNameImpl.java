/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import java.io.Serializable;

import org.mockito.mock.MockName;
import org.mockito.mock.MockType;

public class MockNameImpl implements MockName, Serializable {

    private static final long serialVersionUID = 8014974700844306925L;
    private final String mockName;
    private boolean defaultName;

    @SuppressWarnings("unchecked")
    public MockNameImpl(String mockName, Class<?> type, MockType mockType) {
        
    }

    public MockNameImpl(String mockName) {
        
    }

    private static String toInstanceName(Class<?> clazz) {
        
    }

    private static String toClassName(Class<?> clazz) {
        
    }

    @Override
    public boolean isDefault() {
        
    }

    @Override
    public String toString() {
        
    }
}
