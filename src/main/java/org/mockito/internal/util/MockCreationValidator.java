/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import static org.mockito.internal.exceptions.Reporter.cannotMockClass;
import static org.mockito.internal.exceptions.Reporter.extraInterfacesCannotContainMockedType;
import static org.mockito.internal.exceptions.Reporter.mockedTypeIsInconsistentWithDelegatedInstanceType;
import static org.mockito.internal.exceptions.Reporter.mockedTypeIsInconsistentWithSpiedInstanceType;
import static org.mockito.internal.exceptions.Reporter.usingConstructorWithFancySerializable;

import java.util.Collection;

import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker.TypeMockability;

@SuppressWarnings("unchecked")
public class MockCreationValidator {

    public void validateType(Class<?> classToMock, String mockMaker) {
        
    }

    public void validateExtraInterfaces(
            Class<?> classToMock, Collection<Class<?>> extraInterfaces) {
        
    }

    public void validateMockedType(Class<?> classToMock, Object spiedInstance) {
        
    }

    public void validateDelegatedInstance(Class<?> classToMock, Object delegatedInstance) {
        
    }

    public void validateConstructorUse(boolean usingConstructor, SerializableMode mode) {
        
    }
}
