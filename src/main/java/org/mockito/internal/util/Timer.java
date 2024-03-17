/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import static org.mockito.internal.exceptions.Reporter.cannotCreateTimerWithNegativeDurationTime;

public class Timer {

    private final long durationMillis;
    private long startTime = -1;

    public Timer(long durationMillis) {
        
    }

    /**
     * Informs whether the timer is still counting down.
     */
    public boolean isCounting() {
        
    }

    /**
     * Starts the timer count down.
     */
    public void start() {
        
    }

    private void validateInput(long durationMillis) {
        
    }

    public long duration() {
        
    }
}
