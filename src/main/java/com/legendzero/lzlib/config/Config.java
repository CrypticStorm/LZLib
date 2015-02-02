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

package com.legendzero.lzlib.config;

import com.legendzero.lzlib.annotation.FilePath;
import com.legendzero.lzlib.data.FileData;
import org.apache.commons.lang.ClassUtils;

import java.lang.reflect.AnnotatedElement;
import java.util.regex.Matcher;

public interface Config {

    FileData getFileData();

    void setFileData(FileData data);

    String getPath();

    Object getDefault();

    default Object get() {
        if (this.getFileData() != null) {
            return this.getFileData().get(this.getPath(), this.getDefault());
        } else {
            return this.getDefault();
        }
    }

    default void set(Object value) {
        if (this.getFileData() != null) {
            this.getFileData().set(this.getPath(), value);
        }
    }

    default void setDefault() {
        this.set(this.getDefault());
    }

    default boolean isSet() {
        return this.getFileData().isSet(this.getPath());
    }

    default <T> T as(Class<T> clazz) {
        Object value = this.get();
        if (value != null) {
            if (clazz.isInstance(value)) {
                return clazz.cast(value);
            }
            Object def = this.getDefault();
            if (clazz.isInstance(def)) {
                return clazz.cast(def);
            } else {
                throw new ClassCastException();
            }
        } else if (clazz.isPrimitive() || ClassUtils.wrapperToPrimitive(clazz) != null) {
            if (clazz.equals(boolean.class) || clazz.equals(Boolean.class)) {
                return clazz.cast(false);
            } else if (clazz.equals(byte.class) || clazz.equals(Byte.class)) {
                return clazz.cast((byte) -1);
            } else if (clazz.equals(char.class) || clazz.equals(Character.class)) {
                return clazz.cast((char) -1);
            } else if (clazz.equals(double.class) || clazz.equals(Double.class)) {
                return clazz.cast(-1D);
            } else if (clazz.equals(float.class) || clazz.equals(Float.class)) {
                return clazz.cast(-1F);
            } else if (clazz.equals(int.class) || clazz.equals(Integer.class)) {
                return clazz.cast(-1);
            } else if (clazz.equals(long.class) || clazz.equals(Long.class)) {
                return clazz.cast(-1L);
            } else if (clazz.equals(short.class) || clazz.equals(Short.class)) {
                return clazz.cast((short) -1);
            } else if (clazz.equals(void.class) || clazz.equals(Void.class)) {
                throw new IllegalArgumentException("Can not cast to void");
            } else {
                throw new IllegalArgumentException("Invalid primitive: " + clazz.getName());
            }
        } else {
            return null;
        }
    }
    
    static String getIdentifier(AnnotatedElement element) {
        FilePath annotation = element.getAnnotation(FilePath.class);
        return annotation.value().replaceAll("[/\\\\]+",
                Matcher.quoteReplacement(System.getProperty("file.separator")));
    }
}
