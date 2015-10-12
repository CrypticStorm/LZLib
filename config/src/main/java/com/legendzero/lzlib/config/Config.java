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

import com.google.common.primitives.Primitives;
import com.legendzero.lzlib.data.Data;
import com.legendzero.lzlib.util.Pair;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collector;

public interface Config<E extends Data> {

    E getData();
    
    void setData(E data);
    
    String getPath();

    Object getDefault();

    Object get();

    void set(Object value);

    default void setDefault() {
        this.set(this.getDefault());
    }

    boolean isSet();

    default <T> T as(Class<T> clazz) {
        Object value = this.get();

        if (clazz.isPrimitive() || Primitives.isWrapperType(clazz)) {
            if (value == null) {
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
                return Primitives.wrap(clazz).cast(value);
            }
        } else if (value == null) {
            return null;
        } else if (clazz.equals(String.class)) {
            return clazz.cast(String.valueOf(value));
        } else if (clazz.isInstance(value)) {
            return clazz.cast(value);
        } else {
            Object def = this.getDefault();
            if (clazz.isInstance(def)) {
                return clazz.cast(def);
            }
        }
        throw new ClassCastException("Config value not of valid type");
    }

    default <T, C> C as(Class<T> type, Collector<T, ?, C> collector) {
        Collection<?> collection = this.as(Collection.class);
        return collection.stream().map(type::cast).collect(collector);
    }

    default <K, V, M> M as(Class<K> keyType, Class<V> valueType, Collector<Pair<K, V>, ?, M> collector) {
        Map<?, ?> map = this.as(Map.class);
        return map.entrySet().stream().map(entry -> new Pair<>(keyType.cast(entry.getKey()), valueType.cast(entry.getValue()))).collect(collector);
    }
}
