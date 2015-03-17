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

import com.legendzero.lzlib.command.CommandReflector;
import com.legendzero.lzlib.command.ConfigMap;
import com.legendzero.lzlib.gui.GuiService;
import com.legendzero.lzlib.service.Service;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;

import java.util.function.Function;
import java.util.function.Supplier;

public final class Services {

    private Services() {}

    public final void registerDefaultServiceProviders(Plugin plugin) {
        Services.registerServiceProvider(plugin, CommandReflector.class, CommandReflector::new, ServicePriority.Lowest);
        registerServiceProvider(plugin, ConfigMap.class, ConfigMap::new, ServicePriority.Lowest);
        Services.registerServiceProvider(plugin, GuiService.class, GuiService::new, ServicePriority.Lowest);
    }

    public static <T extends Service> T registerServiceProvider(Plugin plugin, Class<T> clazz,
                                                                Supplier<T> supplier,
                                                                ServicePriority priority) {
        if (plugin.getServer().getServicesManager().isProvidedFor(clazz)) {
            return plugin.getServer().getServicesManager().getRegistration(clazz).getProvider();
        } else {
            T t = supplier.get();
            t.initialize();
            plugin.getServer().getServicesManager().register(clazz, t, plugin, priority);
            return t;
        }
    }

    public static <T extends Service> T registerServiceProvider(Plugin plugin, Class<T> clazz,
                                                               Function<? super Plugin, T> function,
                                                               ServicePriority priority) {
        if (plugin.getServer().getServicesManager().isProvidedFor(clazz)) {
            return plugin.getServer().getServicesManager().getRegistration(clazz).getProvider();
        } else {
            T t = function.apply(plugin);
            t.initialize();
            plugin.getServer().getServicesManager().register(clazz, t, plugin, priority);
            return t;
        }
    }
}
