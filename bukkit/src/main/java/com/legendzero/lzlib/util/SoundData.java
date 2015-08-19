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

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Iterator;

public class SoundData {

    private final Sound soundEnum;
    private final String soundString;
    private final float volume;
    private final float pitch;

    public SoundData(Sound soundEnum, float volume, float pitch) {
        this.soundEnum = soundEnum;
        this.soundString = null;
        this.volume = volume;
        this.pitch = pitch;
    }

    public SoundData(String soundString, float volume, float pitch) {
        this.soundEnum = null;
        this.soundString = soundString;
        this.volume = volume;
        this.pitch = pitch;
    }

    public Sound getSoundEnum() {
        return this.soundEnum;
    }

    public String getSoundString() {
        return this.soundString;
    }

    public float getVolume() {
        return this.volume;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void play(Player... players) {
        if (this.soundEnum != null) {
            Arrays.stream(players).forEach(player -> player.playSound(player.getLocation(), this.soundEnum, this.volume, this.pitch));
        } else {
            Arrays.stream(players).forEach(player -> player.playSound(player.getLocation(), this.soundString, this.volume, this.pitch));
        }
    }

    public void play(Iterable<? extends Player> players) {
        if (this.soundEnum != null) {
            players.forEach(player -> player.playSound(player.getLocation(), this.soundEnum, this.volume, this.pitch));
        } else {
            players.forEach(player -> player.playSound(player.getLocation(), this.soundString, this.volume, this.pitch));
        }
    }

    public void play(Iterator<? extends Player> players) {
        if (this.soundEnum != null) {
            players.forEachRemaining(player -> player.playSound(player.getLocation(), this.soundEnum, this.volume, this.pitch));
        } else {
            players.forEachRemaining(player -> player.playSound(player.getLocation(), this.soundString, this.volume, this.pitch));
        }
    }
}
