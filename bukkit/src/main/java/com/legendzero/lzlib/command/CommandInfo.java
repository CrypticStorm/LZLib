package com.legendzero.lzlib.command;

import com.google.common.collect.ImmutableList;
import com.legendzero.lzlib.command.arg.CommandArg;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Accessors(chain = true, fluent = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CommandInfo {

    @NonNull String name;
    @NonNull List<String> aliases;
    @NonNull List<CommandArg> arguments;
    @NonNull String description;
    @NonNull String usage;
    @NonNull String permission;
    @NonNull String permissionMessage;
    @NonNull CommandHandler handler;

    public CommandInfo(String name, @Singular List<String> aliases,
                       @Singular List<CommandArg> arguments,
                       String description, String usage, String permission,
                       String permissionMessage, CommandHandler handler) {
        this.name = name;
        this.aliases = aliases == null ? ImmutableList.of() : aliases;
        this.arguments = arguments == null ? ImmutableList.of() : arguments;
        this.description = description;
        this.usage = usage;
        this.permission = permission;
        this.permissionMessage = permissionMessage;
        this.handler = handler == null ? new CommandHandler() : handler;
    }
}
