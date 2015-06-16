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
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class GuiContents {

    private final InventoryType type;
    private final String name;
    private final int size;
    private final Consumer<? super Player> consumer;
    private GuiContents parent;
    private final GuiItem[] itemStackFunctions;
    private final GuiClickHandler[] clickHandlers;

    public GuiContents(InventoryType type, String name, int size, Consumer<? super Player> consumer) {
        this.type = type;
        this.name = name;
        this.size = size;
        this.consumer = consumer;
        this.parent = null;
        this.itemStackFunctions = new GuiItem[this.size];
        this.clickHandlers = new GuiClickHandler[this.size];
    }

    public GuiContents(InventoryType type, String name, int size) {
        this(type, name, size, null);
    }

    public InventoryType getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public int getSize() {
        return this.size;
    }

    public Consumer<? super Player> getConsumer() {
        return this.consumer;
    }

    public GuiContents getParent() {
        return this.parent;
    }

    public boolean hasItem(int slot) {
        return this.itemStackFunctions[slot] != null;
    }

    public boolean hasAction(int slot) {
        return this.clickHandlers[slot] != null;
    }

    public ItemStack[] getItems(Player player) {
        ItemStack[] contents = new ItemStack[this.size];
        for (int i = 0; i < this.size; i++) {
            contents[i] = this.itemStackFunctions[i].apply(player);
        }
        return contents;
    }

    public void setParent(GuiContents parent) {
        this.parent = parent;
    }

    public void setItem(int slot, GuiItem item) {
        this.itemStackFunctions[slot] = item;
    }

    public void setClickHandler(int slot, GuiClickHandler clickHandler) {
        this.clickHandlers[slot] = clickHandler;
    }

    public void set(int slot, GuiItem item, GuiClickHandler clickHandler) {
        this.setItem(slot, item);
        this.setClickHandler(slot, clickHandler);
    }

    public void fillEmpty(GuiItem item) {
        for (int i = 0; i < this.size; i++) {
            if (this.itemStackFunctions[i] == null) {
                this.itemStackFunctions[i] = item;
            }
        }
    }

    public void onClick(InventoryClickEvent event) {
        GuiClickHandler clickHandler = this.clickHandlers[event.getSlot()];
        if (clickHandler != null) {
            clickHandler.accept(event);
        }
    }
}
