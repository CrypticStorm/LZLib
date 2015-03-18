package com.legendzero.lzlib.event;

import com.legendzero.lzlib.config.Config;
import org.bukkit.event.HandlerList;
import org.bukkit.event.server.ServerEvent;

public class ConfigRegisterEvent extends ServerEvent {

    private static final HandlerList handlers = new HandlerList();
    private final Class<? extends Config> configClass;

    public ConfigRegisterEvent(Class<? extends Config> configClass) {
        this.configClass = configClass;
    }

    public Class<? extends Config> getConfigClass() {
        return this.configClass;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
