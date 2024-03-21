/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

import org.mockito.internal.invocation.finder.AllInvocationsFinder;
import org.mockito.invocation.Invocation;
import org.mockito.stubbing.Stubbing;

/**
 * For given mocks, finds stubbing arg mismatches
 */
class ArgMismatchFinder {

    StubbingArgMismatches getStubbingArgMismatches(Iterable<?> mocks) {
        StubbingArgMismatches mismatches = new StubbingArgMismatches();
        for (Invocation i : AllInvocationsFinder.find(mocks)) {
            if (i.stubInfo() != null) {
                // if we could not find invocation for this stubbing it means
                // the test has incorrect stubbed the method
                // well... actually we can eventually allow this but we would need a switch
                // on MockingProgress
                // so that user can decide if strict stubbing should be on or off
                // (in other words, we can eventually introduce
                // StrictStubbingMockingProgress)
                // if the user chooses strict stubbing we would then throw exception here
                // if the user chooses soft stubbing we would just ignore the situation
                // for now let's just ignore - we may eventually introduce strict stubbing in v.2
                mismatches.add(i.getInvocation());
            }
        }
        return mismatches;
    }
}
