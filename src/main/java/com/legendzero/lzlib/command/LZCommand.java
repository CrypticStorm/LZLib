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
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.legendzero.lzlib.interfaces.Commandable;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class LZCommand<E extends Plugin & Commandable<E>> implements Comparable<LZCommand> {

    private final E plugin;
    private final LZCommand<E> parent;
    private final String name;
    private final String fullName;
    private final String description;
    private final String usage;
    private final String[] aliases;
    private final Permission permission;
    private final Map<String, LZCommand<E>> subCommandMap;

    public LZCommand(E plugin, LZCommand<E> parent, String name, String description, String usage, String[] aliases, PermissionDefault permissionDefault) {
        this.plugin = plugin;
        this.parent = parent;
        this.name = name;
        if (this.parent == null) {
            this.fullName = "/" + this.name;
            this.permission = new Permission(this.plugin.getName() + ".cmd." + this.name, permissionDefault);
        } else {
            this.fullName = this.parent.fullName + " " + this.name;
            this.permission = new Permission(this.plugin.getName() + ".cmd." + this.name, permissionDefault);
        }
        this.description = description;
        this.usage = usage;
        this.aliases = aliases;

        this.subCommandMap = Maps.newHashMap();
    }

    @Override
    public int compareTo(LZCommand other) {
        return this.name.compareTo(other.name);
    }

    public void addSubCommand(LZCommand<E> command) {
        this.registerAlias(command.getName(), command);
        Arrays.stream(command.getAliases()).forEach(alias -> registerAlias(alias, command));
    }

    private void registerAlias(String alias, LZCommand<E> command) {
        this.subCommandMap.putIfAbsent(alias, command);
    }

    public LZCommand<E> getSubCommand(Permissible permissible, String alias) {
        LZCommand<E> command = this.subCommandMap.get(alias.toLowerCase());
        if (permissible == null) {
            return command;
        } else {
            if (command != null && permissible.hasPermission(command.getPermission())) {
                return command;
            } else {
                return null;
            }
        }
    }

    public boolean resolveCommand(CommandSender sender, List<String> args) {
        if (!args.isEmpty() && this.subCommandMap.containsKey(args.get(0).toLowerCase())) {
            return this.subCommandMap.get(args.remove(0)).resolveCommand(sender, args);
        } else {
            return this.execute(sender, args);
        }
    }

    public List<String> resolveTab(CommandSender sender, List<String> args) {
        if (this.subCommandMap.isEmpty()) {
            return this.tabComplete(sender, args);
        } else if (!args.isEmpty()) {
            if (this.subCommandMap.containsKey(args.get(0).toLowerCase())) {
                return this.subCommandMap.get(args.remove(0)).resolveTab(sender, args);
            } else if (args.size() == 1) {
                String arg = args.get(0);
                return Lists.newArrayList(this.subCommandMap.keySet().stream().filter(cmd -> cmd.startsWith(arg)).iterator());
            } else {
                return Lists.newArrayList();
            }
        } else {
            return Lists.newArrayList(this.subCommandMap.keySet().iterator());
        }
    }

    protected abstract boolean execute(CommandSender sender, List<String> args);

    protected List<String> tabComplete(CommandSender sender, List<String> args) {
        return Lists.newArrayList();
    }

    public final LZCommand<E> getParent() {
        return this.parent;
    }

    public final E getPlugin() {
        return this.plugin;
    }

    public final String getName() {
        return this.name;
    }

    public final String getFullName() {
        return this.fullName;
    }

    public final String getDescription() {
        return this.description;
    }

    public final String getUsage() {
        return this.usage;
    }

    public final String[] getAliases() {
        return this.aliases;
    }

    public final Permission getPermission() {
        return this.permission;
    }

    public final Collection<LZCommand<E>> getSubCommands() {
        return ImmutableSet.copyOf(this.subCommandMap.values());
    }

    public final Collection<LZCommand<E>> getPermissibleSubCommands(Permissible permissible) {
        return ImmutableSet.copyOf(this.subCommandMap.values().stream().filter(cmd -> permissible.hasPermission(cmd.permission)).iterator());
    }

}