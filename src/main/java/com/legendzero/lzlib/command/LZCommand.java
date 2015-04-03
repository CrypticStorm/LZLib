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

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.legendzero.lzlib.lang.LZLibLang;
import com.legendzero.lzlib.util.Reflections;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

public abstract class LZCommand<T extends Plugin> implements TabExecutor, Comparable<LZCommand<?>> {

    private final T plugin;
    private final LZCommand<?> parent;
    private final Map<String, LZCommand<?>> subCommandMap;

    public LZCommand(T plugin, LZCommand<?> parent) {
        this.plugin = plugin;
        this.parent = parent;
        this.subCommandMap = Maps.newHashMap();

        LZLibLang.COMMAND_LOAD.log(this.plugin.getLogger(), Level.INFO, this.getFullName());
        if (this.parent == null) {
            if (this.registerToBukkit()) {
                LZLibLang.COMMAND_REGISTER_SUCCESS.log(this.plugin.getLogger(), Level.INFO, this.name());
            } else {
                LZLibLang.COMMAND_REGISTER_FAILURE.log(this.plugin.getLogger(), Level.INFO, this.name());
            }
        }
    }

    public LZCommand(T plugin) {
        this(plugin, null);
    }

    public final T getPlugin() {
        return this.plugin;
    }

    public final LZCommand<?> getParent() {
        return this.parent;
    }

    public abstract String name();

    public String getFullName() {
        return (this.getParent() == null ? "/" : " ") + this.name();
    }

    public abstract String[] aliases();

    public abstract String description();

    public abstract String usage();

    public abstract Permission permission();

    public final Collection<LZCommand<?>> getSubCommands() {
        return ImmutableSet.copyOf(this.subCommandMap.values());
    }

    public final Collection<LZCommand<?>> getPermissibleSubCommands(Permissible permissible) {
        return this.subCommandMap.values().stream().filter(cmd -> permissible.hasPermission(cmd.permission())).collect(Collectors.toSet());
    }

    public LZCommand<?> getSubCommand(String alias) {
        return this.getSubCommand(null, alias);
    }

    public LZCommand<?> getSubCommand(Permissible permissible, String alias) {
        LZCommand<?> command = this.subCommandMap.get(alias.toLowerCase());
        if (permissible == null) {
            return command;
        } else {
            if (command != null && permissible.hasPermission(command.permission())) {
                return command;
            } else {
                return null;
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String... args) {
        return this.resolveCommand(sender, Arrays.asList(args));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String cmdLabel, String... args) {
        return this.resolveTab(sender, Arrays.asList(args));
    }

    @Override
    public int compareTo(LZCommand<?> other) {
        return this.name().compareToIgnoreCase(other.name());
    }

    private boolean resolveCommand(CommandSender sender, List<String> args) {
        if (!args.isEmpty() && this.subCommandMap.containsKey(args.get(0).toLowerCase())) {
            return this.subCommandMap.get(args.remove(0)).resolveCommand(sender, args);
        } else {
            return this.execute(sender, args);
        }
    }

    protected List<String> resolveTab(CommandSender sender, List<String> args) {
        if (!args.isEmpty() && this.subCommandMap.containsKey(args.get(0).toLowerCase())) {
            return this.subCommandMap.get(args.remove(0)).resolveTab(sender, args);
        } else {
            return this.tabComplete(sender, args);
        }
    }

    protected boolean execute(CommandSender sender, List<String> args) {
        String fullName = getFullName();
        if (sender instanceof Player) {
            Player.Spigot player = ((Player) sender).spigot();
            BaseComponent[] title = new ComponentBuilder(
                    "Command Help: " + fullName + "\n")
                    .color(ChatColor.AQUA).underlined(true).create();
            player.sendMessage(title);
            this.getPermissibleSubCommands(sender).stream().sorted().forEach(
                    command -> player.sendMessage(
                            new ComponentBuilder("> ")
                                    .color(ChatColor.YELLOW)
                                    .bold(true)
                                    .event(new ClickEvent(
                                            ClickEvent.Action.RUN_COMMAND,
                                            fullName))
                                    .append(fullName)
                                    .color(ChatColor.AQUA)
                                    .event(new ClickEvent(
                                            ClickEvent.Action.SUGGEST_COMMAND,
                                            fullName))
                                    .create()));
        } else {
            this.getPermissibleSubCommands(sender).stream().sorted().forEach(
                    command -> sender.sendMessage(fullName));
        }
        return true;
    }

    protected List<String> tabComplete(CommandSender sender, List<String> args) {
        return this.getPermissibleSubCommands(sender).stream().map(LZCommand::name).collect(Collectors.toList());
    }

    public void registerSubCommand(LZCommand<?>... commands) {
        Arrays.stream(commands).forEach(this::registerSubCommand);
    }

    public void registerSubCommand(LZCommand<?> command) {
        this.registerAlias(command.name(), command);
        Arrays.stream(command.aliases()).forEach(alias -> registerAlias(alias, command));
    }

    private void registerAlias(String alias, LZCommand<?> command) {
        this.subCommandMap.putIfAbsent(alias, command);
    }

    public final boolean registerToBukkit() {
        PluginCommand pluginCommand = this.plugin.getServer().getPluginCommand(this.name());
        if (pluginCommand == null) {
            pluginCommand = Reflections.getPluginCommand(this);
            CommandMap commandMap = Reflections.getCommandMap(this);
            if (pluginCommand == null || commandMap == null) {
                return false;
            }
            commandMap.register(this.plugin.getName().toLowerCase(), pluginCommand);
        }
        pluginCommand.setExecutor(this);
        return true;
    }
}