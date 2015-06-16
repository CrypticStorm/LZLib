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

package com.legendzero.lzlib.util;

import com.legendzero.lzlib.command.LZCommand;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.logging.Level;

public final class BukkitReflections {

    private BukkitReflections() {
    }

    public static Plugin getProvidingPlugin(Class<?> clazz) {
        try {
            return JavaPlugin.getProvidingPlugin(clazz);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return null;
        }
    }

    public static PluginCommand getPluginCommand(LZCommand<?> command) {
        try {
            Constructor<PluginCommand> c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            c.setAccessible(true);
            return c.newInstance(command.name(), command.getPlugin());
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            command.getPlugin().getLogger().log(Level.SEVERE, "Error creating Bukkit command", e);
            return null;
        }
    }

    public static CommandMap getCommandMap(LZCommand<?> command) {
        PluginManager pluginManager = command.getPlugin().getServer().getPluginManager();
        CommandMap cMap = null;

        try {
            Field f = pluginManager.getClass().getDeclaredField("commandMap");
            f.setAccessible(true);

            cMap = (CommandMap) f.get(pluginManager);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            command.getPlugin().getLogger().log(Level.SEVERE, "Error getting CommandMap", e);
        }

        return cMap;
    }

    public static boolean registerEnchantment(Enchantment enchantment) {
        if (!Enchantment.isAcceptingRegistrations()) {
            try {
                Field f = Enchantment.class.getDeclaredField("acceptingNew");
                f.setAccessible(true);
                f.set(null, true);
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                getProvidingPlugin(enchantment.getClass()).getLogger().log(Level.SEVERE, "Error making Enchantments acceptable.");
                return false;
            }
        }
        if (Enchantment.getById(enchantment.getId()) != null) {
            return false;
        } else {
            Enchantment.registerEnchantment(enchantment);
            Enchantment.stopAcceptingRegistrations();
            return true;
        }
    }

    public static void unregisterEnchantment(Enchantment enchantment) {
        try {
            Field byId = Enchantment.class.getDeclaredField("byId");
            Field byName = Enchantment.class.getDeclaredField("byName");
            byId.setAccessible(true);
            byName.setAccessible(true);
            Object idMap = byId.get(null);
            Object nameMap = byName.get(null);
            if (idMap instanceof Map) {
                ((Map) idMap).remove(enchantment.getId(), enchantment);
            }

            if (nameMap instanceof Map) {
                ((Map) nameMap).remove(enchantment.getName(), enchantment);
            }
        } catch (ClassCastException | NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            getProvidingPlugin(enchantment.getClass()).getLogger().log(Level.SEVERE, "Error unregistering Enchantment.");
        }
        Enchantment.stopAcceptingRegistrations();
    }
}
