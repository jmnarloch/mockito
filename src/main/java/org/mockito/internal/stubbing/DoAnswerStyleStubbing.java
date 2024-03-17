/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;

/**
 * Holds answers declared using 'doAnswer' stubbing style.
 */
class DoAnswerStyleStubbing implements Serializable {

    private final List<Answer<?>> answers = new ArrayList<>();
    private Strictness stubbingStrictness;

    void setAnswers(List<Answer<?>> answers, Strictness stubbingStrictness) {
        
    }

    boolean isSet() {
        
    }

    void clear() {
        
    }

    List<Answer<?>> getAnswers() {
        
    }

    Strictness getStubbingStrictness() {
        
    }
}
