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

package com.legendzero.lzlib.lang;

import com.legendzero.lzlib.data.FileData;
import com.legendzero.lzlib.util.Configs;

import static org.bukkit.ChatColor.*;

public enum LZLibBukkitLang implements BukkitLang {

    COMMAND_LOAD("command.load", "Loading %s"),
    COMMAND_REGISTER_SUCCESS("command.register.success", "Registered %s to Bukkit."),
    COMMAND_REGISTER_FAILURE("command.register.failure", "Failed to register %s to Bukkit."),

    CONFIG_ERROR_NOT_FOUND("config.error.notfound", RED + "Invalid config file"),
    CONFIG_ERROR_NOT_ENUM("config.error.notenum", RED + "%s is not an enum."),
    CONFIG_ERROR_NOT_ANNOTATED("config.error.notenum", RED + "%s has no FilePath annotation."),

    CONFIG_FILE_LIST_HEADER("config.filelist.header", YELLOW + "Loaded config files:"),
    CONFIG_FILE_LIST_ITEM("config.filelist.item", GRAY + "> " + RESET + "%s"),
    CONFIG_PATH_LIST_HEADER("config.pathlist.header", YELLOW + "Configurable data:"),
    CONFIG_PATH_LIST_ITEM("config.pathlist.item", GRAY + "> " + RESET + "%s"),
    CONFIG_PATH_VALUE("config.pathvalue", "%s " + GRAY + ">" + RESET + " %s " + GRAY + "(%s)"),

    FILE_CREATE_FAILURE("file.create.failure", "Failed to create file: %s"),
    FILE_CREATE_SUCCESS("file.create.success", "Successfully created file: %s"),

    FORMAT("format", GOLD + "[" +RED + "LZLib" +
            GOLD + "]" + RESET)
    ;

    static {
        Configs.register(LZLibBukkitLang.class);
    }

    private final String path;
    private final String def;
    private FileData data;

    private LZLibBukkitLang(String path, String def) {
        this.path = path;
        this.def = def;
    }

    @Override
    public FileData getData() {
        return this.data;
    }

    @Override
    public void setData(FileData data) {
        this.data = data;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public String getDefault() {
        return this.def;
    }

    @Override
    public String defaultFormat() {
        return FORMAT.get();
    }
}
