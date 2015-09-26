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

package com.legendzero.lzlib.gui;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GuiUtils {

    static <T> BiFunction<Player, T, ItemStack> staticItem(ItemStack itemStack) {
        return ((player, data) -> itemStack);
    }

    static <T> BiConsumer<InventoryClickEvent, T> performCommands(
            String... commands) {
        return (event, data) -> Arrays.stream(commands).forEach(cmd
                -> ((Player) event.getWhoClicked())
                .performCommand(cmd.replaceAll("%player%",
                        event.getWhoClicked().getName())));
    }

    static <T> BiConsumer<InventoryClickEvent, T> performCommands(
            Iterable<String> commands) {
        return (event, data) -> commands.forEach(cmd
                -> ((Player) event.getWhoClicked())
                .performCommand(cmd.replaceAll("%player%",
                        event.getWhoClicked().getName())));
    }

    static <T> BiConsumer<InventoryClickEvent, T> performCommands(
            Iterator<String> commands) {
        return (event, data) -> commands.forEachRemaining(cmd
                -> ((Player) event.getWhoClicked())
                .performCommand(cmd.replaceAll("%player%",
                        event.getWhoClicked().getName())));
    }

    static <T> BiConsumer<InventoryClickEvent, T> performLRCommands(
            String[] leftCommands, String[] rightCommands) {
        return (event, data) -> {
            if (event.getClick().isLeftClick()) {
                Arrays.stream(leftCommands).forEach(cmd
                        -> ((Player) event.getWhoClicked())
                        .performCommand(cmd.replaceAll("%player%",
                                event.getWhoClicked().getName())));
            } else if (event.getClick().isRightClick()) {
                Arrays.stream(rightCommands).forEach(cmd
                        -> ((Player) event.getWhoClicked())
                        .performCommand(cmd.replaceAll("%player%",
                                event.getWhoClicked().getName())));
            }
        };
    }

    static <T> BiConsumer<InventoryClickEvent, T> performLRCommands(
            Iterable<String> leftCommands, Iterable<String> rightCommands) {
        return (event, data) -> {
            if (event.getClick().isLeftClick()) {
                leftCommands.forEach(cmd -> ((Player) event.getWhoClicked())
                        .performCommand(cmd.replaceAll("%player%",
                                event.getWhoClicked().getName())));
            } else if (event.getClick().isRightClick()) {
                rightCommands.forEach(cmd -> ((Player) event.getWhoClicked())
                        .performCommand(cmd.replaceAll("%player%",
                                event.getWhoClicked().getName())));
            }
        };
    }

    static <T> BiConsumer<InventoryClickEvent, T> performLRCommands(
            Iterator<String> leftCommands, Iterator<String> rightCommands) {
        return (event, data) -> {
            if (event.getClick().isLeftClick()) {
                leftCommands.forEachRemaining(cmd
                        -> ((Player) event.getWhoClicked())
                        .performCommand(cmd.replaceAll("%player%",
                                event.getWhoClicked().getName())));
            } else if (event.getClick().isRightClick()) {
                rightCommands.forEachRemaining(cmd
                        -> ((Player) event.getWhoClicked())
                        .performCommand(cmd.replaceAll("%player%",
                                event.getWhoClicked().getName())));
            }
        };
    }
}
