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

import com.legendzero.lzlib.util.Listeners;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;

public class GuiInventoryHolder<T> implements InventoryHolder {

    private final Plugin plugin;
    private final GuiContents<T> guiContents;
    private final Inventory inventory;
    private T data;

    public GuiInventoryHolder(Plugin plugin, GuiContents<T> guiContents, Player player) {
        this.plugin = plugin;
        this.guiContents = guiContents;
        if (this.guiContents.getType() == null) {
            this.inventory = this.plugin.getServer().createInventory(this,
                    this.guiContents.getSize(),
                    this.guiContents.getName().apply(player));
        } else {
            this.inventory = this.plugin.getServer().createInventory(this,
                    this.guiContents.getType(),
                    this.guiContents.getName().apply(player));
        }
        this.data = this.guiContents.getData(player);
        this.inventory.setContents(this.guiContents.getItems(player, this.data));

        if (!Listeners.isRegistered(this.plugin, GuiListener.class)) {
            Listeners.register(this.plugin, new GuiListener(this.plugin));
        }
    }

    public Plugin getPlugin() {
        return this.plugin;
    }

    public GuiContents<T> getGuiContents() {
        return this.guiContents;
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().equals(event.getClickedInventory())) {
            if (this.guiContents.hasAction(event.getSlot())) {
                this.guiContents.getAction(event.getSlot()).accept(event, this.data);
            }
        }

    }
}
