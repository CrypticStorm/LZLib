package com.legendzero.lzlib.command;

import com.google.common.collect.Lists;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.stream.Collectors;

public class CommandHandler {

    public SubCommand getSubCommand(SubCommand command, CommandContext context) {
        if (!context.hasNextArgument()) {
            throw new IllegalArgumentException("No remaining arguments");
        }
        String argument = context.nextArgument().toLowerCase();
        SubCommand nextCommand = command.getCommandMap().getOrDefault(argument,
                command.getAliasMap().get(argument));
        if (nextCommand != null) {
            context.markArgument();
        }
        return nextCommand;
    }

    public boolean execute(SubCommand command, CommandContext context) {
        Commands.showHelp(command, context);
        return true;
    }

    public List<String> tabComplete(SubCommand command, CommandContext context) {
        return command.getPermissibleSubCommands(context).stream()
                .map(SubCommand::name).collect(Collectors.toList());
    }
}
