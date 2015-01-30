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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.legendzero.lzlib.interfaces.Commandable;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CommandHandler<E extends JavaPlugin & Commandable<E>> implements TabExecutor {

    private final E plugin;
    private final CommandReflector reflector;
    private final Map<String, Command<E>> registeredCommands;
    private final Map<String, org.bukkit.command.Command> bukkitCommands;

    public CommandHandler(E plugin) {
        this.plugin = plugin;
        this.reflector = new CommandReflector(this.plugin);
        this.registeredCommands = Maps.newHashMap();
        this.bukkitCommands = Maps.newHashMap();
    }

    public void register(Command<E> command) {
        PluginCommand pluginCommand = this.reflector.createBukkitCommand(command);
        if (command != null && this.reflector.getCommandMap().register(this.plugin.getName(), pluginCommand)) {
            pluginCommand.setExecutor(this);
            pluginCommand.setExecutor(this);
            this.registeredCommands.put(pluginCommand.getName(), command);
            this.bukkitCommands.put(pluginCommand.getName(), pluginCommand);
        }
    }

    public Collection<Command<E>> getRootCommands() {
        return this.registeredCommands.values();
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String cmdLabel, String[] args) {
        if (!this.registeredCommands.containsKey(cmd.getName())) {
            return true;
        }
        return this.registeredCommands.get(cmd.getName()).resolveCommand(sender, Lists.newArrayList(args));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command cmd, String cmdLabel, String[] args) {
        if (!this.registeredCommands.containsKey(cmd.getName())) {
            return Lists.newArrayList();
        }
        return this.registeredCommands.get(cmd.getName()).resolveTab(sender, Lists.newArrayList(args));
    }
}
