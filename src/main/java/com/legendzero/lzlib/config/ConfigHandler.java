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
import com.legendzero.lzlib.interfaces.LZHandler;
import com.legendzero.lzlib.lang.LZLibLang;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

public class ConfigHandler<E extends JavaPlugin> implements LZHandler<Class<? extends Config>> {

    private final E plugin;
    private final Map<String, Class<? extends Config>> configClasses;

    public ConfigHandler(E plugin) {
        this.plugin = plugin;
        this.configClasses = Maps.newHashMap();
    }

    public void register(Class<? extends Config> registrant) {
        if (registrant.isEnum()) {
            if (registrant.isAnnotationPresent(FilePath.class)) {
                String path = Config.getIdentifier(registrant);

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
                if (registrant.isAnnotationPresent(Identifier.class)) {
                    identifier = registrant.getAnnotation(Identifier.class).value();
                }
                FileData data = new YamlData(identifier, file);
                for (Config config : registrant.getEnumConstants()) {
                    config.setFileData(data);
                    if (!config.isSet()) {
                        config.setDefault();
                    }
                }

                this.configClasses.put(data.getIdentifier(), registrant);
            }
        } else {
            LZLibLang.CONFIG_NOT_ENUM.log(this.plugin.getLogger(), Level.WARNING);
        }
    }

    @Override
    public void unregister(Class<? extends Config> registrant) {
        this.configClasses.remove(Config.getIdentifier(registrant), registrant);
    }

    @Override
    public void unregisterAll() {
        this.configClasses.clear();
    }

    @Override
    public Collection<Class<? extends Config>> getRegistered() {
        return this.configClasses.values();
    }

    public Set<String> getIdentifiers() {
        return ImmutableSet.copyOf(this.configClasses.keySet());
    }

    public Class<? extends Config> getConfigClass(String identifier) {
        return this.configClasses.get(identifier);
    }
}
