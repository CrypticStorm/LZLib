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

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Arrays;
import java.util.function.Consumer;

public interface GuiClickHandler extends Consumer<InventoryClickEvent> {

    static GuiClickHandler performCommands(String... commands) {
        return event -> Arrays.stream(commands).forEach(cmd -> ((Player) event.getWhoClicked()).performCommand(cmd));
    }

    static GuiClickHandler performLRCommands(String[] leftCommands, String[] rightCommands) {
        return event -> {
            if (event.getClick().isLeftClick()) {
                Arrays.stream(leftCommands).forEach(cmd -> ((Player) event.getWhoClicked()).performCommand(cmd));
            } else if (event.getClick().isRightClick()) {
                Arrays.stream(rightCommands).forEach(cmd -> ((Player) event.getWhoClicked()).performCommand(cmd));
            }
        };
    }
}
