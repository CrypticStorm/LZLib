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
import com.legendzero.lzlib.annotation.Identifier;
import com.legendzero.lzlib.annotation.PluginClass;
import com.legendzero.lzlib.data.Data;
import com.legendzero.lzlib.data.FileData;
import com.legendzero.lzlib.data.YamlData;
import com.legendzero.lzlib.lang.LZLibLang;
import org.apache.commons.lang.ClassUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.util.logging.Level;
import java.util.regex.Matcher;

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

    static void register(Class<? extends FileConfig> registrant) {
        if (registrant.isEnum()) {
            String identifier = registrant.getName();

            if (registrant.isAnnotationPresent(Identifier.class)) {
                identifier = registrant.getAnnotation(Identifier.class).value();
            }

            if (registrant.isAnnotationPresent(PluginClass.class)) {
                Plugin plugin = getPlugin(registrant);

                File file = new File(plugin.getDataFolder(), "config.yml");

                if (registrant.isAnnotationPresent(FilePath.class)) {
                    String path = getPath(registrant);
                    file = new File(plugin.getDataFolder(), path);
                }

                if (file.mkdirs()) {
                    LZLibLang.FILE_CREATE_SUCCESS.log(plugin.getLogger(), Level.INFO, file.getParentFile().getAbsolutePath());
                }

                try {
                    if (file.createNewFile()) {
                        LZLibLang.FILE_CREATE_SUCCESS.log(plugin.getLogger(), Level.INFO, file.getName());
                    }
                } catch (IOException e) {
                    LZLibLang.FILE_CREATE_FAILURE.log(plugin.getLogger(), Level.WARNING, file.getName());
                }

                FileData data = new YamlData(identifier, file);
                for (FileConfig config : registrant.getEnumConstants()) {
                    config.setData(data);
                    if (!config.isSet()) {
                        config.setDefault();
                    }
                }
            }
        }
    }

    static String getPath(AnnotatedElement element) {
        FilePath annotation = element.getAnnotation(FilePath.class);
        return annotation.value().replaceAll("[/\\\\]+",
                Matcher.quoteReplacement(System.getProperty("file.separator")));
    }

    static Plugin getPlugin(AnnotatedElement element) {
        PluginClass annotation = element.getAnnotation(PluginClass.class);
        return JavaPlugin.getPlugin(annotation.value());
    }
}
