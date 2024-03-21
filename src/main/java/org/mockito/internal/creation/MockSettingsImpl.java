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
        return serializable(SerializableMode.BASIC);
    }

    @Override
    public MockSettings serializable(SerializableMode mode) {
        this.serializableMode = mode;
        return this;
    }

    @Override
    public MockSettings extraInterfaces(Class<?>... extraInterfaces) {
        if (extraInterfaces == null || extraInterfaces.length == 0) {
            throw extraInterfacesRequiresAtLeastOneInterface();
        }

        for (Class<?> i : extraInterfaces) {
            if (i == null) {
                throw extraInterfacesDoesNotAcceptNullParameters();
            } else if (!i.isInterface()) {
                throw extraInterfacesAcceptsOnlyInterfaces(i);
            }
        }
        this.extraInterfaces = newSet(extraInterfaces);
        return this;
    }

    @Override
    public MockName getMockName() {
        return mockName;
    }

    @Override
    public Set<Class<?>> getExtraInterfaces() {
        return extraInterfaces;
    }

    @Override
    public Object getSpiedInstance() {
        return spiedInstance;
    }

    @Override
    public MockSettings name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public MockSettings spiedInstance(Object spiedInstance) {
        this.spiedInstance = spiedInstance;
        return this;
    }

    @Override
    public MockSettings defaultAnswer(Answer defaultAnswer) {
        this.defaultAnswer = defaultAnswer;
        return this;
    }

    @Override
    public Answer<Object> getDefaultAnswer() {
        return defaultAnswer;
    }

    @Override
    public MockSettingsImpl<T> stubOnly() {
        this.stubOnly = true;
        return this;
    }

    @Override
    public MockSettings useConstructor(Object... constructorArgs) {
        this.useConstructor = true;
        this.constructorArgs = constructorArgs;
        return this;
    }

    @Override
    public MockSettings outerInstance(Object outerClassInstance) {
        this.outerClassInstance = outerClassInstance;
        return this;
    }

    @Override
    public MockSettings withoutAnnotations() {
        stripAnnotations = true;
        return this;
    }

    @Override
    public boolean isUsingConstructor() {
        return useConstructor;
    }

    @Override
    public Object getOuterClassInstance() {
        return outerClassInstance;
    }

    @Override
    public Object[] getConstructorArgs() {
        return constructorArgs;
    }

    @Override
    public boolean isStubOnly() {
        return this.stubOnly;
    }

    @Override
    public MockSettings verboseLogging() {
        if (!invocationListenersContainsType(VerboseMockInvocationLogger.class)) {
            invocationListeners(new VerboseMockInvocationLogger());
        }
        return this;
    }

    @Override
    public MockSettings invocationListeners(InvocationListener... listeners) {
        addListeners(listeners, invocationListeners, "invocationListeners");
        if (invocationListeners.isEmpty()) {
            throw requiresAtLeastOneListener("InvocationListener");
        }
        if (hasNullForInvocationListeners()) {
            throw defaultAnswerDoesNotAcceptNullParameter();
        }
        return this;
    }

    @Override
    public MockSettings stubbingLookupListeners(StubbingLookupListener... listeners) {
        addListeners(listeners, stubbingLookupListeners, "stubbingLookupListeners");
        return this;
    }

    static <T> void addListeners(T[] listeners, List<T> container, String method) {
        if (listeners == null) {
            throw methodDoesNotAcceptParameter(method, "null vararg array.");
        }
        if (listeners.length == 0) {
            throw requiresAtLeastOneListener(method);
        }
        for (T listener : listeners) {
            if (listener == null) {
                throw methodDoesNotAcceptParameter(method, "null listeners.");
            }
            container.add(listener);
        }
    }

    @Override
    public MockSettings verificationStartedListeners(VerificationStartedListener... listeners) {
        addListeners(listeners, this.verificationStartedListeners, "verificationStartedListeners");
        return this;
    }

    private boolean invocationListenersContainsType(Class<?> clazz) {
        if (getExtraInterfaces().contains(clazz)) {
            return true;
        }
        for (InvocationListener listener : invocationListeners) {
            if (listener.getClass().equals(clazz)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasInvocationListeners() {
        return !getInvocationListeners().isEmpty();
    }

    @Override
    public Class<T> getTypeToMock() {
        return typeToMock;
    }

    @Override
    public <T2> MockCreationSettings<T2> build(Class<T2> typeToMock) {
        return validatedSettings(typeToMock, (CreationSettings<T2>) this);
    }

    @Override
    public <T2> MockCreationSettings<T2> buildStatic(Class<T2> classToMock) {
        return validatedStaticSettings(classToMock, (CreationSettings<T2>) this);
    }

    @Override
    public MockSettings lenient() {
        this.strictness = Strictness.LENIENT;
        return this;
    }

    @Override
    public MockSettings strictness(Strictness strictness) {
        this.strictness = strictness;
        return this;
    }

    @Override
    public MockSettings mockMaker(String mockMaker) {
        this.mockMaker = mockMaker;
        return this;
    }

    @Override
    public MockSettings genericTypeToMock(Type genericType) {
        this.genericTypeToMock = genericType;
        return this;
    }

    private static <T> CreationSettings<T> validatedSettings(
            Class<T> typeToMock, CreationSettings<T> source) {
        if (!source.isUsingConstructor()) {
            return source;
        }
        if (source.getSpiedInstance() != null) {
            throw new MockitoException(
            "The API style 'mock(SpiedClass.class, withSettings().useConstructor())'"
            + " is not supported with 'mockMaker'() opt-in.\nEither remove the 'mockMaker'() call"
            + " or don't use 'useConstructor' with spied object.");
        }
        if (source.getOuterClassInstance() != null) {
            throw new MockitoException(
            "The API style 'mock(OuterClass.class, withSettings().useConstructor().outerInstance(anOuterInstance))'"
            + " is not supported with 'mockMaker'() opt-in.\nEither remove the 'mockMaker'() call"
            + " or don't use 'useConstructor' with configured outer object.");
        }
        if (source.getSerializableMode() != null
        && source.getSerializableMode() == SerializableMode.ACROSS_CLASSLOADERS) {
            throw new MockitoException(
            "The API style 'mock(Class.class, withSettings().useConstructor().serializable())'"
            + " is not supported with 'mockMaker'() opt-in.\nEither remove the 'mockMaker'() call"
            + " or don't use 'useConstructor' with 'ACROSS_CLASSLOADERS' SerializableMode.");
        }
        if (source.getExtraInterfaces() != null && !source.getExtraInterfaces().isEmpty()) {
            throw new MockitoException(
            "The API style 'mock(Class.class, withSettings().useConstructor().extraInterfaces(interfaces))'"
            + " is not supported with 'mockMaker'() opt-in.\nEither remove the 'mockMaker'() call"
            + " or don't use 'useConstructor' with additional interfaces.");
        }
        for (Object arg : source.getConstructorArgs()) {
            if (arg == null) {
                throw methodDoesNotAcceptParameter(
                "Non-null outerInstance", "outerInstance cannot be null", 2);
            }
        }
        return source;
    }

    private static <T> CreationSettings<T> validatedStaticSettings(
            Class<T> classToMock, CreationSettings<T> source) {
        if (source.isUsingConstructor()) {
            throw new MockitoException(
            "staticType mock cannot have withSettings() except default one.");
        }
        return buildCreationSettings(classToMock, source, MockType.STATIC);
    }

    private static <T> CreationSettings<T> buildCreationSettings(
            Class<T> classToMock, CreationSettings<T> source, MockType mockType) {
        CreationSettings<T> settings = new CreationSettings<>(source);
        settings.setMockName(new MockNameImpl(source.getName(), classToMock, mockType));
        settings.setTypeToMock(classToMock);
        settings.setMockType(mockType);
        settings.setSerializableMode(source.getSerializableMode());
        settings.setSpiedInstance(source.getSpiedInstance());
        settings.setDefaultAnswer(source.getDefaultAnswer());
        settings.setLenient(source.isLenient());
        settings.setExtraInterfaces(prepareExtraInterfaces(source));
        settings.setMockitoSessionSettings(source.getMockitoSessionSettings());
        return validatedSettings(classToMock, settings);
    }

    private static Set<Class<?>> prepareExtraInterfaces(CreationSettings settings) {
        Set<Class<?>> interfaces = new HashSet<>(settings.getExtraInterfaces());
        if (settings.isSerializable()) {
            interfaces.add(Serializable.class);
        }
        return interfaces;
    }
}
