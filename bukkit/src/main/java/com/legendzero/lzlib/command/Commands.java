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

import com.legendzero.lzlib.command.CommandContext;
import com.legendzero.lzlib.command.SubCommand;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.UtilityClass;
import lombok.extern.java.Log;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;

@Log
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Commands {

    public static PluginCommand registerPluginCommand(String name, Plugin plugin) {
        PluginCommand pluginCommand = plugin.getServer().getPluginCommand(name);
        if (pluginCommand == null) {
            pluginCommand = constructPluginCommand(name, plugin);
            CommandMap commandMap = getCommandMap();
            if (pluginCommand == null || commandMap == null) {
                return null;
            }
            commandMap.register(plugin.getName().toLowerCase(), pluginCommand);
        }
        return pluginCommand;
    }

    public static PluginCommand constructPluginCommand(String name, Plugin plugin) {
        try {
            Constructor<PluginCommand> c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            c.setAccessible(true);
            return c.newInstance(name, plugin);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            log.log(Level.SEVERE, "Error creating Bukkit command", e);
            return null;
        }
    }

    public static CommandMap getCommandMap() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        CommandMap cMap = null;

        try {
            Field f = pluginManager.getClass().getDeclaredField("commandMap");
            f.setAccessible(true);

            cMap = (CommandMap) f.get(pluginManager);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            log.log(Level.SEVERE, "Error getting CommandMap", e);
        }

        return cMap;
    }

    public static void showHelp(SubCommand<?> command, CommandSender sender, CommandContext context) {
        String executedCommand = context.getExecutedCommand(true);
        if (sender instanceof Player) {
            Player.Spigot player = ((Player) sender).spigot();
            BaseComponent[] title = new ComponentBuilder(
                    "Command Help: " + executedCommand)
                    .color(ChatColor.AQUA).underlined(true).create();
            player.sendMessage(title);
            command.getPermissibleSubCommands(sender).stream().sorted().forEach(
                    cmd -> player.sendMessage(
                            new ComponentBuilder("> ")
                                    .color(ChatColor.YELLOW)
                                    .bold(true)
                                    .event(new ClickEvent(
                                            ClickEvent.Action.RUN_COMMAND,
                                            executedCommand + " " + cmd.getName()))
                                    .append(executedCommand + " " + cmd.getName(),
                                            ComponentBuilder.FormatRetention.NONE)
                                    .color(ChatColor.AQUA)
                                    .event(new ClickEvent(
                                            ClickEvent.Action.SUGGEST_COMMAND,
                                            executedCommand + " " + cmd.getName()))
                                    .create()));
        } else {
            command.getPermissibleSubCommands(sender).stream().sorted().forEach(
                    cmd -> sender.sendMessage(executedCommand + " " + cmd.getName()));
        }

    }
}
