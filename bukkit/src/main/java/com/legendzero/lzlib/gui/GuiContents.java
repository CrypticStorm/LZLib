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

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class GuiContents<T> {

    @Getter private final InventoryType type;
    @Getter private final Function<? super Player, String> name;
    @Getter private final int size;
    private final List<BiFunction<Player, T, ItemStack>> items;
    private final List<BiConsumer<InventoryClickEvent, T>> actions;
    @Getter @Setter private Function<Player, ? extends T> dataFunction;
    @Getter @Setter private GuiContents<?> parent;

    public GuiContents(InventoryType type, int size, Function<? super Player, String> nameFunction, Function<Player, ? extends T> dataFunction) {
        this.type = type;
        this.name = nameFunction;
        this.size = size;
        this.dataFunction = dataFunction;
        this.parent = null;
        this.items = Lists.newArrayList(Collections.nCopies(this.size, null));
        this.actions = Lists.newArrayList(Collections.nCopies(this.size, null));
    }

    public GuiContents(InventoryType type, int size, Function<? super Player, String> nameFunction) {
        this(type, size, nameFunction, null);
    }

    public GuiContents(InventoryType type, int size, String name, Function<Player, ? extends T> dataFunction) {
        this(type, size, player -> name, dataFunction);
    }

    public GuiContents(InventoryType type, int size, String name) {
        this(type, size, name, null);
    }

    public T getData(Player player) {
        return this.dataFunction.apply(player);
    }

    public boolean hasItem(int slot) {
        return this.items.get(slot) != null;
    }

    public boolean hasAction(int slot) {
        return this.actions.get(slot) != null;
    }

    public ItemStack[] getItems(Player player, T data) {
        ItemStack[] contents = new ItemStack[this.size];
        for (int i = 0; i < this.size; i++) {
            contents[i] = this.items.get(i).apply(player, data);
        }
        return contents;
    }

    public void setItem(int slot, BiFunction<Player, T, ItemStack> item) {
        this.items.set(slot, item);
    }

    public BiConsumer<InventoryClickEvent, T> getAction(int slot) {
        return this.actions.get(slot);
    }

    public void setAction(int slot, BiConsumer<InventoryClickEvent, T> action) {
        this.actions.set(slot, action);
    }

    public void set(int slot, BiFunction<Player, T, ItemStack> item,
                    BiConsumer<InventoryClickEvent, T> action) {
        this.setItem(slot, item);
        this.setAction(slot, action);
    }

    public void fillEmpty(BiFunction<Player, T, ItemStack> item) {
        for (int i = 0; i < this.size; i++) {
            if (this.items.get(i) == null) {
                this.items.set(i, item);
            }
        }
    }
}
