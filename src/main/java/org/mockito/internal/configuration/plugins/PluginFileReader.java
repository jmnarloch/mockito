/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.plugins;

import java.io.InputStream;

import org.mockito.internal.util.io.IOUtil;

class PluginFileReader {

    String readPluginClass(InputStream input) {
        for (String line : IOUtil.readLines(input)) {
            String pluginClass = stripCommentAndWhitespace(line);
            if (!pluginClass.isEmpty()) {
                return pluginClass;
            }
        }
        return null;
    }

    private static String stripCommentAndWhitespace(String line) {
        int commentStart = line.indexOf('#');
        if (commentStart != -1) {
            line = line.substring(0, commentStart);
        }
        return line.trim();
    }
}
