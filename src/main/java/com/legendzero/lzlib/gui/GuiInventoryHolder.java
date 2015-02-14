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

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class GuiInventoryHolder implements InventoryHolder {

    private final String name;
    private final InventoryType type;
    private final GuiItem[] items;
    private final Inventory inventory;

    public GuiInventoryHolder(String name, InventoryType type, int rows) {
        this.name = name;
        this.type = type;
        if(this.type == null || this.type == InventoryType.CHEST){
            this.items = new GuiItem[9*rows];
            this.inventory = Bukkit.createInventory(this, this.items.length, this.name);
        } else {
            this.items = new GuiItem[9];
            this.inventory = Bukkit.createInventory(this, this.type);
        }
    }

    public String getName() {
        return this.name;
    }

    public InventoryType getType(){
        return this.type;
    }

    public int getSize() {
        return this.items.length;
    }

    public GuiItem[] getItems() {
        return this.items;
    }

    @Override
    public Inventory getInventory() {
        Inventory inv;
        if(this.type == null || this.type == InventoryType.CHEST){
            inv = Bukkit.createInventory(this, this.items.length, this.name);
        }else {
            inv = Bukkit.createInventory(this, this.type);
        }
        inv.setContents(this.inventory.getContents());
        return inv;
    }

    public GuiItem getItem(int slot) {
        return this.items[slot];
    }

    public void setItem(int slot, GuiItem item) {
        this.items[slot] = item;
        this.inventory.setItem(slot, item.getItemStack());
    }

}
