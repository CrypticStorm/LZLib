package com.legendzero.lzlib.util;

import com.legendzero.lzlib.annotation.FilePath;
import com.legendzero.lzlib.annotation.Identifier;
import com.legendzero.lzlib.annotation.PluginClass;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.regex.Matcher;

public final class Reflections {

    private Reflections() {}

    public static String getIdentifier(Class<?> clazz) {
        String identifier = clazz.getSimpleName();

        if (clazz.isAnnotationPresent(Identifier.class)) {
            identifier = clazz.getAnnotation(Identifier.class).value();
        }
        return identifier;
    }

    public static String getPath(Class<?> clazz) {
        String path = clazz.getSimpleName();

        if (clazz.isAnnotationPresent(FilePath.class)) {
            FilePath annotation = clazz.getAnnotation(FilePath.class);
            path = annotation.value().replaceAll("[/\\\\]+",
                    Matcher.quoteReplacement(System.getProperty("file.separator")));
        }
        return path;
    }

    public static Plugin getPlugin(Class<?> clazz) {
        Class<?> provided = clazz;

        if (clazz.isAnnotationPresent(PluginClass.class)) {
            PluginClass annotation = clazz.getAnnotation(PluginClass.class);
            provided = annotation.value();
        }
        return getProvidingPlugin(provided);
    }

    public static Plugin getProvidingPlugin(Class<?> clazz) {
        try {
            return JavaPlugin.getProvidingPlugin(clazz);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return null;
        }
    }
}
