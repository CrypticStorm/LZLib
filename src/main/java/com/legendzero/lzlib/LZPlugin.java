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

package com.legendzero.lzlib;

import com.legendzero.lzlib.command.CommandReflector;
import com.legendzero.lzlib.gui.GuiService;
import com.legendzero.lzlib.service.Service;
import com.legendzero.lzlib.util.Services;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Function;

public abstract class LZPlugin extends JavaPlugin {


    @Override
    public final void onLoad() {
        this.load();
    }

    protected abstract void load();

    @Override
    public final void onEnable() {
        this.loadCommands();

        this.enable();
    }

    protected abstract void enable();

    @Override
    public final void onDisable() {
        this.getServer().getServicesManager().getRegistrations(this).stream()
                .filter(rsp -> rsp.getProvider() instanceof Service)
                .map(Service.class::cast).forEach(Service::uninitialize);

        this.disable();
    }

    protected abstract void disable();

    public abstract void loadCommands();
}
