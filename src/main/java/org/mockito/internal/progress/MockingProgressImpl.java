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
        this.verificationStrategy = getDefaultVerificationStrategy();
    }

    public static VerificationStrategy getDefaultVerificationStrategy() {
        return new VerificationStrategy() {
            @Override
            public VerificationMode maybeVerifyLazily(VerificationMode mode) {
                return mode;
            }
        };
    }

    @Override
    public void reportOngoingStubbing(OngoingStubbing ongoingStubbing) {
        this.ongoingStubbing = ongoingStubbing;
    }

    @Override
    public OngoingStubbing<?> pullOngoingStubbing() {
        OngoingStubbing<?> temp = ongoingStubbing;
        ongoingStubbing = null;
        return temp;
    }

    @Override
    public Set<VerificationListener> verificationListeners() {
        final LinkedHashSet<VerificationListener> verificationListeners = new LinkedHashSet<>();
        for (MockitoListener listener : listeners) {
            if (listener instanceof VerificationListener) {
                verificationListeners.add((VerificationListener) listener);
            }
        }
        return verificationListeners;
    }

    @Override
    public void verificationStarted(VerificationMode verify) {
        validateState();
        resetOngoingStubbing();
        verificationMode = new Localized(verify);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.mockito.internal.progress.MockingProgress#resetOngoingStubbing()
     */
    public void resetOngoingStubbing() {
        ongoingStubbing = null;
    }

    @Override
    public VerificationMode pullVerificationMode() {
        if (verificationMode == null) {
            return null;
        }

        VerificationMode temp = verificationMode.getPayload();
        verificationMode = null;
        return temp;
    }

    @Override
    public void stubbingStarted() {
        validateState();
        stubbingInProgress = LocationFactory.create();
    }

    @Override
    public void validateState() {
        validateMostStuff();
        verificationStarted(null);
    }

    private void validateMostStuff() {
        if (verificationMode != null) {
            Location location = verificationMode.getLocation();
            verificationMode = null;
            throw unfinishedVerificationException(location);
        }

        getArgumentMatcherStorage().validateState();
    }

    @Override
    public void stubbingCompleted() {
        validateMostStuff();
        stubbingInProgress = null;
    }

    @Override
    public String toString() {
        return "ongoingStubbing: "
        + ongoingStubbing
        + ", verificationMode: "
        + verificationMode
        + ", stubbingInProgress: "
        + stubbingInProgress;
    }

    @Override
    public void reset() {
        stubbingInProgress = null;
        getArgumentMatcherStorage().clear();
    }

    @Override
    public ArgumentMatcherStorage getArgumentMatcherStorage() {
        return argumentMatcherStorage;
    }

    @Override
    public void mockingStarted(Object mock, MockCreationSettings settings) {
        for (MockitoListener listener : listeners) {
            if (listener instanceof MockCreationListener) {
                ((MockCreationListener) listener).onMockCreated(mock, settings);
            }
        }
    }

    @Override
    public void mockingStarted(Class<?> mock, MockCreationSettings settings) {
        for (MockitoListener listener : listeners) {
            if (listener instanceof MockCreationListener) {
                ((MockCreationListener) listener).onMockCreated(mock, settings);
            }
        }
    }

    @Override
    public void addListener(MockitoListener listener) {
        addListener(listener, listeners);
    }

    static void addListener(MockitoListener listener, Set<MockitoListener> listeners) {
        for (MockitoListener l : listeners) {
            if (l.getClass().equals(listener.getClass())) {
                return;
            }
        }
        listeners.add(listener);
    }

    @Override
    public void removeListener(MockitoListener listener) {
        this.listeners.remove(listener);
    }

    @Override
    public void setVerificationStrategy(VerificationStrategy strategy) {
        this.verificationStrategy = strategy;
    }

    @Override
    public VerificationMode maybeVerifyLazily(VerificationMode mode) {
        return this.verificationStrategy.maybeVerifyLazily(mode);
    }

    @Override
    public void clearListeners() {
        listeners.clear();
    }

    /*

    //TODO 545 thread safety of all mockito

    use cases:
       - single threaded execution throughout
       - single threaded mock creation, stubbing & verification, multi-threaded interaction with mock
       - thread per test case

    */
}
