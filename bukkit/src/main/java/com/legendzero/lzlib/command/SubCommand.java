package com.legendzero.lzlib.command;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.legendzero.lzlib.command.arg.CommandArg;
import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;
import lombok.experimental.Delegate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SubCommand {

    @Delegate(types = CommandInfo.class)
    private final CommandInfo commandInfo;
    private final Map<String, SubCommand> commandMap;
    private final Map<String, SubCommand> aliasMap;

    @Builder
    private SubCommand(Plugin register, String name, @Singular List<String> aliases,
                       @Singular List<CommandArg> arguments,
                       String description, String usage, String permission,
                       String permissionMessage, CommandHandler handler) {
        this(new CommandInfo(name, aliases, arguments, description, usage,
                permission, permissionMessage, handler), register);
    }

    SubCommand(CommandInfo commandInfo, @NonNull Plugin register) {
        this(commandInfo);
        if (register != null) {
            PluginCommand pluginCommand = Commands.registerPluginCommand(commandInfo.name(), register);
            BukkitHandler bukkitHandler = new BukkitHandler(register);
            pluginCommand.setExecutor(bukkitHandler);
            pluginCommand.setTabCompleter(bukkitHandler);
        }
    }

    SubCommand(CommandInfo commandInfo) {
        this.commandInfo = commandInfo;
        this.commandMap = Maps.newHashMap();
        this.aliasMap = Maps.newHashMap();
    }

    public Map<String, SubCommand> getAliasMap() {
        return this.aliasMap;
    }

    public Map<String, SubCommand> getCommandMap() {
        return this.commandMap;
    }

    public String getPermissionMessage() {
        return this.commandInfo.permissionMessage();
    }

    public Collection<SubCommand> getPermissibleSubCommands(CommandContext context) {
        return this.getSubCommands(context).stream().filter(cmd -> context.getSender().hasPermission(cmd.permission())).collect(Collectors.toSet());
    }

    public Collection<SubCommand> getSubCommands(CommandContext context) {
        return this.commandMap.values();
    }

    public SubCommand register(SubCommand subCommand) {
        this.commandMap.putIfAbsent(subCommand.name(), subCommand);
        this.aliasMap.values().removeIf(cmd -> cmd.equals(subCommand));
        subCommand.aliases().forEach(alias
                -> this.aliasMap.put(alias.toLowerCase(), subCommand));
        return this;
    }

    public SubCommand unregister(String name) {
        this.unregister(this.commandMap.get(name));
        return this;
    }

    public SubCommand unregister(SubCommand subCommand) {
        if (subCommand != null) {
            this.commandMap.remove(subCommand.name(), subCommand);
            this.aliasMap.values().removeIf(cmd -> cmd.equals(subCommand));
        }
        return this;
    }

    public SubCommand getSubCommand(CommandContext context) {
        return this.commandInfo.handler().getSubCommand(this, context);
    }

    public boolean execute(CommandContext context) {
        return this.commandInfo.handler().execute(this, context);
    }

    public List<String> tabComplete(CommandContext context) {
        return this.commandInfo.handler().tabComplete(this, context);
    }

    @Value
    private class BukkitHandler implements TabExecutor {

        @NonNull Plugin plugin;

        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
            return this.onCommand(sender, cmdLabel, Lists.newArrayList(args));
        }

        public boolean onCommand(CommandSender sender, String cmdLabel, List<String> args) {
            CommandContext context = new CommandContext(this.plugin, sender, cmdLabel, args);
            SubCommand current;
            SubCommand next = SubCommand.this;
            do {
                current = next;
                for (CommandArg commandArg : current.arguments()) {
                    CommandArg.Status status = commandArg.execute(current, context);
                    if (!status.canContinue()) {
                        sender.sendMessage(current.usage());
                        return false;
                    }
                }
                next = current.getSubCommand(context);
            } while (current != next && next != null);
            return current.execute(context);
        }

        @Override
        public List<String> onTabComplete(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
            return this.onTabComplete(sender, cmdLabel, Lists.newArrayList(args));
        }

        public List<String> onTabComplete(CommandSender sender, String cmdLabel, List<String> args) {
            CommandContext context = new CommandContext(this.plugin, sender, cmdLabel, args);
            SubCommand current;
            SubCommand next = SubCommand.this;
            do {
                current = next;
                for (CommandArg commandArg : current.arguments()) {
                    CommandArg.Status status = commandArg.execute(current, context);
                    if (!status.canContinue()) {
                        return commandArg.tabComplete(current, context);
                    }
                }
                next = current.getSubCommand(context);
            } while (current != next && next != null);
            return current.tabComplete(context);
        }
    }
}
