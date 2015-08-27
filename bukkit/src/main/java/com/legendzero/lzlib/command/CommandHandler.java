package com.legendzero.lzlib.command;

import com.google.common.collect.Lists;
import com.legendzero.lzlib.command.CommandContext;
import com.legendzero.lzlib.command.Commands;
import com.legendzero.lzlib.command.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CommandHandler {

    public int processContext(SubCommand command, CommandSender sender, CommandContext context) {
        return 0;
    }

    public SubCommand getSubCommand(SubCommand command, CommandSender sender, CommandContext context) {
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

    public boolean execute(SubCommand command, CommandSender sender, CommandContext context) {
        Commands.showHelp(command, sender, context);
        return true;
    }

    public List<String> tabComplete(SubCommand command, CommandSender sender, CommandContext context) {
        return Lists.newArrayList();
    }
}
