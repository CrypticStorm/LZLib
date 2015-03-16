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

import com.legendzero.lzlib.provider.Provider;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class CommandReflector implements Provider {

    private final Class<? extends PluginManager> pluginManagerClass;
    private Constructor<PluginCommand> constructor;
    private CommandMap commandMap;

    public CommandReflector(Plugin plugin) {
        this.pluginManagerClass = plugin.getServer().getPluginManager().getClass();
    }

    public CommandMap getCommandMap() {
        return this.commandMap;
    }

    public PluginCommand createBukkitCommand(LZCommand command) {
        PluginCommand pluginCommand = this.constructBukkitCommand(command);
        if (pluginCommand == null) {
            return null;
        }

        pluginCommand.setLabel(command.name());
        pluginCommand.setDescription(command.description());
        pluginCommand.setUsage(command.usage());
        pluginCommand.setAliases(Arrays.asList(command.aliases()));
        return pluginCommand;
    }

    private PluginCommand constructBukkitCommand(LZCommand command) {
        PluginCommand pluginCommand = null;

        try {
            pluginCommand = constructor.newInstance(command.name(), command.getPlugin());
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
        }

        return pluginCommand;
    }

    private Constructor<PluginCommand> defineConstructor() {
        Constructor<PluginCommand> c = null;
        try {
            c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            c.setAccessible(true);
        } catch (SecurityException | IllegalArgumentException | NoSuchMethodException e) {
        }

        return c;
    }

    private CommandMap defineCommandMap() {
        CommandMap cMap = null;

        try {
            Field f = this.pluginManagerClass.getDeclaredField("commandMap");
            f.setAccessible(true);

            cMap = (CommandMap) f.get(this.pluginManagerClass);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
        }

        return cMap;
    }

    @Override
    public void initialize() {
        this.constructor = this.defineConstructor();
        this.commandMap = this.defineCommandMap();
    }

    @Override
    public void uninitialize() {
    }
}
