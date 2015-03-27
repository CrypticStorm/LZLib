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

public class GuiInventoryHolder implements InventoryHolder {

    private final GuiContents guiContents;
    private final Inventory inventory;

    public GuiInventoryHolder(GuiContents guiContents, Player player) {
        this.guiContents = guiContents;
        if (this.guiContents.getType() == null) {
            this.inventory = this.guiContents.getPlugin().getServer()
                    .createInventory(this, this.guiContents.getSize(),
                            this.guiContents.getName());
        } else {
            this.inventory = this.guiContents.getPlugin().getServer()
                    .createInventory(this, this.guiContents.getType(),
                            this.guiContents.getName());
        }
        this.guiContents.update(this.inventory, player);

        if (!Listeners.isRegistered(this.guiContents.getPlugin(), GuiListener.class)) {
            Listeners.register(this.guiContents.getPlugin(),
                    new GuiListener(this.guiContents.getPlugin()));
        }
    }

    public Plugin getPlugin() {
        return this.guiContents.getPlugin();
    }

    public GuiContents getGuiContents() {
        return this.guiContents;
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().equals(event.getClickedInventory()) &&
                event.getCurrentItem() != null) {
            GuiItem item = this.guiContents.getGuiItem(event.getSlot());
            if (item != null) {
                item.onClick(event);
            }
        }

    }
}
