/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import java.io.Serializable;

import org.mockito.Mockito;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stubbing.Answer;

public class ReturnsMocks implements Answer<Object>, Serializable {

    private static final long serialVersionUID = -6755257986994634579L;
    private final Answer<Object> delegate = new ReturnsMoreEmptyValues();

    @Override
    public Object answer(final InvocationOnMock invocation) throws Throwable {
        
    }
}
