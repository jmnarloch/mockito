/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import static org.mockito.internal.util.StringUtil.join;

import java.util.Locale;

public abstract class Platform {

    public static final String JAVA_VERSION = System.getProperty("java.specification.version");
    public static final String JVM_VERSION = System.getProperty("java.runtime.version");
    public static final String JVM_VENDOR = System.getProperty("java.vm.vendor");
    public static final String JVM_VENDOR_VERSION = System.getProperty("java.vm.version");
    public static final String JVM_NAME = System.getProperty("java.vm.name");
    public static final String JVM_INFO = System.getProperty("java.vm.info");
    public static final String OS_NAME = System.getProperty("os.name");
    public static final String OS_VERSION = System.getProperty("os.version");

    private Platform() { }

    public static boolean isAndroid() {
        
    }

    public static boolean isAndroidMockMakerRequired() {
        
    }

    public static String describe() {
        
    }

    public static String warnForVM(
            String vmName1, String warnMessage1, String vmName2, String warnMessage2) {
        
    }

    static String warnForVM(
            String current,
            String vmName1,
            String warnMessage1,
            String vmName2,
            String warnMessage2) {
        
    }
}
