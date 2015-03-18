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

import com.google.common.base.Joiner;
import com.legendzero.lzlib.config.Config;
import com.legendzero.lzlib.lang.LZLibLang;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public class ConfigCommand extends LZCommand {

    private final ConfigMap configMap;

    public ConfigCommand(Plugin plugin, LZCommand parent) {
        this(plugin, parent, false);
    }

    public ConfigCommand(Plugin plugin, LZCommand parent, boolean autoDetect) {
        super(plugin, parent);
        this.configMap = new ConfigMap(plugin, autoDetect);
    }

    @Override
    protected boolean execute(CommandSender sender, List<String> args) {
        Map<String, Class<? extends Config>> map = this.configMap.getConfigMap();
        Class<? extends Config> configClass;
        switch (args.size()) {
            case 0:
                LZLibLang.CONFIG_FILE_LIST_HEADER.send(sender);
                for (String identifier : map.keySet()) {
                    LZLibLang.CONFIG_FILE_LIST_ITEM.send(sender, identifier);
                }
                break;
            case 1:
                configClass = map.get(args.get(0));
                if (configClass == null) {
                    LZLibLang.CONFIG_ERROR_NOT_FOUND.send(sender);
                } else {
                    LZLibLang.CONFIG_PATH_LIST_HEADER.send(sender);
                    for (Config config : configClass.getEnumConstants()) {
                        LZLibLang.CONFIG_PATH_LIST_ITEM.send(sender, config.getPath());
                    }
                }
                break;
            case 2:
                configClass = map.get(args.get(0));
                if (configClass == null) {
                    LZLibLang.CONFIG_ERROR_NOT_FOUND.send(sender);
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
                configClass = map.get(args.get(0));
                if (configClass == null) {
                    LZLibLang.CONFIG_ERROR_NOT_FOUND.send(sender);
                } else {
                    for (Config config : configClass.getEnumConstants()) {
                        if (args.get(1).equalsIgnoreCase(config.getPath())) {
                            String argString = Joiner.on(' ').join(
                                    args.subList(2, args.size()));
                            Object value = argString;
                            try {
                                value = NumberFormat.getInstance()
                                        .parse(argString);
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

    @Override
    public String name() {
        return "config";
    }

    @Override
    public String[] aliases() {
        return new String[] {"cfg"};
    }

    @Override
    public String description() {
        return "Edit config values for " + this.getPlugin().getName();
    }

    @Override
    public String usage() {
        return " <file> <path> <value>";
    }

    @Override
    public Permission permission() {
        return new Permission(this.getPlugin().getName() + ".cmd." + this.name(),
                this.description(), PermissionDefault.OP);
    }

    public void register(Class<? extends Config> clazz) {
        this.configMap.register(clazz);
    }
}
