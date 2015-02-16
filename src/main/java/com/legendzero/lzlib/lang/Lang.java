/*
 * Copyright (c) 2015 Legend Zero LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.legendzero.lzlib.lang;

import com.legendzero.lzlib.config.FileConfig;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public interface Lang extends FileConfig {

    String getDefault();

    default String get() {
        return FileConfig.super.get().toString();
    }

    default String simpleFormat(Object... args) {
        return String.format(this.get(), args);
    }

    default String format(Object... args) {
        return String.format(this.defaultFormat(), this.simpleFormat(args));
    }

    String defaultFormat();

    default void sendSimple(CommandSender target, Object... args) {
        target.sendMessage(this.simpleFormat(args));
    }

    default void send(CommandSender target, Object... args) {
        target.sendMessage(this.format(args));
    }

    default void sendRawSimple(Player target, Object... args) {
        target.sendRawMessage(this.simpleFormat(args));
    }

    default void sendRaw(Player target, Object... args) {
        target.sendRawMessage(this.format(args));
    }

    default void logSimple(Logger logger, Level level, Object... args) {
        logger.log(level, this.simpleFormat(args));
    }

    default void log(Logger logger, Level level, Object... args) {
        logger.log(level, this.format(args));
    }

    default void consumeSimple(Consumer<String> consumer, Object... args) {
        consumer.accept(this.simpleFormat(args));
    }

    default void consume(Consumer<String> consumer, Object... args) {
        consumer.accept(this.format(args));
    }
}
