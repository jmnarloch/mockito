/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.io;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.mockito.exceptions.base.MockitoException;

/**
 * IO utils. A bit of reinventing the wheel but we don't want extra dependencies at this stage and
 * we want to be java.
 */
public final class IOUtil {

    /**
     * Writes text to file in UTF-8.
     */
    public static void writeText(String text, File output) {
        
    }

    /**
     * Reads all lines from the given stream. Uses UTF-8.
     */
    public static Collection<String> readLines(InputStream is) {
        
    }

    /**
     * Closes the target. Does nothing when target is null. Is silent.
     *
     * @param closeable the target, may be null
     */
    public static void closeQuietly(Closeable closeable) {
        
    }

    /**
     * Closes the target. Does nothing when target is null. Is not silent and exceptions are rethrown.
     *
     * @param closeable the target, may be null
     */
    public static void close(Closeable closeable) {
        
    }

    private IOUtil() { }
}
