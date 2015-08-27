package com.legendzero.lzlib.command.arg.status;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ArgStatus {

    private final boolean success;
    private final boolean required;
    private final int argsParsed;

    public static ArgStatus OPTIONAL_SUCCESS(int argsParsed) {
        return new ArgStatus(true, false, argsParsed);
    }

    public static ArgStatus OPTIONAL_FAILURE() {
        return new ArgStatus(false, false, 0);
    }

    public static ArgStatus REQUIRED_SUCCESS(int argsParsed) {
        return new ArgStatus(true, true, argsParsed);
    }

    public static ArgStatus REQUIRED_FAILURE() {
        return new ArgStatus(false, true, 0);
    }
}
