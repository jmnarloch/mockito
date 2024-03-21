/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import static org.mockito.internal.exceptions.Reporter.missingMethodInvocation;
import static org.mockito.internal.exceptions.Reporter.mocksHaveToBePassedToVerifyNoMoreInteractions;
import static org.mockito.internal.exceptions.Reporter.mocksHaveToBePassedWhenCreatingInOrder;
import static org.mockito.internal.exceptions.Reporter.notAMockPassedToVerify;
import static org.mockito.internal.exceptions.Reporter.notAMockPassedToVerifyNoMoreInteractions;
import static org.mockito.internal.exceptions.Reporter.notAMockPassedWhenCreatingInOrder;
import static org.mockito.internal.exceptions.Reporter.nullPassedToVerify;
import static org.mockito.internal.exceptions.Reporter.nullPassedToVerifyNoMoreInteractions;
import static org.mockito.internal.exceptions.Reporter.nullPassedWhenCreatingInOrder;
import static org.mockito.internal.exceptions.Reporter.stubPassedToVerify;
import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;
import static org.mockito.internal.util.MockUtil.createConstructionMock;
import static org.mockito.internal.util.MockUtil.createMock;
import static org.mockito.internal.util.MockUtil.createStaticMock;
import static org.mockito.internal.util.MockUtil.getInvocationContainer;
import static org.mockito.internal.util.MockUtil.getMockHandler;
import static org.mockito.internal.util.MockUtil.isMock;
import static org.mockito.internal.util.MockUtil.resetMock;
import static org.mockito.internal.verification.VerificationModeFactory.noInteractions;
import static org.mockito.internal.verification.VerificationModeFactory.noMoreInteractions;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.mockito.InOrder;
import org.mockito.MockSettings;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.MockingDetails;
import org.mockito.exceptions.misusing.DoNotMockException;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.finder.VerifiableInvocationsFinder;
import org.mockito.internal.listeners.VerificationStartedNotifier;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.stubbing.DefaultLenientStubber;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubberImpl;
import org.mockito.internal.util.DefaultMockingDetails;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.verification.MockAwareVerificationMode;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.internal.verification.api.VerificationDataInOrder;
import org.mockito.internal.verification.api.VerificationDataInOrderImpl;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.DoNotMockEnforcerWithType;
import org.mockito.plugins.MockMaker;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.LenientStubber;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.verification.VerificationMode;

@SuppressWarnings("unchecked")
public class MockitoCore {

    private static final DoNotMockEnforcerWithType DO_NOT_MOCK_ENFORCER =
            Plugins.getDoNotMockEnforcer();

    public <T> T mock(Class<T> typeToMock, MockSettings settings) {
        checkDoNotMockAnnotation(Plugins.getMockMaker().createMockType(typeToMock, settings));
        return MockUtil.createMock(typeToMock, settings);
    }

    public <T> MockedStatic<T> mockStatic(Class<T> classToMock, MockSettings settings) {
        if (!MockSettingsImpl.class.isInstance(settings)) {
            throw new IllegalArgumentException(
            "Unexpected implementation of '"
            + settings.getClass().getCanonicalName()
            + "' : this is should be handled by Mockito team");
        }
        MockSettingsImpl impl = MockSettingsImpl.class.cast(settings);
        MockCreationSettings<T> creationSettings = impl.buildStatic(classToMock);
        checkDoNotMockAnnotation(creationSettings);
        MockMaker.StaticMockControl<T> control = createStaticMock(classToMock, creationSettings);
        control.enable();
        mockingProgress().mockingStarted(classToMock, creationSettings);
        return new MockedStaticImpl<>(control);
    }

    private void checkDoNotMockAnnotation(MockCreationSettings<?> creationSettings) {
        String warning = DO_NOT_MOCK_ENFORCER.checkTypeForDoNotMock(creationSettings.getTypeToMock());
        if (warning != null) {
            throw new DoNotMockException(
            warning + "\n" + "Strictness of WhensMocks: " + creationSettings.getStrictness());
        }
    }

    public <T> MockedConstruction<T> mockConstruction(
            Class<T> typeToMock,
            Function<MockedConstruction.Context, ? extends MockSettings> settingsFactory,
            MockedConstruction.MockInitializer<T> mockInitializer) {
        Function<MockedConstruction.Context, MockCreationSettings<T>> creationSettings =
        input -> {
            MockSettings value = settingsFactory.apply(input);
            if (!(value instanceof MockCreationSettings)) {
                throw new IllegalArgumentException(
                "Unexpected implementation of 'MockSettings' is passed : "
                + value.getClass().getCanonicalName());
            }
            return (MockCreationSettings) value;
        };
        return createConstructionMock(typeToMock, creationSettings, mockInitializer);
    }

    public <T> OngoingStubbing<T> when(T methodCall) {
        mockingProgress().stubbingStarted();
        return new OngoingStubbingImpl();
    }

