package com.legendzero.lzlib.command;

import com.google.common.collect.Lists;
import com.legendzero.lzlib.command.handler.CommandHandler;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true, fluent = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CommandInfo {

    public CommandInfo(String name, String... aliases) {
        this.name = name;
        this.aliases = Lists.newArrayList(aliases);
    }

    @NonNull final String name;
    @NonNull final List<String> aliases;
    @NonNull String description = null;
    @NonNull String usage = null;
    @NonNull String permission = null;
    @NonNull String permissionMessage = null;
    @NonNull CommandHandler commandHandler = new CommandHandler();
}
