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

    public static void deserialize(Map<String, Object> args) {
        ConfigurationSerialization.deserializeObject(args);
    }

    public static void deserializeAs(Map<String, Object> args, Class<? extends ConfigurationSerializable> serializable) {
        ConfigurationSerialization.deserializeObject(args, serializable);
    }
}