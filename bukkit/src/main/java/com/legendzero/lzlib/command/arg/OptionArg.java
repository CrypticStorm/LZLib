package com.legendzero.lzlib.command.arg;

import com.google.common.collect.BiMap;
import com.legendzero.lzlib.command.CommandContext;
import com.legendzero.lzlib.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class OptionArg<T> implements CommandArg {

    private final String contextKey;
    private final Supplier<? extends Collection<? extends T>> collection;
    private final Function<? super T, String> stringFunction;
    private final Function<String, ? extends T> contextFunction;

    public OptionArg(String contextKey,Collection<? extends T> collection,
                     Function<? super T, String> stringFunction) {
        this(contextKey, () -> collection, stringFunction);
    }

    public OptionArg(String contextKey, Map<String, ? extends T> map,
                     Function<? super T, String> stringFunction) {
        this(contextKey, map::values, stringFunction, map::get);

    }

    public OptionArg(String contextKey, BiMap<String, ? extends T> map) {
        this(contextKey, map::values, map.inverse()::get, map::get);
    }

    public OptionArg(String contextKey,
                     Supplier<? extends Collection<? extends T>> supplier,
                     Function<? super T, String> stringFunction) {
        this(contextKey, supplier, stringFunction, string
                -> supplier.get().stream()
                .filter(object -> stringFunction.apply(object).equals(string))
                .findAny().orElse(null));
    }

    public OptionArg(String contextKey,
                     Supplier<? extends Collection<? extends T>> supplier,
                     Function<? super T, String> stringFunction,
                     Function<String, ? extends T> contextFunction) {
        this.contextKey = contextKey;
        this.collection = supplier;
        this.stringFunction = stringFunction;
        this.contextFunction = contextFunction;
    }

    public Status execute(SubCommand command, CommandContext context) {
        if (!context.hasNextArgument()) {
            return Status.NOT_ENOUGH_ARGUMENTS;
        }
        String argument = context.nextArgument();
        T object = this.contextFunction.apply(argument);
        if (object != null) {
            context.putContext(this.contextKey, object);
            context.markArgument();
            return Status.PARSED_ARGUMENTS;
        } else {
            context.previousArgument();
            return Status.INVALID_ARGUMENTS;
        }
    }

    @Override
    public List<String> tabComplete(SubCommand command, CommandContext context) {
        return this.collection.get().stream().map(this.stringFunction)
                .collect(Collectors.toList());
    }
}
