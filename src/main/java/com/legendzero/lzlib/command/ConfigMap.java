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

package com.legendzero.lzlib.command;

import com.google.common.collect.Maps;
import com.legendzero.lzlib.config.Config;
import com.legendzero.lzlib.service.Service;
import org.bukkit.plugin.Plugin;

import java.util.Map;

public class ConfigMap implements Service {

    private final Map<Plugin, Map<String, Class<? extends Config>>> configMap;

    public ConfigMap() {
        this.configMap = Maps.newHashMap();
    }

    public Map<String, Class<? extends Config>> getConfigMap(Plugin plugin) {
        return this.configMap.get(plugin);
    }

    public void register(Plugin plugin, String identifier, Class<? extends Config> clazz) {
        if (!this.configMap.containsKey(plugin)) {
            this.configMap.put(plugin, Maps.newHashMap());
        }
        this.configMap.get(plugin).putIfAbsent(identifier, clazz);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void uninitialize() {
    }
}
