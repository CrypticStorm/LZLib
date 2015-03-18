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

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.legendzero.lzlib.config.Config;
import com.legendzero.lzlib.event.ConfigRegisterEvent;
import com.legendzero.lzlib.util.Reflections;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.Map;

public class ConfigMap {

    private final Map<String, Class<? extends Config>> configMap;

    public ConfigMap(Plugin plugin) {
        this(plugin, false);
    }

    public ConfigMap(Plugin plugin, boolean autoDetect) {
        this.configMap = Maps.newHashMap();
        if (autoDetect) {
            plugin.getServer().getPluginManager().registerEvents(new ConfigListener(), plugin);
        }
    }

    public Map<String, Class<? extends Config>> getConfigMap() {
        return ImmutableMap.copyOf(this.configMap);
    }

    public void register(Class<? extends Config> clazz) {
        this.register(Reflections.getIdentifier(clazz), clazz);
    }

    public void register(String identifier, Class<? extends Config> clazz) {
        this.configMap.putIfAbsent(identifier, clazz);
    }

    private class ConfigListener implements Listener {

        @EventHandler
        public void onConfigRegister(ConfigRegisterEvent event) {
            ConfigMap.this.register(event.getConfigClass());
        }
    }
}
