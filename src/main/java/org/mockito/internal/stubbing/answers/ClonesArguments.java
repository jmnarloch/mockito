/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import java.lang.reflect.Array;

import org.mockito.creation.instance.Instantiator;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

// TODO this needs documentation and further analysis - what if someone changes the answer?
// we might think about implementing it straight on MockSettings
public class ClonesArguments implements Answer<Object> {
    @Override
    public Object answer(InvocationOnMock invocation) throws Throwable {
        
    }
}
