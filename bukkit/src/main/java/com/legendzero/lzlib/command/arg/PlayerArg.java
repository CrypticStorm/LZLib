package com.legendzero.lzlib.command.arg;

import com.google.common.collect.Lists;
import com.legendzero.lzlib.command.CommandContext;
import com.legendzero.lzlib.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class PlayerArg implements CommandArg {

    public static BiPredicate<? super CommandSender, ? super Player> EXCLUDE_SELF
            = (sender, player) -> sender != player;
    public static BiPredicate<? super CommandSender, ? super Player> CAN_SEE
            = (sender, player) -> (sender instanceof Player) && ((Player) sender).canSee(player);

    private final String contextKey;
    private final boolean infinite;
    private final BiPredicate<? super CommandSender, ? super Player> predicate;

    public PlayerArg(String contextKey) {
        this(contextKey, false);
    }

    public PlayerArg(String contextKey, boolean infinite) {
        this(contextKey, infinite, (s, p) -> true);
    }

    public PlayerArg(String contextKey,
                     BiPredicate<? super CommandSender, ? super Player> predicate) {
        this(contextKey, false, predicate);
    }

    public PlayerArg(String contextKey, boolean infinite,
                     BiPredicate<? super CommandSender, ? super Player> predicate) {
        this.contextKey = contextKey;
        this.infinite = infinite;
        this.predicate = predicate;
    }

    public Status execute(SubCommand command, CommandContext context) {
        if (this.infinite) {
            List<Player> players = Lists.newArrayList();
            while (context.hasNextArgument()) {
                String argument = context.nextArgument();
                Player player = Bukkit.getPlayer(argument);
                if (player != null && this.predicate.test(context.getSender(), player)) {
                    players.add(player);
                } else {
                    context.previousArgument();
                }
            }
            context.putContext(this.contextKey, players);
            if (players.size() > 0) {
                return Status.PARSED_ARGUMENTS;
            } else {
                return Status.SKIPPED_ARGUMENTS;
            }
        }
        if (context.hasNextArgument()) {
            String argument = context.nextArgument();
            Player player = Bukkit.getPlayer(argument);
            if (player != null && this.predicate.test(context.getSender(), player)) {
                context.putContext(this.contextKey, player);
                context.markArgument();
                return Status.PARSED_ARGUMENTS;
            } else {
                return Status.INVALID_ARGUMENTS;
            }
        }
        return Status.NOT_ENOUGH_ARGUMENTS;
    }

    @Override
    public List<String> tabComplete(SubCommand command, CommandContext context) {
        String prefix = context.hasNextArgument() ? context.peekArgument() : "";
        return Bukkit.getOnlinePlayers().stream().filter(player
                -> this.predicate.test(context.getSender(), player))
                .map(Player::getName).filter(name
                        -> name.startsWith(prefix))
                .collect(Collectors.toList());
    }
}
