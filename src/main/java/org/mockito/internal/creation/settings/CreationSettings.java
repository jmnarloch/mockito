/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.settings;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.mockito.listeners.InvocationListener;
import org.mockito.listeners.StubbingLookupListener;
import org.mockito.listeners.VerificationStartedListener;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.MockName;
import org.mockito.mock.MockType;
import org.mockito.mock.SerializableMode;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;

public class CreationSettings<T> implements MockCreationSettings<T>, Serializable {
    private static final long serialVersionUID = -6789800638070123629L;

    protected Class<T> typeToMock;
    protected transient Type genericTypeToMock;
    protected Set<Class<?>> extraInterfaces = new LinkedHashSet<>();
    protected String name;
    protected Object spiedInstance;
    protected Answer<Object> defaultAnswer;
    protected MockName mockName;
    protected SerializableMode serializableMode = SerializableMode.NONE;
    protected List<InvocationListener> invocationListeners = new ArrayList<>();

    // Other listeners in this class may also need concurrency-safe implementation. However, no
    // issue was reported about it.
    // If we do it, we need to understand usage patterns and choose the right concurrent
    // implementation.
    protected List<StubbingLookupListener> stubbingLookupListeners = new CopyOnWriteArrayList<>();

    protected List<VerificationStartedListener> verificationStartedListeners = new LinkedList<>();
    protected boolean stubOnly;
    protected boolean stripAnnotations;
    private boolean useConstructor;
    private Object outerClassInstance;
    private Object[] constructorArgs;
    protected Strictness strictness = null;
    protected String mockMaker;
    protected MockType mockType;

    public CreationSettings() { }

    @SuppressWarnings("unchecked")
    public CreationSettings(CreationSettings copy) {
        // TODO can we have a reflection test here? We had a couple of bugs here in the past.
        
    }

    @Override
    public Class<T> getTypeToMock() {
        
    }

    public CreationSettings<T> setTypeToMock(Class<T> typeToMock) {
        
    }

    public CreationSettings<T> setGenericTypeToMock(Type genericTypeToMock) {
        
    }

    @Override
    public Set<Class<?>> getExtraInterfaces() {
        
    }

    public CreationSettings<T> setExtraInterfaces(Set<Class<?>> extraInterfaces) {
        
    }

    public String getName() {
        
    }

    @Override
    public Object getSpiedInstance() {
        
    }

    @Override
    public Answer<Object> getDefaultAnswer() {
        
    }

    @Override
    public MockName getMockName() {
        
    }

    public CreationSettings<T> setMockName(MockName mockName) {
        
    }

    @Override
    public boolean isSerializable() {
        
    }

    public CreationSettings<T> setSerializableMode(SerializableMode serializableMode) {
        
    }

    @Override
    public SerializableMode getSerializableMode() {
        
    }

    @Override
    public List<InvocationListener> getInvocationListeners() {
        
    }

    @Override
    public List<VerificationStartedListener> getVerificationStartedListeners() {
        
    }

    @Override
    public List<StubbingLookupListener> getStubbingLookupListeners() {
        
    }

    @Override
    public boolean isUsingConstructor() {
        
    }

    @Override
    public boolean isStripAnnotations() {
        
    }

    @Override
    public Object[] getConstructorArgs() {
        
    }

    @Override
    public Object getOuterClassInstance() {
        
    }

    @Override
    public boolean isStubOnly() {
        
    }

    @Override
    public boolean isLenient() {
        
    }

    @Override
    public Strictness getStrictness() {
        
    }

    @Override
    public String getMockMaker() {
        
    }

    @Override
    public Type getGenericTypeToMock() {
        
    }

    @Override
    public MockType getMockType() {
        
    }

    public void setMockType(MockType mockType) {
        
    }
}
