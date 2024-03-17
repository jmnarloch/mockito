/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import static org.mockito.internal.util.StringUtil.join;

import java.util.Collections;
import java.util.List;

import org.mockito.MockedConstruction;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.debugging.LocationFactory;
import org.mockito.invocation.Location;
import org.mockito.plugins.MockMaker;

public final class MockedConstructionImpl<T> implements MockedConstruction<T> {

    private final MockMaker.ConstructionMockControl<T> control;

    private boolean closed;

    private final Location location = LocationFactory.create();

    protected MockedConstructionImpl(MockMaker.ConstructionMockControl<T> control) {
        
    }

    @Override
    public List<T> constructed() {
        
    }

    @Override
    public boolean isClosed() {
        
    }

    @Override
    public void close() {
        
    }

    @Override
    public void closeOnDemand() {
        
    }

    private void assertNotClosed() {
        
    }
}
