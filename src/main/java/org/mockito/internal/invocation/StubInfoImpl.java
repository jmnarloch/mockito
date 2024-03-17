/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import java.io.Serializable;

import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Location;
import org.mockito.invocation.StubInfo;

public class StubInfoImpl implements StubInfo, Serializable {
    private static final long serialVersionUID = 2125827349332068867L;
    private final DescribedInvocation stubbedAt;

    public StubInfoImpl(DescribedInvocation stubbedAt) {
        
    }

    @Override
    public Location stubbedAt() {
        
    }
}
