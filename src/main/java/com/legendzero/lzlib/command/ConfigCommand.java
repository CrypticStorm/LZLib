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

package com.legendzero.lzlib.command;

import com.legendzero.lzlib.config.Config;
import com.legendzero.lzlib.config.ConfigHandler;
import com.legendzero.lzlib.interfaces.Commandable;
import com.legendzero.lzlib.interfaces.Configurable;
import com.legendzero.lzlib.lang.LZLibLang;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;

public class ConfigCommand<E extends JavaPlugin & Commandable<E> & Configurable<E>> extends LZCommand<E> {

    public ConfigCommand(E plugin, LZCommand<E> parent, String name, String[] aliases, ConfigHandler<E> configHandler) {
        super(plugin, parent,
                name,
                "Edit config values for " + plugin.getName(),
                (parent == null ? "/" : parent.getFullName()) +
                        " " + name + " <file> <path> <value>",
                aliases, PermissionDefault.OP);
    }

    @Override
    protected boolean execute(CommandSender sender, List<String> args) {
        ConfigHandler<E> configHandler = this.getPlugin().getConfigHandler();
        Class<? extends Config> configClass;
        switch (args.size()) {
            case 0:
                LZLibLang.CONFIG_FILE_LIST_HEADER.send(sender);
                for (String identifier : configHandler.getIdentifiers()) {
                    LZLibLang.CONFIG_FILE_LIST_ITEM.send(sender, identifier);
                }
                break;
            case 1:
                configClass = configHandler.getConfigClass(args.get(0));
                if (configClass == null) {
                    LZLibLang.CONFIG_INVALID.send(sender);
                } else {
                    LZLibLang.CONFIG_PATH_LIST_HEADER.send(sender);
                    for (Config config : configClass.getEnumConstants()) {
                        LZLibLang.CONFIG_PATH_LIST_ITEM.send(sender, config.getPath());
                    }
                }
                break;
            case 2:
                configClass = configHandler.getConfigClass(args.get(0));
                if (configClass == null) {
                    LZLibLang.CONFIG_INVALID.send(sender);
                } else {
                    for (Config config : configClass.getEnumConstants()) {
                        if (args.get(1).equalsIgnoreCase(config.getPath())) {
                            Object value = config.get();
                            LZLibLang.CONFIG_PATH_VALUE.send(sender,
                                    config.getPath(), value,
                                    value.getClass().getName());
                            break;
                        }
                    }
                }
                break;
            default:
                configClass = configHandler.getConfigClass(args.get(0));
                if (configClass == null) {
                    LZLibLang.CONFIG_INVALID.send(sender);
                } else {
                    for (Config config : configClass.getEnumConstants()) {
                        if (args.get(1).equalsIgnoreCase(config.getPath())) {
                            String argString = StringUtils.join(
                                    args.subList(2, args.size()), ' ');
                            Object value = argString;
                            try {
                                Number number = NumberFormat.getInstance()
                                        .parse(argString);
                                value = number;
                            } catch (ParseException e) {
                            }
                            config.set(value);
                            LZLibLang.CONFIG_PATH_VALUE.send(sender,
                                    config.getPath(), value,
                                    value.getClass().getName());
                            break;
                        }
                    }
                }
                break;
        }

        return false;
    }
}
