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

import com.legendzero.lzlib.command.LZCommand;
import com.legendzero.lzlib.command.CommandHandler;
import com.legendzero.lzlib.config.Config;
import com.legendzero.lzlib.config.ConfigHandler;
import com.legendzero.lzlib.interfaces.Commandable;
import com.legendzero.lzlib.interfaces.Configurable;
import com.legendzero.lzlib.interfaces.Listenable;
import com.legendzero.lzlib.lang.LZLibLang;
import com.legendzero.lzlib.listener.ListenerHandler;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public abstract class LZPlugin<E extends LZPlugin<E>> extends JavaPlugin implements Commandable<E>, Configurable<E>, Listenable<E> {

    protected CommandHandler<E> commandHandler;
    protected ConfigHandler<E> configHandler;
    protected ListenerHandler<E> listenerHandler;

    @Override
    public void onLoad() {
        LZLibLang.LOAD_SERIALIZABLES.log(this.getLogger(), Level.INFO);
        for (Class<? extends ConfigurationSerializable> clazz :
                this.getSerializableClasses()) {
            ConfigurationSerialization.registerClass(clazz);
        }
        this.listenerHandler = new ListenerHandler<>((E) this);
    }

    @Override
    public void onEnable() {
        LZLibLang.LOAD_CONFIGURATION.log(this.getLogger(), Level.INFO);
        this.configHandler = new ConfigHandler<>((E) this);
        for (Class<? extends Config> clazz : this.getConfigClasses()) {
            this.configHandler.register(clazz);
        }

        LZLibLang.LOAD_COMMANDS.log(this.getLogger(), Level.INFO);
        this.commandHandler = new CommandHandler<>((E) this);
        for (LZCommand<E> command : this.getRootCommands()) {
            this.commandHandler.register(command);
        }
    }

    @Override
    public void onDisable() {
        this.listenerHandler.unregisterAll();
        this.commandHandler.unregisterAll();
        this.configHandler.unregisterAll();
    }

    @Override
    public CommandHandler<E> getCommandHandler() {
        return this.commandHandler;
    }

    @Override
    public ConfigHandler<E> getConfigHandler() {
        return this.configHandler;
    }
    
    @Override
    public ListenerHandler<E> getListenerHandler() {
        return this.listenerHandler;
    }

    public abstract Class<? extends ConfigurationSerializable>[] getSerializableClasses();

    public abstract Class<? extends Config>[] getConfigClasses();

    public abstract LZCommand<E>[] getRootCommands();
}
