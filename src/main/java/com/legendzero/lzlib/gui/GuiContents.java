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
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class GuiContents {

    private final Plugin plugin;
    private final InventoryType type;
    private final String name;
    private final int size;
    private GuiContents parent;
    private GuiItem[] contents;

    public GuiContents(Plugin plugin, InventoryType type, String name, int size) {
        this.plugin = plugin;
        this.type = type;
        this.name = name;
        this.size = size;
        this.parent = null;
        this.contents = new GuiItem[this.size];
    }

    public Plugin getPlugin() {
        return this.plugin;
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

    public GuiContents getParent() {
        return this.parent;
    }

    public GuiItem[] getContents() {
        return this.contents;
    }

    public GuiItem getGuiItem(int slot) {
        return this.contents[slot];
    }

    public ItemStack[] getItems(Player player) {
        ItemStack[] contents = new ItemStack[this.size];
        for (int i = 0; i < this.size; i++) {
            contents[i] = this.contents[i].getItemStack(player);
        }
        return contents;
    }

    public void setParent(GuiContents parent) {
        this.parent = parent;
    }

    public void setGuiItem(int slot, GuiItem item) {
        this.contents[slot] = item;
    }

    public void fillEmpty(GuiItem item) {
        for (int i = 0; i < this.size; i++) {
            if (this.contents[i] == null) {
                this.contents[i] = item;
            }
        }
    }

    public void open(Player player) {
        InventoryHolder inventoryHolder = new GuiInventoryHolder(this, player);
        Inventory inventory = inventoryHolder.getInventory();
        player.openInventory(inventory);
    }

    public void update(Inventory inventory, Player player) {
        inventory.setContents(this.getItems(player));
    }

    public void back(Player player) {
        if (this.parent != null) {
            this.parent.open(player);
        }
    }

    public void close(Player player) {
        player.closeInventory();
    }
}
