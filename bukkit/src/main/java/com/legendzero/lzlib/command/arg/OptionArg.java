package com.legendzero.lzlib.command.arg;

import com.legendzero.lzlib.command.CommandContext;
import com.legendzero.lzlib.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class OptionArg implements CommandArg {

    public Status execute(SubCommand command, CommandContext context) {
        if (!context.hasNextArgument()) {
            return 0;
        }
        SubCommand subCommand = command.getSubCommand(context);
        Player player = Bukkit.getPlayer(argument);
        if (player != null && this.predicate.test(sender, player)) {
            context.putContext(this.contextKey, player);
            context.markArgument();
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public List<String> tabComplete(SubCommand command, CommandContext context) {
        return command.getPermissibleSubCommands(context).stream()
                .map(SubCommand::name).collect(Collectors.toList());
    }
}
