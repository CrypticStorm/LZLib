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

package com.legendzero.lzlib;

import com.legendzero.lzlib.command.CommandReflector;
import com.legendzero.lzlib.config.ConfigHandler;
import com.legendzero.lzlib.config.FileConfig;
import com.legendzero.lzlib.gui.GuiProvider;
import com.legendzero.lzlib.interfaces.Configurable;
import com.legendzero.lzlib.lang.LZLibLang;
import com.legendzero.lzlib.provider.Provider;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Function;
import java.util.logging.Level;

public abstract class LZPlugin<E extends LZPlugin<E>> extends JavaPlugin implements Configurable {

    protected ConfigHandler configHandler = null;

    @Override
    public final void onLoad() {
        this.configHandler = new ConfigHandler(this);

        this.load();
    }

    protected abstract void load();

    @Override
    public final void onEnable() {
        LZLibLang.LOAD_SERIALIZABLES.log(this.getLogger(), Level.INFO);
        for (Class<? extends ConfigurationSerializable> clazz :
                this.getSerializableClasses()) {
            ConfigurationSerialization.registerClass(clazz);
        }

        if (this.configHandler != null) {
            LZLibLang.LOAD_CONFIGURATION.log(this.getLogger(), Level.INFO);
            for (Class<? extends FileConfig> clazz : this.getFileConfigClasses()) {
                this.configHandler.register(clazz);
            }
        }

        this.registerDefaultServiceProviders();

        this.enable();
    }

    protected abstract void enable();

    @Override
    public final void onDisable() {
        this.configHandler.unregisterAll();
        this.getServer().getServicesManager().unregisterAll(this);

        this.disable();
    }

    protected abstract void disable();

    public abstract void loadCommands();

    @Override
    public ConfigHandler getConfigHandler() {
        return this.configHandler;
    }

    public final void registerDefaultServiceProviders() {
        this.registerServiceProvider(GuiProvider.class, GuiProvider::new, ServicePriority.Lowest);
        this.registerServiceProvider(CommandReflector.class, CommandReflector::new, ServicePriority.Lowest);
    }

    public final <T extends Provider> T registerServiceProvider(Class<T> clazz,
                                                          Function<? super Plugin, T> function,
                                                          ServicePriority priority) {
        if (this.getServer().getServicesManager().isProvidedFor(clazz)) {
            return this.getServer().getServicesManager().getRegistration(clazz).getProvider();
        } else {
            T t = function.apply(this);
            t.initialize();
            this.getServer().getServicesManager().register(clazz, t, this, priority);
            return t;
        }
    }

    public abstract Class<? extends ConfigurationSerializable>[] getSerializableClasses();

    public abstract Class<? extends FileConfig>[] getFileConfigClasses();
}
