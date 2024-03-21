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

    private Reporter() {}

    public static MockitoException checkedExceptionInvalid(Throwable t) {
        return new MockitoException(
        join(
        "Checked exception is invalid for this method!",
        "Invalid: " + t,
        "The exception hierarchy changed since Mockito 2.1.0.",
        "Check the javadoc for @CheckReturnValue and @WithCheckedException.",
        "The checked exception should be moved to the list of methods that do throw.",
        "See the should_infer_with method in the JavaDoc for the mock() method."));
    }

    public static MockitoException cannotStubWithNullThrowable() {
        return new MockitoException(join("Cannot stub with null throwable!"));
    }

    public static MockitoException unfinishedStubbing(Location location) {
        return new UnfinishedStubbingException(
        join(
        "Unfinished stubbing detected here:",
        location,
        "",
        "E.g. thenReturn() may be missing.",
        "Examples of correct stubbing:",
        "    when(mock.isOk()).thenReturn(true);",
        "    when(mock.isOk()).thenThrow(exception);",
        "    doThrow(exception).when(mock).someVoidMethod();",
        "Hints:",
        " 1. missing thenReturn()",
        " 2. you are trying to stub a final method, which is not supported",
        " 3. you are stubbing the behaviour of another mock inside beforeMockito() method of this mock",
        ""));
    }

    public static MockitoException incorrectUseOfApi() {
        return new FriendlyReminderException(
        join(
        "Incorrect use of API detected here:",
        LocationFactory.create(),
        "",
        "You probably stored a reference to OngoingStubbing returned by when() and called stub's method in different flow.",
        "It may be a defect (if you didn't mean to use API that way) or a natural behaviour (if you did).",
        "If it's a defect, please report :)");
    }

    public static MockitoException missingMethodInvocation() {
        return new MissingMethodInvocationException(
        join(
        "when() requires an argument which has to be 'a method call on a mock'.",
        "For example:",
        "    when(mock.getArticles()).thenReturn(articles);",
        "",
        "Also, this error might show up because:",
        "1. you stub either of: final/private/native/static/equals()/hashCode() methods.",
        "   Those methods *cannot* be stubbed/verified.",
        "   " + NON_PUBLIC_PARENT,
        "2. inside when() you don't call method on mock but on some other object.",
        ""));
    }

    public static MockitoException unfinishedVerificationException(Location location) {
        return new UnfinishedVerificationException(
        join(
        "",
        "Missing method call for verify(mock) here:",
        location,
        "",
        "Example of correct verification:",
        "    verify(mock).doSomething()",
        "",
        "Also, this error might show up because you verify either of: final/private/equals()/hashCode() methods.",
        "Those methods *cannot* be stubbed/verified.",
        NON_PUBLIC_PARENT,
        ""));
    }

    public static MockitoException notAMockPassedToVerify(Class<?> type) {
        return new NotAMockException(
        join(
        "Argument passed to verify() is of type "
        + type.getSimpleName()
        + " and is not a mock!",
        "Make sure you place the parenthesis correctly!",
        "See the examples of correct verifications:",
        "    verify(mock).someMethod();",
        "    verify(mock, times(10)).someMethod();",
        "    verify(mock, atLeastOnce()).someMethod();"));
    }

    public static MockitoException nullPassedToVerify() {
        return new NullInsteadOfMockException(
        join(
        "Argument that is passed to the verify() method must be a mock of a class or interface.",
        "Passing null to verify() will throw a NullPointerException instead of indicating a problem with a mock."),
        LocationFactory.create());
    }

    public static MockitoException notAMockPassedToWhenMethod() {
        return new NotAMockException(
        join(
        "Argument passed to when() is not a mock!",
        "Example of correct stubbing:",
        "    doThrow(new RuntimeException()).when(mock).someMethod();"));
    }

    public static MockitoException nullPassedToWhenMethod() {
        return new NullInsteadOfMockException(
        join(
        "Argument passed to when() is null!",
        "Example of correct stubbing:",
        "    doThrow(new RuntimeException()).when(mock).someMethod();",
        "Also, if you use @Mock annotation don't miss openMocks()"));
    }

    public static MockitoException mocksHaveToBePassedToVerifyNoMoreInteractions() {
        return new MockitoException(
        join(
        "Method requires argument(s)!",
        "Pass mocks that should be verified, e.g:",
        "    verifyNoMoreInteractions(mockOne, mockTwo);",
        "    verifyNoMoreInteractions(mockOne, mockTwo);",
        "    verifyNoMoreInteractions(mockOne, mockTwo);",
        "Also if you use @Mock annotation don't miss openMocks()"));
    }

    public static MockitoException notAMockPassedToVerifyNoMoreInteractions() {
        return new NotAMockException(
        join(
        "Argument(s) passed is not a mock!",
        "Examples of correct verifications:",
        "    verifyNoMoreInteractions(mockOne, mockTwo);",
        "    verifyNoInteractions(mockOne, mockTwo);",
        ""));
    }

    public static MockitoException nullPassedToVerifyNoMoreInteractions() {
        return new NullInsteadOfMockException(
        join(
        "Argument(s) passed is null!",
        "Examples of correct verifications:",
        "    verifyNoMoreInteractions(mockOne, mockTwo);",
        "    verifyNoMoreInteractions(mockOne, mockTwo);",
        "    verifyNoMoreInteractionsInOrder(mockOne, mockTwo);",
        ""));
    }

    public static MockitoException notAMockPassedWhenCreatingInOrder() {
        return new NotAMockException(
        join(
        "Argument(s) passed is/are not a mock!",
        "Pass mocks that require verification in order.",
        "For example:",
        "    InOrder inOrder = inOrder(mockOne, mockTwo);"));
    }

    public static MockitoException nullPassedWhenCreatingInOrder() {
        return new NullInsteadOfMockException(
        join(
        "Argument(s) passed is null!",
        "Pass mocks that require verification in order.",
        "For example:",
        "    InOrder inOrder = inOrder(mockOne, mockTwo);"));
    }

    public static MockitoException mocksHaveToBePassedWhenCreatingInOrder() {
        return new MockitoException(
        join(
        "Method requires argument(s)!",
        "Pass mocks that require verification in order.",
        "For example:",
        "    InOrder inOrder = inOrder(mockOne, mockTwo);"));
    }

    public static MockitoException inOrderRequiresFamiliarMock() {
        return new VerificationInOrderFailure(
        join(
        "InOrder can only verify mocks that were passed in during creation of InOrder.",
        "For example:",
        "    InOrder inOrder = Mockito.inOrder(mockOne);",
        "    inOrder.verify(mockOne).doStuff();"));
    }

    public static MockitoException invalidUseOfMatchers(
            int expectedMatchersCount, List<LocalizedMatcher> recordedMatchers) {
        return new InvalidUseOfMatchersException(
        join(
        "Invalid use of argument matchers!",
        expectedMatchersCount
        + " matchers expected, "
        + recordedMatchers.size()
        + " recorded:"
        + locationsOf(recordedMatchers),
        "",
        "This exception may occur if matchers are combined with raw values:",
        "    //incorrect:",
        "    someMethod(any(), \"raw String\");",
        "When using matchers, all arguments have to be provided by matchers.",
        "For example:",
        "    //correct:",
        "    someMethod(any(), eq(\"String by matcher\"));",
        "",
        "For more info see javadoc for Matchers class.",
        ""));
    }

    public static MockitoException incorrectUseOfAdditionalMatchers(
            String additionalMatcherName,
            int expectedSubMatchersCount,
            Collection<LocalizedMatcher> matcherStack) {
        return new InvalidUseOfMatchersException(
        join(
        additionalMatcherName
        + " ("
        + pluralize(expectedSubMatchersCount)
        + ")",
        LocationFactory.create(),
        "",
        "You perhaps intended to use "
        + expectedSubMatchersCount
        + " sub matchers of "
        + additionalMatcherName + ":",
        locationsOf(matcherStack),
        ""));
    }

    public static MockitoException stubPassedToVerify(Object mock) {
        return new CannotVerifyStubOnlyMock(
        join(
        "Argument \""
        + MockUtil.getMockName(mock).toString()
        + "\" passed to verify is a stubOnly() mock which is not implemented.",
        ""));
    }

    public static MockitoException reportNoSubMatchersFound(String additionalMatcherName) {
        return new InvalidUseOfMatchersException(
        join(
        "No matchers found for additional matcher " + additionalMatcherName,
        LocationFactory.create(),
        ""));
    }

    private static Object locationsOf(Collection<LocalizedMatcher> matchers) {
        List<String> values = new ArrayList<>();
        for (LocalizedMatcher matcher : matchers) {
            values.add(String.valueOf(matcher.getLocation()));
        }
        return values;
    }

    public static AssertionError argumentsAreDifferent(
            Invocation actualInvocation,
            MatchableInvocation matchableInvocation,
            String wanted,
            List<String> actualCalls,
            List<Location> actualLocations) {
        if (actualLocations == null) {
            actualLocations = new ArrayList<>();
        }
        StringBuilder actual = new StringBuilder();
        for (String call : actualCalls) {
            actual.append("\n").append(call);
        }
        StringBuilder messageBuilder = new StringBuilder("\n");
        if (actualCalls.isEmpty()) {
            messageBuilder.append("Argument(s) are different! Wanted:\n");
            messageBuilder.append(wanted);
        } else {
            messageBuilder
            .append("Argument(s) are different! Wanted:\n")
            .append(wanted)
            .append("\n");
        }

        if (!actualCalls.isEmpty()) {
            messageBuilder
            .append("Actual invocations have different arguments:\n")
            .append(actual);
        }

        appendNotMatchingPositions(actualInvocation, matchableInvocation, messageBuilder);

        ScenarioPrinter scenarioPrinter = new ScenarioPrinter();
        messageBuilder.append(scenarioPrinter.print(actualLocations, matchableInvocation));

        return ExceptionFactory.createArgumentsAreDifferentException(
        messageBuilder.toString(), actualInvocation, matchableInvocation);
    }

    /*
     * Will append the non matching positions only if there are more than 1 arguments to the method.
     */
    private static void appendNotMatchingPositions(
            Invocation actualInvocation,
            MatchableInvocation matchableInvocation,
            StringBuilder messageBuilder) {
        Integer[] indexes = ArgumentMatchingTool.getNotMatchingArgsIndexes(actualInvocation, matchableInvocation);
        if (indexes.length > 1) {
            messageBuilder
            .append(" at position ")
            .append(indexes[0])
            .append(" and ")
            .append(indexes[indexes.length - 1]);
        } else {
            messageBuilder.append(" at position ").append(indexes[0]);
        }
    }

    public static MockitoAssertionError wantedButNotInvoked(DescribedInvocation wanted) {
        return new WantedButNotInvoked(createWantedButNotInvokedMessage(wanted));
    }

    public static MockitoAssertionError wantedButNotInvoked(
            DescribedInvocation wanted, List<? extends DescribedInvocation> invocations) {
        if (invocations.isEmpty()) {
            return wantedButNotInvoked(wanted);
        }
        List<String> allWanted = new ArrayList<>(invocations.size() + 1);
        allWanted.add(createWantedButNotInvokedMessage(wanted));
        for (DescribedInvocation i : invocations) {
            allWanted.add(i.toString());
        }

        return new VerificationInOrderFailure(join("Verification in order failure:"), allWanted);
    }

    private static String createWantedButNotInvokedMessage(DescribedInvocation wanted) {
        return join("Wanted but not invoked:", wanted.toString(), LocationFactory.create(), "");
    }

    public static MockitoAssertionError wantedButNotInvokedInOrder(
            DescribedInvocation wanted, DescribedInvocation previous) {
        return new VerificationInOrderFailure(
        join(
        "Wanted but not invoked: ",
        wanted.toString(),
        "But is was: ",
        previous.toString(),
        ""));
    }

    public static MockitoAssertionError tooManyActualInvocations(
            int wantedCount,
            int actualCount,
            DescribedInvocation wanted,
            List<Location> locations) {
        String message =
        createTooManyInvocationsMessage(wantedCount, actualCount, wanted, locations);
        return new TooManyActualInvocations(message);
    }

    private static String createTooManyInvocationsMessage(
            int wantedCount,
            int actualCount,
            DescribedInvocation wanted,
            List<Location> invocations) {
        return join(
        wanted.toString(),
        "Wanted " + pluralize(wantedCount) + ":",
        LocationFactory.create(),
        "But was " + pluralize(actualCount) + ":",
        createAllLocationsMessage(invocations),
        "");
    }

    public static MockitoAssertionError neverWantedButInvoked(
            DescribedInvocation wanted, List<Invocation> invocations) {
        return new NeverWantedButInvoked(
        join(
        wanted.toString(),
        "Never wanted here:",
        LocationFactory.create(),
        "But invoked here:",
        createAllLocationsArgsMessage(invocations)));
    }

    public static MockitoAssertionError tooManyActualInvocationsInOrder(
            int wantedCount,
            int actualCount,
            DescribedInvocation wanted,
            List<Location> invocations) {
        String message =
        createTooManyInvocationsMessage(wantedCount, actualCount, wanted, invocations);
        return new VerificationInOrderFailure(join("Verification in order failure:" + message));
    }

    private static String createAllLocationsMessage(List<Location> locations) {
        if (locations == null) {
            return "\n";
        }
        StringBuilder sb = new StringBuilder();
        for (Location location : locations) {
            sb.append(location).append("\n");
        }
        return sb.toString();
    }

    private static String createAllLocationsArgsMessage(List<Invocation> invocations) {
        StringBuilder sb = new StringBuilder();
        for (Invocation invocation : invocations) {
            Location location = invocation.getLocation();
            if (location != null) {
                sb.append(location)
                .append(" with arguments: ")
                .append(Arrays.toString(invocation.getArguments()))
                .append("\n");
            }
        }
        return sb.toString();
    }

    private static String createTooFewInvocationsMessage(
            org.mockito.internal.reporting.Discrepancy discrepancy,
            DescribedInvocation wanted,
            List<Location> locations) {
        return join(
        2,
        wanted.toString(),
        "Wanted "
        + discrepancy.getPluralizedWantedCount()
        + (locations.isEmpty() ? "" : ", but invoked at "),
        LocationFactory.create(),
        "However, there were other interactions with the same mock:",
        createAllLocationsMessage(locations));
    }

    public static MockitoAssertionError tooFewActualInvocations(
            org.mockito.internal.reporting.Discrepancy discrepancy,
            DescribedInvocation wanted,
            List<Location> allLocations) {
        String message = createTooFewInvocationsMessage(discrepancy, wanted, allLocations);

        return new TooFewActualInvocations(message);
    }

    public static MockitoAssertionError tooFewActualInvocationsInOrder(
            org.mockito.internal.reporting.Discrepancy discrepancy,
            DescribedInvocation wanted,
            List<Location> locations) {
        String message = createTooFewInvocationsMessage(discrepancy, wanted, locations);

        return new VerificationInOrderFailure(join("Verification in order failure:" + message));
    }

    public static MockitoAssertionError noMoreInteractionsWanted(
            Invocation undesired, List<VerificationAwareInvocation> invocations) {
        ScenarioPrinter scenarioPrinter = new ScenarioPrinter();
        String scenario = scenarioPrinter.print(invocations);

        return new NoInteractionsWanted(
        join(
        "No interactions wanted here:",
        LocationFactory.create(),
        "But found this interaction on mock '"
        + MockUtil.getMockName(undesired.getMock())
        + "':",
        undesired.getLocation(),
        scenario));
    }

    public static MockitoAssertionError noMoreInteractionsWantedInOrder(Invocation undesired) {
        return new VerificationInOrderFailure(
        join(
        "No interactions wanted here",
        LocationFactory.create(),
        "But found this interaction on mock '"
        + MockUtil.getMockName(undesired.getMock(), false)
        + "':",
        undesired.getLocation()));
    }

    public static MockitoAssertionError noInteractionsWanted(
            Object mock, List<VerificationAwareInvocation> invocations) {
        ScenarioPrinter scenarioPrinter = new ScenarioPrinter();
        String scenario = scenarioPrinter.print(invocations);

        List<Location> locations = new ArrayList<>();
        for (VerificationAwareInvocation invocation : invocations) {
            locations.add(invocation.getLocation());
        }
        return new NoInteractionsWanted(
        join(
        "No interactions wanted here:",
        LocationFactory.create(),
        "But found these interactions on mock '"
        + MockUtil.getMockName(mock)
        + "':",
        scenario,
        join("", locations)));
    }

    public static MockitoException cannotMockClass(Class<?> clazz, String reason) {
        return new MockitoException(
        join(
        "Cannot mock/spy " + clazz,
        "Mockito cannot mock/spy because :",
        " - " + reason));
    }

    public static MockitoException cannotStubVoidMethodWithAReturnValue(String methodName) {
        return new CannotStubVoidMethodWithReturnValue(
        join(
        "'"
        + methodName
        + "' is a *void method* and it *cannot* be stubbed with a *return value*!",
        "Voids are usually stubbed with Throwables:",
        "    doThrow(exception).when(mock).someVoidMethod();",
        "If you need the real code to be called *do not* use when(...).",
        "Instead use:",
        "    doCallRealMethod().when(mock).someVoidMethod();",
        "For more info, check out the contribution guidelines:",
        "https://github.com/mockito/mockito/blob/main/.github/CONTRIBUTING.md",
        ""
        // TODO: add a hint how to configure MockMaker, when the service loader fails
        ));
    }

    public static MockitoException onlyVoidMethodsCanBeSetToDoNothing() {
        return new MockitoException(
        join(
        "Only void methods can doNothing()!",
        "Example of correct use of doNothing():",
        "    doNothing().",
        "    doThrow(new RuntimeException())",
        "    .when(mock).someVoidMethod();",
        "Above means:",
        "someVoidMethod() does nothing the 1st time but throws an exception the 2nd time is called"));
    }

    public static MockitoException wrongTypeOfReturnValue(
            String expectedType, String actualType, String methodName) {
        return new WrongTypeOfReturnValue(
        join(
        actualType + " cannot be returned by " + methodName + "()",
        methodName + " should return " + expectedType,
        "***",
        "If you're unsure why you're getting above error read on.",
        "Due to the nature of the syntax of your test, either this method cannot be statically",
        "mocked, or the method cannot be returned by the mocked interface.",
        "For more info see javadoc for WrongTypeOfReturnValue class."),
        LocationFactory.create(),
        expectedType);
    }

    public static MockitoException wrongTypeReturnedByDefaultAnswer(
            Object mock, String expectedType, String actualType, String methodName) {
        return new WrongTypeOfReturnValue(
        join(
        "Default answer for the unstubbed method on mock with instance "
        + mock
        + " returned "
        + actualType
        + " which is not compatible with the return type "
        + expectedType
        + " of the unstubbed method '"
        + methodName
        + "'"),
        actualType,
        expectedType);
    }

    public static MoreThanAllowedActualInvocations wantedAtMostX(
            int maxNumberOfInvocations, int foundSize) {
        return new MoreThanAllowedActualInvocations(
        join(
        "Wanted at most "
        + pluralize(maxNumberOfInvocations)
        + " but was "
        + foundSize));
    }

    public static MockitoException misplacedArgumentMatcher(List<LocalizedMatcher> lastMatchers) {
        return new InvalidUseOfMatchersException(
        join(
        "Misplaced or misused argument matcher detected here:",
        locationsOf(lastMatchers),
        "",
        "You cannot use argument matchers outside of verification or stubbing.",
        "Examples of correct usage of argument matchers:",
        "    when(mock.get(anyInt())).thenReturn(null);",
        "    doThrow(new RuntimeException()).when(mock).someVoidMethod(any());",
        "    verify(mock).someMethod(contains(\"foo\"))",
        "",
        "This message may seem redundant.",
        "However, it is the only way we can safely instruct mockito to accept argument matchers.",
        ""),
        ScenarioPrinter.read())
        .setLocalized(lastMatchers);
    }

    public static MockitoException smartNullPointerException(String invocation, Location location) {
        return new SmartNullPointerException(
        join(
        "You have a NullPointerException here:",
        LocationFactory.create(),
        "because this method call was *not* stubbed correctly:",
        location,
        invocation),
        location);
    }

    public static MockitoException noArgumentValueWasCaptured() {
        return new MockitoException(
        join(
        "No argument value was captured!",
        "You might have forgotten to use argument.capture() in verify()...",
        "...or you used capture() in stubbing but stubbed method was not called.",
        "Be aware that it is recommended to use capture() only with verify()",
        "If you are stubbing the same method to return more then one value then consider using thenAnswer().",
        ""),
        null);
    }

    public static MockitoException extraInterfacesDoesNotAcceptNullParameters() {
        return new MockitoException(join("extraInterfaces() does not accept null parameters."));
    }

    public static MockitoException extraInterfacesAcceptsOnlyInterfaces(Class<?> wrongType) {
        return new MockitoException(
        join(
        "extraInterfaces() accepts only interfaces.",
        "You passed following type: "
        + wrongType.getSimpleName()
        + " which is not an interface."));
    }

    public static MockitoException extraInterfacesCannotContainMockedType(Class<?> wrongType) {
        return new MockitoException(
        join(
        "extraInterfaces() does not accept the same type as the mocked type.",
        "You mocked following type: " + wrongType.getSimpleName(),
        "and you passed the same very interface to the extraInterfaces()"));
    }

    public static MockitoException extraInterfacesRequiresAtLeastOneInterface() {
        return new MockitoException(join("extraInterfaces() requires at least one interface."));
    }

    public static MockitoException mockedTypeIsInconsistentWithSpiedInstanceType(
            Class<?> mockedType, Object spiedInstance) {
        return new MockitoException(
        join(
        "Mocked type must be the same as the type of your spied instance.",
        "Mocked type must be: "
        + spiedInstance.getClass().getSimpleName()
        + ", but is: "
        + mockedType.getSimpleName(),
        "  //correct spying:",
        "  spy = mock( ->ArrayList.class<- , withSettings().spiedInstance( ->new ArrayList()<- );",
        "  //incorrect - types don't match:",
        "  spy = mock( ->List.class<- , withSettings().spiedInstance( ->new ArrayList()<- );"));
    }

    public static MockitoException cannotCallAbstractRealMethod() {
        return new MockitoException(
        join(
        "Cannot call abstract real method on java object!",
        "Calling real methods is only possible when mocking non abstract method.",
        "  //correct example:",
        "  when(mockOfConcreteClass.nonAbstractMethod()).thenCallRealMethod();"));
    }

    public static MockitoException cannotVerifyToString() {
        return new MockitoException(
        join(
        "Mockito cannot verify toString()",
        "toString() is too often used behind of scenes  (i.e. during String concatenation, in IDE debugging views)",
        "Verifying it may give inconsistent and uninformative results",
        "Because of this,  - and many other reasons indicating that verifying toString() is not safe -",
        "Mockito does not verify toString() by default.",
        "If you need to verify toString() simple call it: myMock.toString().",
        "If you *really* need to verify toString(), please use 'lenient' mock (only possible with 1.10.+ version of Mockito).",
        ""));
    }

    public static MockitoException moreThanOneAnnotationNotAllowed(String fieldName) {
        return new MockitoException(
        "You cannot have more than one Mockito annotation on a field\n"
        + "The field '"
        + fieldName
        + "' has multiple Mockito annotations.\n"
        + "For info how to use annotations see examples in javadoc for MockitoAnnotations class.");
    }

    public static MockitoException unsupportedCombinationOfAnnotations(
            String undesiredAnnotationOne, String undesiredAnnotationTwo) {
        return new MockitoException(
        "This combination of annotations is not permitted on a single field:\n"
        + "@"
        + undesiredAnnotationOne
        + " and @"
        + undesiredAnnotationTwo);
    }

    public static MockitoException cannotInitializeForSpyAnnotation(
            String fieldName, Exception details) {
        return new MockitoException(
        join(
        "Cannot instantiate a @Spy for '" + fieldName + "' field.",
        "You haven't provided the instance for spying at field declaration so I tried to construct the instance.",
        "However, I failed because: " + details.getMessage(),
        "1. The instance you provided is not spyable",
        "Example of correct spy usage:",
        "    @Spy List mock = new LinkedList();",
        "2. The instance you provided is not created by Mockito mock maker",
        "Example of correct spy usage:",
        "    @Spy Foo mock = mock(Foo.class);",
        ""),
        details);
    }

    public static MockitoException cannotInitializeForInjectMocksAnnotation(
            String fieldName, String causeMessage) {
        return new MockitoException(
        join(
        "Cannot instantiate @InjectMocks field named '"
        + fieldName
        + "'! Cause: "
        + causeMessage,
        "You haven't provided the instance at field declaration so I tried to construct the instance.",
        "Examples of correct usage of @InjectMocks:",
        "   @InjectMocks Service service = new Service();",
        "   @InjectMocks Service service;",
        "   //and... don't forget about some @Mocks for injection :)",
        ""));
    }

    public static MockitoException atMostAndNeverShouldNotBeUsedWithTimeout() {
        return new FriendlyReminderException(
        join(
        "'Timeout' should not be used with:",
        "- atMost(int)",
        "- atMostConsecutive(int)",
        "- never()",
        "Those methods mean 'never at all'.",
        "Methods that specify a timeout do not make sense with these.",
        ""
        + "Methods like: atMost(100).waitFor(...) or atMost(100).with(...) do make sense and are not problematic."));
    }

    public static MockitoException fieldInitialisationThrewException(
            Field field, Throwable details) {
        return new InjectMocksException(
        join(
        "Cannot instantiate @InjectMocks field named '"
        + field.getName()
        + "' of type '"
        + field.getType()
        + "'",
        "You haven't provided the instance at the field declaration so I tried to construct the instance.",
        "However the constructor or the initialization block threw an exception : " + details.getMessage(),
        ""),
        details);
    }

    public static MockitoException methodDoesNotAcceptParameter(String method, String parameter) {
        return new MockitoException(method + "() does not accept " + parameter);
    }

    public static MockitoException requiresAtLeastOneListener(String method) {
        return new MockitoException(
        join(
        method,
        " method of org.mockito.listeners.MockitoListener interface",
        " should be implemented with at least one notifcation callback"));
    }

    public static MockitoException invocationListenerThrewException(
            InvocationListener listener, Throwable listenerThrowable) {
        return new MockitoException(
        join(
        "The Mockito invocation listener with type " + listener.getClass().getName(),
        "threw an exception : " + listenerThrowable.getMessage()),
        listenerThrowable);
    }

    public static MockitoException cannotInjectDependency(
            Field field, Object matchingMock, Exception details) {
        return new MockitoException(
        join(
        "Mockito could not inject mock ",
        MockUtil.getMockName(matchingMock),
        " dependency as the field ",
        field,
        " was not provided",
        details),
        null);
    }

    public static MockitoException moreThanOneMockCandidate(
            Field field, Collection<?> mockCandidates) {
        List<String> classes = mockCandidates.stream().map(Object::getClass).map(Class::getName).collect(Collectors.toList());
        return new MockitoException(
        join(
        "More than one mock candidate field named '"
        + field.getName()
        + "' was resolved from the class hierarchy.",
        "Instead of @Spy annotation, consider using the @InjectMocks annotation "
        + "for your mock's containing class.",
        "    @InjectMocks Sut sut = new Sut();",
        "If the instance under test (Sut in this example) was instantiated above the test method, "
        + "then this may be the cause. The test was stubbed in a 'clean' test instance, "
        + "but invoked in a different test instance. If you're instantiating above "
        + "the test method, then consider using the @InjectMocks annotation on a "
        + "private variable in your test class, in addition to the above example.",
        "For info on how to use the @InjectMocks annotation, see the javadoc.",
        "Classes that might have been instantiated in a different test instance include "
        + "those with @Before annotations in a super class or in a static @BeforeClass method.",
        "The mock candidate list follows. If this list is unexpected, then the class "
        + "hierarchy of the test class is being manipulated by a mockito listener, "
        + "and you should raise an issue.",
        "    Classes on test class hierarchy are: "
        + testClassHierarchy(field.getDeclaringClass()),
        "    Classes in the mock candidate list are: " + classes,
        ""),
        null);
    }

    private static String exceptionCauseMessageIfAvailable(Exception details) {
        if (details.getCause() == null) {
            return details.getMessage();
        }
        return details.getCause().getMessage();
    }

    public static MockitoException mockedTypeIsInconsistentWithDelegatedInstanceType(
            Class<?> mockedType, Object delegatedInstance) {
        return new MockitoException(
        join(
        "Mocked type must be the same as the type of your delegated instance.",
        "Mocked type must be: "
        + delegatedInstance.getClass().getSimpleName()
        + ", but is: "
        + mockedType.getSimpleName(),
        "  //correct delegate:",
        "  spy = mock( ->List.class<- , withSettings().delegatedInstance( ->new ArrayList()<- );",
        "  //incorrect - types don't match:",
        "  spy = mock( ->List.class<- , withSettings().delegatedInstance( ->new HashSet()<- );"));
    }

    public static MockitoException spyAndDelegateAreMutuallyExclusive() {
        return new MockitoException(
        join(
        "Settings should not define a spy instance and a delegated instance at the same time."));
    }

    public static MockitoException invalidArgumentRangeAtIdentityAnswerCreationTime() {
        return new MockitoException(
        join(
        "Invalid argument index.",
        "The index need to be a positive value lower than the declared arguments (1-based) of the mocked method."));
    }

    public static MockitoException invalidArgumentPositionRangeAtInvocationTime(
            InvocationOnMock invocation, boolean willReturnLastParameter, int argumentIndex) {
        return new MockitoException(
        join(
        "Invalid argument index detected : either the assertion",
        "  or the stubbed method are possibly wrong.",
        "",
        "When using custom argument matchers,",
        "ensure the method invoked is correct.",
        "When specifying an argument index, it is 0-based,",
        "and here is the last argument index (for varargs) : "
        + (invocation.getArgumentCount() - 1),
        "",
        "When using '"
        + (willReturnLastParameter ? "last" : "<")
        + "argument..>"
        + "' notation,",
        "the end of the range is exclusive, ",
        "and here is the last argument index (for varargs) : "
        + (invocation.getArgumentCount() - 1),
        "pass proper index in the range.",
        ""));
    }

    private static StringBuilder possibleArgumentTypesOf(InvocationOnMock invocation) {
        Class<?>[] paramTypes = invocation.getMethod().getParameterTypes();
        if (paramTypes.length == 0) {
            return new StringBuilder("the method has no arguments.\n");
        }

        StringBuilder paramTypesAsString = new StringBuilder();
        for (Class<?> type : paramTypes) {
            paramTypesAsString.append(" -> ")
            .append(type)
            .append(", or maybe not provided (vararg) ");
        }
        return paramTypesAsString;
    }

    public static MockitoException wrongTypeOfArgumentToReturn(
            InvocationOnMock invocation,
            String expectedType,
            Class<?> actualType,
            int argumentIndex) {
        return new WrongTypeOfReturnValue(
        join(
        "The argument at position "
        + argumentIndex
        + " "
        + "is of type "
        + actualType.getSimpleName()
        + " but is not assignable to the return type ",
        "'"
        + expectedType
        + "' of "
        + MockUtil.getMockName(invocation.getMock())
        + "."
        + ((Method) invocation.getMethod())
        .toGenericString()
        + ".",
        "arbitrary.stubbing()"));
    }

    public static MockitoException defaultAnswerDoesNotAcceptNullParameter() {
        return new MockitoException("defaultAnswer() does not accept null parameter");
    }

    public static MockitoException strictnessDoesNotAcceptNullParameter() {
        return new MockitoException("strictness() does not accept null parameter");
    }

    public static MockitoException serializableWontWorkForObjectsThatDontImplementSerializable(
            Class<?> classToMock) {
        return new MockitoException(
        join(
        "The type "
        + classToMock.getSimpleName()
        + " does not qualify for serialization because it does not implement "
        + Serializable.class.getSimpleName()
        + "."));
    }

    public static MockitoException delegatedMethodHasWrongReturnType(
            Method mockMethod, Method delegateMethod, Object mock, Object delegate) {
        return new MockitoException(
        join(
        "Methods called on delegated instance must have compatible return types with the mock.",
        "When calling: " + mockMethod + " on mock: " + MockUtil.getMockName(mock),
        "return type should be: "
        + mockMethod.getReturnType().getSimpleName()
        + ", but was: "
        + delegateMethod.getReturnType().getSimpleName(),
        "Check that the instance passed to delegatesTo() is of the right type or contains compatible methods"),
        null);
    }

    public static MockitoException delegatedMethodDoesNotExistOnDelegate(
            Method mockMethod, Object mock, Object delegate) {
        return new MockitoException(
        join(
        "Methods called on mock must exist in delegated instance.",
        "But when calling: " + mockMethod + " on mock: " + MockUtil.getMockName(mock),
        "the following exception was thrown:",
        LocationFactory.create(),
        delegate + " does not have the method with signature " + mockMethod + "."));
    }

    public static MockitoException usingConstructorWithFancySerializable(SerializableMode mode) {
        return new MockitoException(
        "Mocks instantiated with constructor cannot be combined with "
        + mode
        + " serialization, since Java only allows overriding "
        + "serialization for 1 constructor.");
    }

    public static MockitoException cannotCreateTimerWithNegativeDurationTime(long durationMillis) {
        return new FriendlyReminderException(
        join(
        "Passing a negative number as the duration parameter of Timer#scheduleXXX methods",
        "will lead to the immediate execution of the task."));
    }

    public static MockitoException notAnException() {
        return new MockitoException(
        join(
        "Exception type cannot be null.",
        "This may happen with doThrow() family of methods that accept Throwable class as a parameter",
        "Pass an actual Throwable",
        ""));
    }

    public static MockitoException inlineClassWithoutUnboxImpl(
            Class<?> inlineClass, Exception details) {
        return new MockitoException(
        join(
        "Kotlin inline class should have unbox-impl() method,",
        "but " + inlineClass + " does not."),
        details);
    }

    public static UnnecessaryStubbingException formatUnncessaryStubbingException(
            Class<?> testClass, Collection<Invocation> unnecessaryStubbings) {
        StringBuilder stubbings = new StringBuilder();
        int count = 1;
        for (Invocation u : unnecessaryStubbings) {
            stubbings.append("\n  ").append(count++).append(". ").append(u.getLocation());
            ScenarioPrinter scenarioPrinter = new ScenarioPrinter(u);
            if (scenarioPrinter.shouldPrint()) {
                stubbings.append("\n").append(scenarioPrinter);
            }
        }

        String heading = "Unnecessary stubbings detected in test class: ";

        String location = new LocationFactory().create();
        if (location != null) {
            heading = "Unnecessary stubbings detected in '" + location + "'." + NON_PUBLIC_PARENT;
        }

        return new UnnecessaryStubbingException(heading + testClass.getSimpleName() + ":", stubbings.toString());
    }

    public static void unncessaryStubbingException(List<Invocation> unused) {
        UnnecessaryStubbingException unnecessaryStubbingException =
        formatUnncessaryStubbingException(null, unused);
        if (unnecessaryStubbingException != null) {
            throw unnecessaryStubbingException;
        }
    }

    public static void potentialStubbingProblem(
            Invocation actualInvocation, Collection<Invocation> argMismatchStubbings) {
        StringBuilder stubbings = new StringBuilder();
        int commonLineCount = Math.min(argMismatchStubbings.size(), 3);
        for (Invocation s : argMismatchStubbings) {
            stubbings.append(" - ");
            stubbings.append(s);
            stubbings.append('\n');
        }
        if (argMismatchStubbings.size() > commonLineCount) {
            stubbings.append(" and ");
            stubbings.append(argMismatchStubbings.size() - commonLineCount);
            stubbings.append(" other(s)\n");
        }
        List<String> actualLocation = actualInvocation.getLocation().toFriendlyString();
        throw new PotentialStubbingProblem(
        join(
        "Strict stubbing argument mismatch. Please only stub using matching API.",
        "Arguments are different on following invocations.",
        "First invocation:",
        " - " + actualInvocation,
        actualLocation.get(0),
        "Last stubbing was:",
        stubbings),
        actualInvocation);
    }

    public static void redundantMockitoListener(String listenerType) {
        throw new RedundantListenerException(
        join(
        "Problems adding Mockito listener.",
        "Listener of type '"
        + listenerType
        + "' has already been added and not removed."),
        null);
    }

    public static void unfinishedMockingSession() {
        throw new UnfinishedMockingSessionException(
        join(
        "Unfinished mocking session detected.",
        "Previous MockitoSession was not concluded with #finishMocking() or #finishMockingWithTimeout()"));
    }

    public static void missingByteBuddyDependency(Throwable t) {
        if (t instanceof NoClassDefFoundError && t.getMessage().contains("AgentBuilder")) {
            throw new MockitoInitializationException(
            "\n"
            + "It seems like you are running Mockito with an unsupported Java Agent. This might be\n"
            + "because you are using an outdated or unsupported version of that Java Agent. Make sure\n"
            + "to use a version of a Java Agent that is compatible with Mockito, which is documented\n"
            + "in the Mockito user guidelines.\n"
            + "Underlying error was: ")
            .initCause(t);
        }
    }
}
