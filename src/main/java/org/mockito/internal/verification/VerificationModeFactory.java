/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import org.mockito.verification.VerificationMode;

public final class VerificationModeFactory {

    public static VerificationMode atLeastOnce() {
        
    }

    public static VerificationMode atLeast(int minNumberOfInvocations) {
        
    }

    public static VerificationMode only() {
         // TODO make exception message nicer
    }

    public static Times times(int wantedNumberOfInvocations) {
        
    }

    public static Calls calls(int wantedNumberOfInvocations) {
        
    }

    public static NoMoreInteractions noMoreInteractions() {
        
    }

    public static NoInteractions noInteractions() {
        
    }

    public static VerificationMode atMostOnce() {
        
    }

    public static VerificationMode atMost(int maxNumberOfInvocations) {
        
    }

    /**
     * Verification mode will prepend the specified failure message if verification fails with the given implementation.
     * @param mode Implementation used for verification
     * @param description The custom failure message
     * @return VerificationMode
     * @since 2.1.0
     */
    public static VerificationMode description(VerificationMode mode, String description) {
        
    }

    private VerificationModeFactory() { }
}
