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

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

public final class Serialization {

    private Serialization() {}

    public static void serialize(Class<? extends ConfigurationSerializable> serializable) {
        ConfigurationSerialization.registerClass(serializable);
    }

    public static void serialize(Class<? extends ConfigurationSerializable>... serializables) {
        Arrays.stream(serializables).forEach(Serialization::serialize);
    }

    public static void serialize(Iterable<? extends Class<? extends ConfigurationSerializable>> serializables) {
        serializables.forEach(Serialization::serialize);
    }

    public static void serialize(Iterator<? extends Class<? extends ConfigurationSerializable>> serializables) {
        serializables.forEachRemaining(Serialization::serialize);
    }

    public static ConfigurationSerializable deserialize(Map<String, Object> args) {
        return ConfigurationSerialization.deserializeObject(args);
    }

    public static ConfigurationSerializable deserializeAs(Map<String, Object> args, Class<? extends ConfigurationSerializable> serializable) {
        return ConfigurationSerialization.deserializeObject(args, serializable);
    }
}
