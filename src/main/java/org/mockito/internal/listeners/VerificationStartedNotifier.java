/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.listeners;

import java.util.List;
import java.util.Set;

import org.mockito.MockingDetails;
import org.mockito.Mockito;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.internal.matchers.text.ValuePrinter;
import org.mockito.listeners.VerificationStartedEvent;
import org.mockito.listeners.VerificationStartedListener;
import org.mockito.mock.MockCreationSettings;

public final class VerificationStartedNotifier {

    public static Object notifyVerificationStarted(
            List<VerificationStartedListener> listeners, MockingDetails originalMockingDetails) {
        
    }

    static class Event implements VerificationStartedEvent {
        private final MockingDetails originalMockingDetails;
        private Object mock;

        public Event(MockingDetails originalMockingDetails) {
            
        }

        @Override
        public void setMock(Object mock) {
            
        }

        @Override
        public Object getMock() {
            
        }
    }

    static void assertCompatibleTypes(Object mock, MockCreationSettings originalSettings) {
        
    }

    private VerificationStartedNotifier() { }
}
