/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

import static org.mockito.internal.exceptions.Reporter.unfinishedStubbing;
import static org.mockito.internal.exceptions.Reporter.unfinishedVerificationException;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.debugging.Localized;
import org.mockito.internal.debugging.LocationFactory;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.internal.listeners.AutoCleanableListener;
import org.mockito.invocation.Location;
import org.mockito.listeners.MockCreationListener;
import org.mockito.listeners.MockitoListener;
import org.mockito.listeners.VerificationListener;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.verification.VerificationMode;
import org.mockito.verification.VerificationStrategy;

@SuppressWarnings("unchecked")
public class MockingProgressImpl implements MockingProgress {

    private final ArgumentMatcherStorage argumentMatcherStorage = new ArgumentMatcherStorageImpl();

    private OngoingStubbing<?> ongoingStubbing;
    private Localized<VerificationMode> verificationMode;
    private Location stubbingInProgress = null;
    private VerificationStrategy verificationStrategy;
    private final Set<MockitoListener> listeners = new LinkedHashSet<>();

    public MockingProgressImpl() {
        
    }

    public static VerificationStrategy getDefaultVerificationStrategy() {
        
    }

    @Override
    public void reportOngoingStubbing(OngoingStubbing ongoingStubbing) {
        
    }

    @Override
    public OngoingStubbing<?> pullOngoingStubbing() {
        
    }

    @Override
    public Set<VerificationListener> verificationListeners() {
        
    }

    @Override
    public void verificationStarted(VerificationMode verify) {
        
    }

    /**
     * (non-Javadoc)
     *
     * @see org.mockito.internal.progress.MockingProgress#resetOngoingStubbing()
     */
    public void resetOngoingStubbing() {
        
    }

    @Override
    public VerificationMode pullVerificationMode() {
        
    }

    @Override
    public void stubbingStarted() {
        
    }

    @Override
    public void validateState() {
        
    }

    private void validateMostStuff() {
        // State is cool when GlobalConfiguration is already loaded
        // this cannot really be tested functionally because I cannot dynamically mess up
        // org.mockito.configuration.MockitoConfiguration class
        
    }

    @Override
    public void stubbingCompleted() {
        
    }

    @Override
    public String toString() {
        
    }

    @Override
    public void reset() {
        
    }

    @Override
    public ArgumentMatcherStorage getArgumentMatcherStorage() {
        
    }

    @Override
    public void mockingStarted(Object mock, MockCreationSettings settings) {
        
    }

    @Override
    public void mockingStarted(Class<?> mock, MockCreationSettings settings) {
        
    }

    @Override
    public void addListener(MockitoListener listener) {
        
    }

    static void addListener(MockitoListener listener, Set<MockitoListener> listeners) {
        
    }

    @Override
    public void removeListener(MockitoListener listener) {
        
    }

    @Override
    public void setVerificationStrategy(VerificationStrategy strategy) {
        
    }

    @Override
    public VerificationMode maybeVerifyLazily(VerificationMode mode) {
        
    }

    @Override
    public void clearListeners() {
        
    }

    /*

    //TODO 545 thread safety of all mockito

    use cases:
       - single threaded execution throughout
       - single threaded mock creation, stubbing & verification, multi-threaded interaction with mock
       - thread per test case

    */
}
