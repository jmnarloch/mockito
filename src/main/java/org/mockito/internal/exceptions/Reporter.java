/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.exceptions;

import static org.mockito.internal.reporting.Pluralizer.pluralize;
import static org.mockito.internal.reporting.Pluralizer.were_exactly_x_interactions;
import static org.mockito.internal.util.StringUtil.join;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.base.MockitoInitializationException;
import org.mockito.exceptions.misusing.CannotStubVoidMethodWithReturnValue;
import org.mockito.exceptions.misusing.CannotVerifyStubOnlyMock;
import org.mockito.exceptions.misusing.FriendlyReminderException;
import org.mockito.exceptions.misusing.InjectMocksException;
import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;
import org.mockito.exceptions.misusing.MissingMethodInvocationException;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.exceptions.misusing.NullInsteadOfMockException;
import org.mockito.exceptions.misusing.PotentialStubbingProblem;
import org.mockito.exceptions.misusing.RedundantListenerException;
import org.mockito.exceptions.misusing.UnfinishedMockingSessionException;
import org.mockito.exceptions.misusing.UnfinishedStubbingException;
import org.mockito.exceptions.misusing.UnfinishedVerificationException;
import org.mockito.exceptions.misusing.UnnecessaryStubbingException;
import org.mockito.exceptions.misusing.WrongTypeOfReturnValue;
import org.mockito.exceptions.verification.MoreThanAllowedActualInvocations;
import org.mockito.exceptions.verification.NeverWantedButInvoked;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.exceptions.verification.SmartNullPointerException;
import org.mockito.exceptions.verification.TooFewActualInvocations;
import org.mockito.exceptions.verification.TooManyActualInvocations;
import org.mockito.exceptions.verification.VerificationInOrderFailure;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockito.internal.debugging.LocationFactory;
import org.mockito.internal.exceptions.util.ScenarioPrinter;
import org.mockito.internal.junit.ExceptionFactory;
import org.mockito.internal.matchers.LocalizedMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.invocation.Location;
import org.mockito.invocation.MatchableInvocation;
import org.mockito.listeners.InvocationListener;
import org.mockito.mock.MockName;
import org.mockito.mock.SerializableMode;

/**
 * Reports verification and misusing errors.
 * <p>
 * One of the key points of mocking library is proper verification/exception
 * messages. All messages in one place makes it easier to tune and amend them.
 * <p>
 * Reporter can be injected and therefore is easily testable.
 * <p>
 * Generally, exception messages are full of line breaks to make them easy to
 * read (xunit plugins take only fraction of screen on modern IDEs).
 */
public class Reporter {

    private static final String NON_PUBLIC_PARENT =
            "Mocking methods declared on non-public parent classes is not supported.";

    private Reporter() { }

    public static MockitoException checkedExceptionInvalid(Throwable t) {
        
    }

    public static MockitoException cannotStubWithNullThrowable() {
        
    }

    public static MockitoException unfinishedStubbing(Location location) {
        
    }

    public static MockitoException incorrectUseOfApi() {
        
    }

    public static MockitoException missingMethodInvocation() {
        
    }

    public static MockitoException unfinishedVerificationException(Location location) {
        
    }

    public static MockitoException notAMockPassedToVerify(Class<?> type) {
        
    }

    public static MockitoException nullPassedToVerify() {
        
    }

    public static MockitoException notAMockPassedToWhenMethod() {
        
    }

    public static MockitoException nullPassedToWhenMethod() {
        
    }

    public static MockitoException mocksHaveToBePassedToVerifyNoMoreInteractions() {
        
    }

    public static MockitoException notAMockPassedToVerifyNoMoreInteractions() {
        
    }

    public static MockitoException nullPassedToVerifyNoMoreInteractions() {
        
    }

    public static MockitoException notAMockPassedWhenCreatingInOrder() {
        
    }

    public static MockitoException nullPassedWhenCreatingInOrder() {
        
    }

    public static MockitoException mocksHaveToBePassedWhenCreatingInOrder() {
        
    }

    public static MockitoException inOrderRequiresFamiliarMock() {
        
    }

    public static MockitoException invalidUseOfMatchers(
            int expectedMatchersCount, List<LocalizedMatcher> recordedMatchers) {
        
    }

