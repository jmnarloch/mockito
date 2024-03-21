/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import static org.mockito.Mockito.withSettings;
import static org.mockito.internal.util.MockUtil.typeMockabilityOf;

import java.io.IOException;
import java.io.Serializable;

import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stubbing.Answer;

/**
 * Returning deep stub implementation.
 *
 * <p>Will return previously created mock if the invocation matches.
 *
 * <p>Supports nested generic information, with this answer you can write code like this :
 *
 * <pre class="code"><code class="java">
 *     interface GenericsNest&lt;K extends Comparable&lt;K&gt; & Cloneable&gt; extends Map&lt;K, Set&lt;Number&gt;&gt; {}
 *
 *     GenericsNest&lt;?&gt; mock = mock(GenericsNest.class, new ReturnsGenericDeepStubs());
 *     Number number = mock.entrySet().iterator().next().getValue().iterator().next();
 * </code></pre>
 * </p>
 *
 * <p>However this answer does not support generics information when the mock has been deserialized.
 *
 * @see org.mockito.Mockito#RETURNS_DEEP_STUBS
 * @see org.mockito.Answers#RETURNS_DEEP_STUBS
 */
public class ReturnsDeepStubs implements Answer<Object>, Serializable {

    private static final long serialVersionUID = -7105341425736035847L;

    @Override
    public Object answer(InvocationOnMock invocation) throws Throwable {
        GenericMetadataSupport returnTypeGenericMetadata =
        actualParameterizedType(invocation.getMock())
        .resolveGenericReturnType(invocation.getMethod());
        MockCreationSettings<?> mockSettings = MockUtil.getMockSettings(invocation.getMock());

        Class<?> rawType = returnTypeGenericMetadata.rawTypeOf(returnTypeGenericMetadata.depth());
        if (rawType == Serializable.class) {
            return delegate().answer(invocation);
        }

        if (typeMockabilityOf(rawType, mockSettings.getMockMaker()).mockCreate(
        mockito -> mockito
        .mock(
        rawType,
        withSettingsUsing(returnTypeGenericMetadata, mockSettings))
        .deepStub(),
        mockitoCore()::mockingProgress,
        returnTypeGenericMetadata,
        mockSettings.getMockMaker(),
        false)) {
            return deepStub(invocation, returnTypeGenericMetadata);
        } else {
            return delegate().answer(invocation);
        }
    }

    private Object deepStub(
            InvocationOnMock invocation, GenericMetadataSupport returnTypeGenericMetadata)
            throws Throwable {
        Answer answer = returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);
        return answer.answer(invocation);
    }

    /**
     * Creates a mock using the Generics Metadata.
     *
     * <li>Finally as we want to mock the actual type, but we want to pass along the contextual generics meta-data
     * that was resolved for the current return type, for this to happen we associate to the mock an new instance of
     * {@link ReturnsDeepStubs} answer in which we will store the returned type generic metadata.
     *
     * @param returnTypeGenericMetadata The metadata to use to create the new mock.
     * @param parentMock                The parent of the current deep stub mock.
     * @return The mock
     */
    private Object newDeepStubMock(
            GenericMetadataSupport returnTypeGenericMetadata, Object parentMock) {
        MockCreationSettings parentMockSettings = MockUtil.getMockSettings(parentMock);
        return mockitoCore()
        .mock(
        returnTypeGenericMetadata.rawType(),
        withSettingsUsing(returnTypeGenericMetadata, parentMockSettings));
    }

    private MockSettings withSettingsUsing(
            GenericMetadataSupport returnTypeGenericMetadata,
            MockCreationSettings<?> parentMockSettings) {
        MockSettings mockSettings =
        withSettings()
        .defaultAnswer(
        returnsDeepStubsAnswerUsing(returnTypeGenericMetadata))
        .serializable(parentMockSettings.getSerializableMode());

        return propagateSerializationSettings(mockSettings, parentMockSettings);
    }

    private MockSettings propagateSerializationSettings(
            MockSettings mockSettings, MockCreationSettings parentMockSettings) {
        return mockSettings.serializable(parentMockSettings.getSerializableMode());
    }

    private ReturnsDeepStubs returnsDeepStubsAnswerUsing(
            final GenericMetadataSupport returnTypeGenericMetadata) {
        return new ReturnsDeepStubsSerializationFallback(returnTypeGenericMetadata);
    }

    private StubbedInvocationMatcher recordDeepStubAnswer(
            final Object mock, InvocationContainerImpl container) {
        DeeplyStubbedAnswer answer = new DeeplyStubbedAnswer(mock);
        return container.addAnswer(answer, answer);
    }

    protected GenericMetadataSupport actualParameterizedType(Object mock) {
        CreationSettings mockSettings =
        (CreationSettings) MockUtil.getMockHandler(mock).getMockSettings();
        return GenericMetadataSupport.inferFrom(mockSettings.getTypeToMock());
    }

    private static class ReturnsDeepStubsSerializationFallback extends ReturnsDeepStubs
            implements Serializable {
        @SuppressWarnings("serial") // not gonna be serialized
        private final GenericMetadataSupport returnTypeGenericMetadata;

        public ReturnsDeepStubsSerializationFallback(
                GenericMetadataSupport returnTypeGenericMetadata) {
            this.returnTypeGenericMetadata = returnTypeGenericMetadata;
        }

        @Override
        protected GenericMetadataSupport actualParameterizedType(Object mock) {
            return returnTypeGenericMetadata;
        }

        /**
         * Generics support and serialization with deep stubs don't work together.
         * <p>
         * The issue is that GenericMetadataSupport is not serializable because
         * the type elements inferred via reflection are not serializable. Supporting
         * serialization would require to replace all types coming from the Java reflection
         * with our own and still managing type equality with the JDK ones.
         */
        private Object writeReplace() throws IOException {
            return new ReturnsDeepStubsSerializationFallback(returnTypeGenericMetadata);
        }
    }

    private static class DeeplyStubbedAnswer implements Answer<Object>, Serializable {
        @SuppressWarnings(
                "serial") // serialization will fail with a nice message if mock not serializable
        private final Object mock;

        DeeplyStubbedAnswer(Object mock) {
            this.mock = mock;
        }

        @Override
        public Object answer(InvocationOnMock invocation) throws Throwable {
            return delegate().answer(invocation);
        }
    }

    private static MockitoCore mockitoCore() {
        return LazyHolder.MOCKITO_CORE;
    }

    private static ReturnsEmptyValues delegate() {
        return LazyHolder.DELEGATE;
    }

    private static class LazyHolder {
        private static final MockitoCore MOCKITO_CORE = new MockitoCore();
        private static final ReturnsEmptyValues DELEGATE = new ReturnsEmptyValues();
    }
}
