/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.listeners;

import java.util.Collection;
import java.util.List;

import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.invocation.Invocation;
import org.mockito.listeners.StubbingLookupEvent;
import org.mockito.listeners.StubbingLookupListener;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stubbing.Stubbing;

public final class StubbingLookupNotifier {

    public static void notifyStubbedAnswerLookup(
            Invocation invocation,
            Stubbing stubbingFound,
            Collection<Stubbing> allStubbings,
            CreationSettings creationSettings) {
        
    }

    static class Event implements StubbingLookupEvent {
        private final Invocation invocation;
        private final Stubbing stubbing;
        private final Collection<Stubbing> allStubbings;
        private final MockCreationSettings mockSettings;

        public Event(
                Invocation invocation,
                Stubbing stubbing,
                Collection<Stubbing> allStubbings,
                MockCreationSettings mockSettings) {
            
        }

        @Override
        public Invocation getInvocation() {
            
        }

        @Override
        public Stubbing getStubbingFound() {
            
        }

        @Override
        public Collection<Stubbing> getAllStubbings() {
            
        }

        @Override
        public MockCreationSettings getMockSettings() {
            
        }
    }

    private StubbingLookupNotifier() { }
}