    public static MockitoException incorrectUseOfAdditionalMatchers(
            String additionalMatcherName,
            int expectedSubMatchersCount,
            Collection<LocalizedMatcher> matcherStack) {
        
    }

    public static MockitoException stubPassedToVerify(Object mock) {
        
    }

    public static MockitoException reportNoSubMatchersFound(String additionalMatcherName) {
        
    }

    private static Object locationsOf(Collection<LocalizedMatcher> matchers) {
        
    }

    public static AssertionError argumentsAreDifferent(
            Invocation actualInvocation,
            MatchableInvocation matchableInvocation,
            String wanted,
            List<String> actualCalls,
            List<Location> actualLocations) {
        
    }

    /*
     * Will append the non matching positions only if there are more than 1 arguments to the method.
     */
    private static void appendNotMatchingPositions(
            Invocation actualInvocation,
            MatchableInvocation matchableInvocation,
            StringBuilder messageBuilder) {
        
    }

    public static MockitoAssertionError wantedButNotInvoked(DescribedInvocation wanted) {
        
    }

    public static MockitoAssertionError wantedButNotInvoked(
            DescribedInvocation wanted, List<? extends DescribedInvocation> invocations) {
        
    }

    private static String createWantedButNotInvokedMessage(DescribedInvocation wanted) {
        
    }

    public static MockitoAssertionError wantedButNotInvokedInOrder(
            DescribedInvocation wanted, DescribedInvocation previous) {
        
    }

    public static MockitoAssertionError tooManyActualInvocations(
            int wantedCount,
            int actualCount,
            DescribedInvocation wanted,
            List<Location> locations) {
        
    }

    private static String createTooManyInvocationsMessage(
            int wantedCount,
            int actualCount,
            DescribedInvocation wanted,
            List<Location> invocations) {
        
    }

    public static MockitoAssertionError neverWantedButInvoked(
            DescribedInvocation wanted, List<Invocation> invocations) {

        
    }

    public static MockitoAssertionError tooManyActualInvocationsInOrder(
            int wantedCount,
            int actualCount,
            DescribedInvocation wanted,
            List<Location> invocations) {
        
    }

    private static String createAllLocationsMessage(List<Location> locations) {
        
    }

    private static String createAllLocationsArgsMessage(List<Invocation> invocations) {
        
    }

    private static String createTooFewInvocationsMessage(
            org.mockito.internal.reporting.Discrepancy discrepancy,
            DescribedInvocation wanted,
            List<Location> locations) {
        
    }

    public static MockitoAssertionError tooFewActualInvocations(
            org.mockito.internal.reporting.Discrepancy discrepancy,
            DescribedInvocation wanted,
            List<Location> allLocations) {
        
    }

    public static MockitoAssertionError tooFewActualInvocationsInOrder(
            org.mockito.internal.reporting.Discrepancy discrepancy,
            DescribedInvocation wanted,
            List<Location> locations) {
        
    }

    public static MockitoAssertionError noMoreInteractionsWanted(
            Invocation undesired, List<VerificationAwareInvocation> invocations) {
        
    }

    public static MockitoAssertionError noMoreInteractionsWantedInOrder(Invocation undesired) {
        
    }

    public static MockitoAssertionError noInteractionsWanted(
            Object mock, List<VerificationAwareInvocation> invocations) {
        
    }

    public static MockitoException cannotMockClass(Class<?> clazz, String reason) {
        
    }

    public static MockitoException cannotStubVoidMethodWithAReturnValue(String methodName) {
        
    }

    public static MockitoException onlyVoidMethodsCanBeSetToDoNothing() {
        
    }

    public static MockitoException wrongTypeOfReturnValue(
            String expectedType, String actualType, String methodName) {
        
    }

    public static MockitoException wrongTypeReturnedByDefaultAnswer(
            Object mock, String expectedType, String actualType, String methodName) {
        
    }

    public static MoreThanAllowedActualInvocations wantedAtMostX(
            int maxNumberOfInvocations, int foundSize) {
        
    }

    public static MockitoException misplacedArgumentMatcher(List<LocalizedMatcher> lastMatchers) {
        
    }

    public static MockitoException smartNullPointerException(String invocation, Location location) {
        
    }

    public static MockitoException noArgumentValueWasCaptured() {
        
    }

    public static MockitoException extraInterfacesDoesNotAcceptNullParameters() {
        
    }

