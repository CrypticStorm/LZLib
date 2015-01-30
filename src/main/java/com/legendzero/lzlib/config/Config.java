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

import com.legendzero.lzlib.data.FileData;

public interface Config<E extends Enum<E> & Config<E>> {

    FileData getFileData();

    void setFileData(FileData data);

    String getPath();

    Object getDefault();

    default Object get() {
        return this.getFileData().get(this.getPath(), this.getDefault());
    }

    default void set(Object value) {
        this.getFileData().set(this.getPath(), value);
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
        } else if (clazz.isPrimitive()) {
            if (clazz.equals(boolean.class)) {
                return clazz.cast(false);
            } else if (clazz.equals(byte.class)) {
                return clazz.cast((byte) -1);
            } else if (clazz.equals(char.class)) {
                return clazz.cast((char) -1);
            } else if (clazz.equals(double.class)) {
                return clazz.cast(-1D);
            } else if (clazz.equals(float.class)) {
                return clazz.cast(-1F);
            } else if (clazz.equals(int.class)) {
                return clazz.cast(-1);
            } else if (clazz.equals(long.class)) {
                return clazz.cast(-1L);
            } else if (clazz.equals(short.class)) {
                return clazz.cast((short) -1);
            } else if (clazz.equals(void.class)) {
                throw new IllegalArgumentException("Can not cast to void");
            } else {
                throw new IllegalArgumentException("Invalid primitive: " + clazz.getName());
            }
        } else {
            return null;
        }
    }
}
