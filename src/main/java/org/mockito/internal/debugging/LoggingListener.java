/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.debugging;

import static org.mockito.internal.util.StringUtil.join;

import java.util.LinkedList;
import java.util.List;

import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.Invocation;

public class LoggingListener implements FindingsListener {
    private final boolean warnAboutUnstubbed;

    private final List<String> argMismatchStubs = new LinkedList<>();
    private final List<String> unusedStubs = new LinkedList<>();
    private final List<String> unstubbedCalls = new LinkedList<>();

    public LoggingListener(boolean warnAboutUnstubbed) {
        
    }

    @Override
    public void foundStubCalledWithDifferentArgs(Invocation unused, InvocationMatcher unstubbed) {
        // TODO there is not good reason we should get Invocation and InvocationMatcher here
        // we should pass 2 InvocationMatchers and testing is easier
        // it's also confusing that unstubbed invocation is passed as InvocationMatcher (should be
        // rather Invocation)

        // this information comes in pairs
        
    }

    static int indexOfNextPair(int collectionSize) {
        
    }

    @Override
    public void foundUnusedStub(Invocation unused) {
        
    }

    @Override
    public void foundUnstubbed(InvocationMatcher unstubbed) {
        
    }

    public String getStubbingInfo() {
        
    }

    private void addOrderedList(List<String> target, List<String> additions) {
        
    }
}
