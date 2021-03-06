/*
 * Copyright (c) 2015 Legend Zero LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.legendzero.lzlib.util;

import java.util.regex.Matcher;

public final class Annotations {

    private Annotations() {
    }

    public static String getIdentifier(Class<?> clazz) {
        String identifier = clazz.getSimpleName();

        if (clazz.isAnnotationPresent(Identifier.class)) {
            identifier = clazz.getAnnotation(Identifier.class).value();
        }
        return identifier;
    }

    public static String getPath(Class<?> clazz) {
        String path = clazz.getSimpleName() + ".yml";

        if (clazz.isAnnotationPresent(FilePath.class)) {
            FilePath annotation = clazz.getAnnotation(FilePath.class);
            path = annotation.value().replaceAll("[/\\\\]+",
                    Matcher.quoteReplacement(System.getProperty("file.separator")));
        }
        return path;
    }
}
