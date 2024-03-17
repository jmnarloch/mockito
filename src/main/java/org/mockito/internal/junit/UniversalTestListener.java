/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

import java.util.Collection;
import java.util.IdentityHashMap;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.listeners.AutoCleanableListener;
import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.MockitoLogger;
import org.mockito.quality.Strictness;

/**
 * Universal test listener that behaves accordingly to current setting of strictness.
 * Will come handy when we offer tweaking strictness at the method level with annotation.
 * Should be relatively easy to improve and offer tweaking strictness per mock.
 */
public class UniversalTestListener implements MockitoTestListener, AutoCleanableListener {

    private Strictness currentStrictness;
    private final MockitoLogger logger;

    private IdentityHashMap mocks = new IdentityHashMap<Object, MockCreationSettings>();
    private final DefaultStubbingLookupListener stubbingLookupListener;
    private boolean listenerDirty;

    public UniversalTestListener(Strictness initialStrictness, MockitoLogger logger) {
        
    }

    @Override
    public void testFinished(TestFinishedEvent event) {
        
    }

    private void reportUnusedStubs(TestFinishedEvent event, Collection<Object> mocks) {
        // If there is some other failure (or mismatches were detected) don't report another
        // exception to avoid confusion
        
    }

    private static void emitWarnings(
            MockitoLogger logger, TestFinishedEvent event, Collection<Object> mocks) {
        
    }

    @Override
    public void onMockCreated(Object mock, MockCreationSettings settings) {
        
    }

    public void setStrictness(Strictness strictness) {
        
    }

    /**
     * See {@link AutoCleanableListener#isListenerDirty()}
     */
    @Override
    public boolean isListenerDirty() {
        
    }

    /**
     * Marks listener as dirty, scheduled for cleanup when the next session starts
     */
    public void setListenerDirty() {
        
    }
}
