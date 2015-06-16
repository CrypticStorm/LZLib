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

import com.legendzero.lzlib.config.FileConfig;
import com.legendzero.lzlib.data.FileData;
import com.legendzero.lzlib.data.YamlData;
import com.legendzero.lzlib.event.ConfigRegisterEvent;
import com.legendzero.lzlib.lang.LZLibBukkitLang;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public final class BukkitUtils {

    private BukkitUtils() {
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


    public static <T extends Enum & FileConfig> void register(Class<T> registrant) {
        Plugin plugin = BukkitUtils.getPlugin(registrant);
        if (plugin == null) {
            throw new IllegalArgumentException(registrant.getName() +
                    " not provided by Plugin or annotated with a Plugin provided class");
        }
        String identifier = Annotations.getIdentifier(registrant);
        String path = Annotations.getPath(registrant);

        File file = new File(plugin.getDataFolder(), path);

        if (file.getParentFile().mkdirs()) {
            LZLibBukkitLang.FILE_CREATE_SUCCESS.log(plugin.getLogger(), Level.INFO, file.getParentFile().getAbsolutePath());
        }

        try {
            if (file.createNewFile()) {
                LZLibBukkitLang.FILE_CREATE_SUCCESS.log(plugin.getLogger(), Level.INFO, file.getName());
            }
        } catch (IOException e) {
            LZLibBukkitLang.FILE_CREATE_FAILURE.log(plugin.getLogger(), Level.WARNING, file.getName());
        }

        FileData data = new YamlData(identifier, file);
        for (FileConfig config : registrant.getEnumConstants()) {
            config.setData(data);
            if (!config.isSet()) {
                config.setDefault();
            }
        }

        plugin.getServer().getPluginManager().callEvent(new ConfigRegisterEvent(registrant));
    }
}
