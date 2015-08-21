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

import lombok.NonNull;
import lombok.Value;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class SoundData {

    private final BiConsumer<Player, Location> soundPlayer;

    public SoundData(@NonNull Sound soundEnum, float volume, float pitch) {
        this.soundPlayer = (player, location) -> player.playSound(location, soundEnum, volume, pitch);
    }

    public SoundData(@NonNull String soundString, float volume, float pitch) {
        this.soundPlayer = (player, location) -> player.playSound(location, soundString, volume, pitch);
    }

    public void play(Player player) {
        this.play(player, player.getLocation());
    }

    public void play(Player player, Location location) {
        this.soundPlayer.accept(player, location);
    }

    public void play(Player player, Function<Player, Location> function) {
        this.soundPlayer.accept(player, function.apply(player));
    }

    public void play(Player[] players) {
        Arrays.stream(players).forEach(this::play);
    }

    public void play(Player[] players, Location location) {
        Arrays.stream(players).forEach(player -> this.play(player, location));
    }

    public void play(Player[] players, Function<Player, Location> function) {
        Arrays.stream(players).forEach(player -> this.play(player, function));
    }

    public void play(Iterable<? extends Player> players) {
        players.forEach(this::play);
    }

    public void play(Iterable<? extends Player> players, Location location) {
        players.forEach(player -> this.play(player, location));
    }

    public void play(Iterable<? extends Player> players, Function<Player, Location> function) {
        players.forEach(player -> this.play(player, function));
    }

    public void play(Iterator<? extends Player> players) {
        players.forEachRemaining(this::play);
    }

    public void play(Iterator<? extends Player> players, Location location) {
        players.forEachRemaining(player -> this.play(player, location));
    }

    public void play(Iterator<? extends Player> players, Function<Player, Location> function) {
        players.forEachRemaining(player -> this.play(player, function));
    }
}
