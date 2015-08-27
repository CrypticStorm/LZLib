package com.legendzero.lzlib.command.arg;

import com.legendzero.lzlib.command.CommandContext;
import com.legendzero.lzlib.command.SubCommand;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

public interface CommandArg {

    Status execute(SubCommand command, CommandContext context);

    List<String> tabComplete(SubCommand command, CommandContext context);

    @Getter
    @Accessors(fluent = true)
    @RequiredArgsConstructor
    enum Status {
        NOT_ENOUGH_ARGUMENTS(false),
        INVALID_ARGUMENTS(false),
        PARSED_ARGUMENTS(true),
        SKIPPED_ARGUMENTS(true);

        private final boolean canContinue;
    }
}
