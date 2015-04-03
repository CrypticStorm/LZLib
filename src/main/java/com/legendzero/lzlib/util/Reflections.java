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

import com.legendzero.lzlib.annotation.FilePath;
import com.legendzero.lzlib.annotation.Identifier;
import com.legendzero.lzlib.annotation.PluginClass;
import com.legendzero.lzlib.command.LZCommand;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
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

    public static PluginCommand getPluginCommand(LZCommand<?> command) {
        try {
            Constructor<PluginCommand> c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            c.setAccessible(true);
            return c.newInstance(command.name(), command.getPlugin());
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            command.getPlugin().getLogger().log(Level.SEVERE, "Error creating Bukkit command", e);
            return null;
        }
    }

    public static CommandMap getCommandMap(LZCommand<?> command) {
        PluginManager pluginManager = command.getPlugin().getServer().getPluginManager();
        CommandMap cMap = null;

        try {
            Field f = pluginManager.getClass().getDeclaredField("commandMap");
            f.setAccessible(true);

            cMap = (CommandMap) f.get(pluginManager);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            command.getPlugin().getLogger().log(Level.SEVERE, "Error getting CommandMap", e);
        }

        return cMap;
    }
}
