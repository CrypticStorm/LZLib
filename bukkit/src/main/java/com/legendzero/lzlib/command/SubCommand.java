package com.legendzero.lzlib.command;

import com.legendzero.lzlib.util.BukkitReflections;
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class SubCommand<T extends Plugin> implements PluginIdentifiableCommand, TabExecutor {

    private final String name;
    private final T plugin;
    private final PluginCommand pluginCommand;
    private final Map<String, Comman>

    public SubCommand(String name, T plugin) {
        this.name = name;
        this.plugin = plugin;
        this.pluginCommand = this.registerToBukkit();
    }

    public String getName() {
        return this.name;
    }

    public T getPlugin() {
        return this.plugin;
    }

    public SubCommand<T> setAliases(List<String> aliases) {
        this.pluginCommand.setAliases(aliases);
        return this;
    }

    public SubCommand<T> setDescription(String description) {
        this.pluginCommand.setDescription(description);
        return this;
    }

    public SubCommand<T> setLabel(String label) {
        this.pluginCommand.setLabel(label);
        return this;
    }

    public SubCommand<T> setPermission(String permission) {
        this.pluginCommand.setPermission(permission);
        return this;
    }

    public SubCommand<T> setPermissionMessage(String permissionMessage) {
        this.pluginCommand.setPermissionMessage(permissionMessage);
        return this;
    }

    public SubCommand<T> setUsage(String usage) {
        this.pluginCommand.setUsage(usage);
        return this;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
        return null;
    }

    private PluginCommand registerToBukkit() {
        PluginCommand pluginCommand = this.plugin.getServer().getPluginCommand(this.getName());
        if (pluginCommand == null) {
            pluginCommand = BukkitReflections.getPluginCommand(this);
            CommandMap commandMap = BukkitReflections.getCommandMap(this);
            if (pluginCommand == null || commandMap == null) {
                return null;
            }
            commandMap.register(this.plugin.getName().toLowerCase(), pluginCommand);
        }
        pluginCommand.setExecutor(this);
        pluginCommand.setTabCompleter(this);
        return pluginCommand;
    }
}
