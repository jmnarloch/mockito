/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation;

import static java.util.Arrays.asList;

import static org.mockito.internal.exceptions.Reporter.defaultAnswerDoesNotAcceptNullParameter;
import static org.mockito.internal.exceptions.Reporter.extraInterfacesAcceptsOnlyInterfaces;
import static org.mockito.internal.exceptions.Reporter.extraInterfacesDoesNotAcceptNullParameters;
import static org.mockito.internal.exceptions.Reporter.extraInterfacesRequiresAtLeastOneInterface;
import static org.mockito.internal.exceptions.Reporter.methodDoesNotAcceptParameter;
import static org.mockito.internal.exceptions.Reporter.requiresAtLeastOneListener;
import static org.mockito.internal.exceptions.Reporter.strictnessDoesNotAcceptNullParameter;
import static org.mockito.internal.util.collections.Sets.newSet;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mockito.MockSettings;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.debugging.VerboseMockInvocationLogger;
import org.mockito.internal.util.Checks;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockNameImpl;
import org.mockito.listeners.InvocationListener;
import org.mockito.listeners.StubbingLookupListener;
import org.mockito.listeners.VerificationStartedListener;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.MockName;
import org.mockito.mock.MockType;
import org.mockito.mock.SerializableMode;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;

@SuppressWarnings("unchecked")
public class MockSettingsImpl<T> extends CreationSettings<T>
        implements MockSettings, MockCreationSettings<T> {

    private static final long serialVersionUID = 4475297236197939569L;
    private boolean useConstructor;
    private Object outerClassInstance;
    private Object[] constructorArgs;

    @Override
    public MockSettings serializable() {
        
    }

    @Override
    public MockSettings serializable(SerializableMode mode) {
        
    }

    @Override
    public MockSettings extraInterfaces(Class<?>... extraInterfaces) {
        
    }

    @Override
    public MockName getMockName() {
        
    }

    @Override
    public Set<Class<?>> getExtraInterfaces() {
        
    }

    @Override
    public Object getSpiedInstance() {
        
    }

    @Override
    public MockSettings name(String name) {
        
    }

    @Override
    public MockSettings spiedInstance(Object spiedInstance) {
        
    }

    @Override
    public MockSettings defaultAnswer(Answer defaultAnswer) {
        
    }

    @Override
    public Answer<Object> getDefaultAnswer() {
        
    }

    @Override
    public MockSettingsImpl<T> stubOnly() {
        
    }

    @Override
    public MockSettings useConstructor(Object... constructorArgs) {
        
    }

    @Override
    public MockSettings outerInstance(Object outerClassInstance) {
        
    }

    @Override
    public MockSettings withoutAnnotations() {
        
    }

    @Override
    public boolean isUsingConstructor() {
        
    }

    @Override
    public Object getOuterClassInstance() {
        
    }

    @Override
    public Object[] getConstructorArgs() {
        
    }

    @Override
    public boolean isStubOnly() {
        
    }

    @Override
    public MockSettings verboseLogging() {
        
    }

    @Override
    public MockSettings invocationListeners(InvocationListener... listeners) {
        
    }

    @Override
    public MockSettings stubbingLookupListeners(StubbingLookupListener... listeners) {
        
    }

    static <T> void addListeners(T[] listeners, List<T> container, String method) {
        
    }

    @Override
    public MockSettings verificationStartedListeners(VerificationStartedListener... listeners) {
        
    }

    private boolean invocationListenersContainsType(Class<?> clazz) {
        
    }

    public boolean hasInvocationListeners() {
        
    }

    @Override
    public Class<T> getTypeToMock() {
        
    }

    @Override
    public <T2> MockCreationSettings<T2> build(Class<T2> typeToMock) {
        
    }

    @Override
    public <T2> MockCreationSettings<T2> buildStatic(Class<T2> classToMock) {
        
    }

    @Override
    public MockSettings lenient() {
        
    }

    @Override
    public MockSettings strictness(Strictness strictness) {
        
    }

    @Override
    public MockSettings mockMaker(String mockMaker) {
        
    }

    @Override
    public MockSettings genericTypeToMock(Type genericType) {
        
    }

    private static <T> CreationSettings<T> validatedSettings(
            Class<T> typeToMock, CreationSettings<T> source) {
        
    }

    private static <T> CreationSettings<T> validatedStaticSettings(
            Class<T> classToMock, CreationSettings<T> source) {

        
    }

    private static <T> CreationSettings<T> buildCreationSettings(
            Class<T> classToMock, CreationSettings<T> source, MockType mockType) {
        
    }

    private static Set<Class<?>> prepareExtraInterfaces(CreationSettings settings) {
        
    }
}
