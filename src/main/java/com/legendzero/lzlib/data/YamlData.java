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

package com.legendzero.lzlib.data;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class YamlData implements FileData {

    private String identifier;
    private File file;
    private YamlConfiguration data;

    public YamlData(String identifier, File file) {
        this.identifier = identifier;
        this.file = file;
        this.data = YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public File getStorage() {
        return this.file;
    }

    @Override
    public void setStorage(File storage) {
        this.file = storage;
        this.data = YamlConfiguration.loadConfiguration(storage);
    }

    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    @Override
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public Object get(String path) {
        return this.data.get(path);
    }

    @Override
    public Object get(String path, Object def) {
        return this.data.get(path, def);
    }

    @Override
    public boolean isSet(String path) {
        return this.data.isSet(path);
    }

    @Override
    public void set(String path, Object value) {
        this.data.set(path, value);
    }

    @Override
    public boolean save() {
        return this.save(this.file);
    }

    @Override
    public boolean save(File target)  {
        try {
            this.data.save(file);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public YamlConfiguration getData() {
        return this.data;
    }
}
