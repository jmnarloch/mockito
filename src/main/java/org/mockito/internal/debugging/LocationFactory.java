/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.debugging;

import org.mockito.invocation.Location;

public final class LocationFactory {
    private static final Factory factory = createLocationFactory();

    private LocationFactory() { }

    public static Location create() {
        
    }

    public static Location create(boolean inline) {
        
    }

    private interface Factory {
        Location create(boolean inline);
    }

    private static Factory createLocationFactory() {
        
    }

    private static final class Java8LocationFactory implements Factory {
        @Override
        public Location create(boolean inline) {
            
        }
    }

    private static final class DefaultLocationFactory implements Factory {
        @Override
        public Location create(boolean inline) {
            
        }
    }
}