    public <T> T verify(T mock, VerificationMode mode) {
        if (mock == null) {
            throw nullPassedToVerify();
        }
        MockingDetails mockingDetails = mockingDetails(mock);
        if (!mockingDetails.isMock()) {
            throw notAMockPassedToVerify(mock.getClass());
        }
        assertNotStubOnlyMock(mock);
        MockHandler handler = mockingDetails.getMockHandler();
        mock =
        (T)
        VerificationStartedNotifier.notifyVerificationStarted(
        handler.getMockSettings().getVerificationStartedListeners(),
        mockingDetails);
        MockingProgress mockingProgress = mockingProgress();
        mockingProgress.verificationStarted(mode);
        mockingProgress.validateState();
        return mock;
    }

    public <T> void reset(T... mocks) {
        assertMocksNotEmpty(mocks);
        MockingProgress mockingProgress = mockingProgress();
        mockingProgress.validateState();
        mockingProgress.reset();
        for (T m : mocks) {
            resetMock(m);
        }
    }

    public <T> void clearInvocations(T... mocks) {
        MockingProgress mockingProgress = mockingProgress();
        mockingProgress.validateState();
        mockingProgress.reset();
        for (T m : mocks) {
            getInvocationContainer(m).clearInvocations();
        }
    }

    public void verifyNoMoreInteractions(Object... mocks) {
        assertMocksNotEmpty(mocks);
        mockingProgress().validateState();
        for (Object mock : mocks) {
            try {
                if (mock == null) {
                    throw nullPassedToVerifyNoMoreInteractions();
                }
                InvocationContainerImpl invocations = getInvocationContainer(mock);
                assertNotStubOnlyMock(mock);
                VerificationDataImpl data = new VerificationDataImpl(invocations, null);
                noMoreInteractions().verify(data);
            } catch (NotAMockException e) {
                throw notAMockPassedToVerifyNoMoreInteractions();
            }
        }
    }

    public void verifyNoInteractions(Object... mocks) {
        assertMocksNotEmpty(mocks);
        MockingProgress mockingProgress = mockingProgress();
        mockingProgress.validateState();
        VerificationDataImpl data =
        new VerificationDataImpl(
        mockingProgress.verificationStarted(
        VerificationModeFactory.noInteractions()));
        for (Object mock : mocks) {
            getInvocationContainer(mock).verifyNoInteractions();
        }
        mockingProgress.verificationFinished(data, new NoInteractionsVerificationMode());
    }

    public void verifyNoMoreInteractionsInOrder(List<Object> mocks, InOrderContext inOrderContext) {
        mockingProgress().validateState();
        VerificationDataInOrder data =
        new VerificationDataInOrderImpl(
        inOrderContext, VerifiableInvocationsFinder.find(mocks), null);
        VerificationModeFactory.noMoreInteractions().verifyInOrder(data);
    }

    private void assertMocksNotEmpty(Object[] mocks) {
        if (mocks == null) {
            throw mocksHaveToBePassedToVerifyNoMoreInteractions();
        }
        if (mocks.length == 0) {
            throw noMocksToInteractWith();
        }
    }

    private void assertNotStubOnlyMock(Object mock) {
        if (getMockHandler(mock).getMockSettings().isStubOnly()) {
            throw stubPassedToVerify(mock);
        }
    }

    public InOrder inOrder(Object... mocks) {
        if (mocks == null || mocks.length == 0) {
            throw mocksHaveToBePassedWhenCreatingInOrder();
        }
        for (Object mock : mocks) {
            if (mock == null) {
                throw nullPassedWhenCreatingInOrder();
            }
            if (!isMock(mock)) {
                throw notAMockPassedWhenCreatingInOrder();
            }
            assertNotStubOnlyMock(mock);
        }
        return new InOrderImpl(Arrays.asList(mocks));
    }

    public Stubber stubber() {
        return stubber(null);
    }

    public Stubber stubber(Strictness strictness) {
        MockingProgress mockingProgress = mockingProgress();
        mockingProgress.stubbingStarted();
        mockingProgress.resetOngoingStubbing();
        return new StubberImpl(strictness);
    }

    public void validateMockitoUsage() {
        mockingProgress().validateState();
    }

    /**
     * For testing purposes only. Is not the part of main API.
     *
     * @return last invocation
     */
    public Invocation getLastInvocation() {
        return mockingProgress().pullInvocation();
    }

    public Object[] ignoreStubs(Object... mocks) {
        MockingProgress mockingProgress = mockingProgress();
        mockingProgress.validateState();
        mockingProgress.ignoreStubs(mocks);
        return mocks;
    }

    public MockingDetails mockingDetails(Object toInspect) {
        return new DefaultMockingDetails(toInspect);
    }

    public LenientStubber lenient() {
        return new DefaultLenientStubber();
    }

    public void clearAllCaches() {
        MockUtil.clearAllCaches();
    }
}
