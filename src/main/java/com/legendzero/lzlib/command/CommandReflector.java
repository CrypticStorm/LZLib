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

import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;

public class CommandReflector {

    private final Constructor<PluginCommand> constructor;
    private final CommandMap commandMap;
    private final Map<String, Command> aliasMap;

    public CommandReflector(Plugin plugin) {
        this.constructor = this.getConstructor();
        this.commandMap = this.getCommandMap(plugin);
        this.aliasMap = this.findAliasMap();
    }

    public CommandMap getCommandMap() {
        return this.commandMap;
    }

    public Map<String, Command> getAliasMap() {
        return this.aliasMap;
    }

    public PluginCommand createBukkitCommand(Command command) {
        PluginCommand pluginCommand = this.createCommand(command);
        if (pluginCommand == null) {
            return null;
        }

        pluginCommand.setLabel(command.getName());
        pluginCommand.setDescription(command.getDescription());
        pluginCommand.setUsage(command.getUsage());
        pluginCommand.setAliases(Arrays.asList(command.getAliases()));
        return pluginCommand;
    }

    private PluginCommand createCommand(Command command) {
        PluginCommand pluginCommand = null;

        try {
            pluginCommand = constructor.newInstance(command.getName(), command.getPlugin());
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
        }

        return pluginCommand;
    }

    private Constructor<PluginCommand> getConstructor() {
        Constructor<PluginCommand> c = null;
        try {
            c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            c.setAccessible(true);
        } catch (SecurityException | IllegalArgumentException | NoSuchMethodException e) {
        }

        return c;
    }

    private CommandMap getCommandMap(Plugin plugin) {
        CommandMap cMap = null;

        try {
            Field f = plugin.getServer().getPluginManager().getClass().getDeclaredField("commandMap");
            f.setAccessible(true);

            cMap = (CommandMap) f.get(plugin.getServer().getPluginManager());
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
        }

        return cMap;
    }

    private Map<String, Command> findAliasMap() {
        Map<String, Command> map = null;

        try {
            Field f = this.commandMap.getClass().getDeclaredField("knownCommands");
            f.setAccessible(true);

            map = (Map<String, Command>) f.get(this.commandMap);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
        }

        return map;
    }
}
