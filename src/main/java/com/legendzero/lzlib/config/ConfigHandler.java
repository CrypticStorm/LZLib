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

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.legendzero.lzlib.annotation.FilePath;
import com.legendzero.lzlib.annotation.Identifier;
import com.legendzero.lzlib.data.FileData;
import com.legendzero.lzlib.data.YamlData;
import com.legendzero.lzlib.lang.LZLibLang;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.regex.Matcher;

public class ConfigHandler<E extends JavaPlugin> {

    private final E plugin;
    private final Map<String, Class<? extends Config>> configs;

    public ConfigHandler(E plugin) {
        this.plugin = plugin;
        this.configs = Maps.newHashMap();
    }

    public void register(Class<? extends Config> clazz) {
        if (clazz.isEnum()) {
            if (clazz.isAnnotationPresent(FilePath.class)) {
                FilePath annotation = clazz.getAnnotation(FilePath.class);
                String path = annotation.value();
                path = path.replaceAll("[/\\\\]+",Matcher.quoteReplacement(
                        System.getProperty("file.separator")));

                File file = new File(this.plugin.getDataFolder(), path);
                if (!file.exists()) {
                    file.mkdirs();
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        this.plugin.getLogger().warning("Could not create config file");
                    }
                }

                String identifier = file.getName();
                if (clazz.isAnnotationPresent(Identifier.class)) {
                    identifier = clazz.getAnnotation(Identifier.class).value();
                }
                FileData data = new YamlData(identifier, file);
                for (Config config : clazz.getEnumConstants()) {
                    config.setFileData(data);
                    if (!config.isSet()) {
                        config.setDefault();
                    }
                }

                this.configs.put(data.getIdentifier(), clazz);
            }
        } else {
            LZLibLang.CONFIG_NOT_ENUM.log(this.plugin.getLogger(), Level.WARNING);
        }
    }

    public Set<String> getIdentifiers() {
        return ImmutableSet.copyOf(this.configs.keySet());
    }

    public Class<? extends Config> getConfigClass(String identifier) {
        return this.configs.get(identifier);
    }
}