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
        
    }

    public <T> MockedStatic<T> mockStatic(Class<T> classToMock, MockSettings settings) {
        
    }

    private void checkDoNotMockAnnotation(MockCreationSettings<?> creationSettings) {
        
    }

    public <T> MockedConstruction<T> mockConstruction(
            Class<T> typeToMock,
            Function<MockedConstruction.Context, ? extends MockSettings> settingsFactory,
            MockedConstruction.MockInitializer<T> mockInitializer) {
        
    }

    public <T> OngoingStubbing<T> when(T methodCall) {
        
    }

    public <T> T verify(T mock, VerificationMode mode) {
        
    }

    public <T> void reset(T... mocks) {
        
    }

    public <T> void clearInvocations(T... mocks) {
        
    }

    public void verifyNoMoreInteractions(Object... mocks) {
        
    }

    public void verifyNoInteractions(Object... mocks) {
        
    }

    public void verifyNoMoreInteractionsInOrder(List<Object> mocks, InOrderContext inOrderContext) {
        
    }

    private void assertMocksNotEmpty(Object[] mocks) {
        
    }

    private void assertNotStubOnlyMock(Object mock) {
        
    }

    public InOrder inOrder(Object... mocks) {
        
    }

    public Stubber stubber() {
        
    }

    public Stubber stubber(Strictness strictness) {
        
    }

    public void validateMockitoUsage() {
        
    }

    /**
     * For testing purposes only. Is not the part of main API.
     *
     * @return last invocation
     */
    public Invocation getLastInvocation() {
        
    }

    public Object[] ignoreStubs(Object... mocks) {
        
    }

    public MockingDetails mockingDetails(Object toInspect) {
        
    }

    public LenientStubber lenient() {
        
    }

    public void clearAllCaches() {
        
    }
}
