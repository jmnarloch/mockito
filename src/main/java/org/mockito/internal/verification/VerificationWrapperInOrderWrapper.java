/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InOrderImpl;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.api.VerificationInOrderMode;
import org.mockito.verification.VerificationMode;

public class VerificationWrapperInOrderWrapper implements VerificationMode {
    private final VerificationMode delegate;

    public VerificationWrapperInOrderWrapper(
            VerificationWrapper<?> verificationWrapper, InOrderImpl inOrder) {
        
    }

    @Override
    public void verify(VerificationData data) {
        
    }

    private VerificationMode wrapInOrder(
            VerificationWrapper<?> verificationWrapper,
            VerificationMode verificationMode,
            InOrderImpl inOrder) {
        
    }
}
