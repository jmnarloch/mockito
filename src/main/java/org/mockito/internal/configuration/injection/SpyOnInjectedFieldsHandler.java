/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.injection;

import static org.mockito.Mockito.withSettings;

import java.lang.reflect.Field;
import java.util.Set;

import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.FieldReader;
import org.mockito.plugins.MemberAccessor;

/**
 * Handler for field annotated with &#64;InjectMocks and &#64;Spy.
 *
 * <p>
 * The handler assumes that field initialization AND injection already happened.
 * So if the field is still null, then nothing will happen there.
 * </p>
 */
public class SpyOnInjectedFieldsHandler extends MockInjectionStrategy {

    private final MemberAccessor accessor = Plugins.getMemberAccessor();

    @Override
    protected boolean processInjection(Field field, Object fieldOwner, Set<Object> mockCandidates) {
        FieldReader fieldReader = new FieldReader(fieldOwner, field);

        // issue 2031 - prefer constructor mock to avoid partial mock
        if (fieldReader.isNull() && !mockCandidates.isEmpty()) {
            Object candidate = mockCandidates.iterator().next();
            if (MockUtil.isMock(candidate)) {
                try {
                    Object spiedInstance =
                    Mockito.mock(
                    field.getType(),
                    withSettings()
                    .spiedInstance(candidate)
                    .defaultAnswer(Mockito.CALLS_REAL_METHODS)
                    .name(field.getName()));
                    accessor.set(field, fieldOwner, spiedInstance);
                } catch (Exception e) {
                    throw new MockitoException(
                    "Problems initiating spied field " + field.getName(), e);
                }
            }
        }

        if (!fieldReader.isNull()) {
            // at this point we have a mock instance to be spied
            // no verifyNoInteractions(), not marking verified(), not stripping MockitoLogger
            // since this is a stubbing mock made to be spied on
            Object instance =
            Mockito.mock(
            field.getType(),
            withSettings()
            .spiedInstance(fieldReader.read())
            .name(field.getName())
            .defaultAnswer(Mockito.CALLS_REAL_METHODS));
            accessor.set(field, fieldOwner, instance);
            return true;
        }

        return false;
    }
}
