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

    public void play(Iterable<Player> players) {
        if (this.soundEnum != null) {
            players.forEach(player -> player.playSound(player.getLocation(), this.soundEnum, this.volume, this.pitch));
        } else {
            players.forEach(player -> player.playSound(player.getLocation(), this.soundString, this.volume, this.pitch));
        }
    }

    public void play(Iterator<Player> players) {
        if (this.soundEnum != null) {
            players.forEachRemaining(player -> player.playSound(player.getLocation(), this.soundEnum, this.volume, this.pitch));
        } else {
            players.forEachRemaining(player -> player.playSound(player.getLocation(), this.soundString, this.volume, this.pitch));
        }
    }
}
