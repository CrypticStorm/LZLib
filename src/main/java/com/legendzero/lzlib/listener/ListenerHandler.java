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

package com.legendzero.lzlib.listener;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.legendzero.lzlib.interfaces.Listenable;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.Collection;
import java.util.Set;

public class ListenerHandler<E extends Plugin & Listenable<E>> {
    
    private final E plugin;
    private final PluginManager pluginManager;
    private final Set<LZListener<E>> listeners;

    public ListenerHandler(E plugin) {
        this.plugin = plugin;
        this.pluginManager = plugin.getServer().getPluginManager();
        this.listeners = Sets.newHashSet();
    }

    public void register(LZListener<E> registrant) {
        if (this.listeners.add(registrant)) {
            this.pluginManager.registerEvents(registrant, this.plugin);
        }
    }

    public void unregister(LZListener<E> registrant) {
        if (this.listeners.remove(registrant)) {
            HandlerList.unregisterAll(registrant);
        }
    }

    public void unregisterAll() {
        for (LZListener<E> listener : this.listeners) {
            HandlerList.unregisterAll(listener);
        }
        this.listeners.clear();
    }

    public Collection<LZListener<E>> getRegistered() {
        return ImmutableSet.copyOf(this.listeners);
    }
}