    public static MockitoException extraInterfacesAcceptsOnlyInterfaces(Class<?> wrongType) {
        
    }

    public static MockitoException extraInterfacesCannotContainMockedType(Class<?> wrongType) {
        
    }

    public static MockitoException extraInterfacesRequiresAtLeastOneInterface() {
        
    }

    public static MockitoException mockedTypeIsInconsistentWithSpiedInstanceType(
            Class<?> mockedType, Object spiedInstance) {
        
    }

    public static MockitoException cannotCallAbstractRealMethod() {
        
    }

    public static MockitoException cannotVerifyToString() {
        
    }

    public static MockitoException moreThanOneAnnotationNotAllowed(String fieldName) {
        
    }

    public static MockitoException unsupportedCombinationOfAnnotations(
            String undesiredAnnotationOne, String undesiredAnnotationTwo) {
        
    }

    public static MockitoException cannotInitializeForSpyAnnotation(
            String fieldName, Exception details) {
        
    }

    public static MockitoException cannotInitializeForInjectMocksAnnotation(
            String fieldName, String causeMessage) {
        
    }

    public static MockitoException atMostAndNeverShouldNotBeUsedWithTimeout() {
        
    }

    public static MockitoException fieldInitialisationThrewException(
            Field field, Throwable details) {
        
    }

    public static MockitoException methodDoesNotAcceptParameter(String method, String parameter) {
        
    }

    public static MockitoException requiresAtLeastOneListener(String method) {
        
    }

    public static MockitoException invocationListenerThrewException(
            InvocationListener listener, Throwable listenerThrowable) {
        
    }

    public static MockitoException cannotInjectDependency(
            Field field, Object matchingMock, Exception details) {
        
    }

    public static MockitoException moreThanOneMockCandidate(
            Field field, Collection<?> mockCandidates) {
        
    }

    private static String exceptionCauseMessageIfAvailable(Exception details) {
        
    }

    public static MockitoException mockedTypeIsInconsistentWithDelegatedInstanceType(
            Class<?> mockedType, Object delegatedInstance) {
        
    }

    public static MockitoException spyAndDelegateAreMutuallyExclusive() {
        
    }

    public static MockitoException invalidArgumentRangeAtIdentityAnswerCreationTime() {
        
    }

    public static MockitoException invalidArgumentPositionRangeAtInvocationTime(
            InvocationOnMock invocation, boolean willReturnLastParameter, int argumentIndex) {
        
    }

    private static StringBuilder possibleArgumentTypesOf(InvocationOnMock invocation) {
        
    }

    public static MockitoException wrongTypeOfArgumentToReturn(
            InvocationOnMock invocation,
            String expectedType,
            Class<?> actualType,
            int argumentIndex) {
        
    }

    public static MockitoException defaultAnswerDoesNotAcceptNullParameter() {
        
    }

    public static MockitoException strictnessDoesNotAcceptNullParameter() {
        
    }

    public static MockitoException serializableWontWorkForObjectsThatDontImplementSerializable(
            Class<?> classToMock) {
        
    }

    public static MockitoException delegatedMethodHasWrongReturnType(
            Method mockMethod, Method delegateMethod, Object mock, Object delegate) {
        
    }

    public static MockitoException delegatedMethodDoesNotExistOnDelegate(
            Method mockMethod, Object mock, Object delegate) {
        
    }

    public static MockitoException usingConstructorWithFancySerializable(SerializableMode mode) {
        
    }

    public static MockitoException cannotCreateTimerWithNegativeDurationTime(long durationMillis) {
        
    }

    public static MockitoException notAnException() {
        
    }

    public static MockitoException inlineClassWithoutUnboxImpl(
            Class<?> inlineClass, Exception details) {
        
    }

    public static UnnecessaryStubbingException formatUnncessaryStubbingException(
            Class<?> testClass, Collection<Invocation> unnecessaryStubbings) {
        
    }

    public static void unncessaryStubbingException(List<Invocation> unused) {
        
    }

    public static void potentialStubbingProblem(
            Invocation actualInvocation, Collection<Invocation> argMismatchStubbings) {
        
    }

    public static void redundantMockitoListener(String listenerType) {
        
    }

    public static void unfinishedMockingSession() {
        
    }

    public static void missingByteBuddyDependency(Throwable t) {
        
    }
}
