package com.legendzero.lzlib.command.arg;

import com.legendzero.lzlib.command.CommandContext;
import com.legendzero.lzlib.command.SubCommand;
import org.bukkit.command.CommandSender;

public interface CommandArg {

    int processContext(SubCommand<?> command, CommandSender sender, CommandContext context);
}
