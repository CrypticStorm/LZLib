package com.legendzero.lzlib.util;

import com.legendzero.lzlib.config.FileConfig;
import com.legendzero.lzlib.data.FileData;
import com.legendzero.lzlib.data.YamlData;
import com.legendzero.lzlib.event.ConfigRegisterEvent;
import com.legendzero.lzlib.lang.LZLibBukkitLang;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Configs {

    public static <T extends Enum & FileConfig> void register(Class<T> registrant) {
        Plugin plugin = Plugins.getPlugin(registrant);
        if (plugin == null) {
            throw new IllegalArgumentException(registrant.getName() +
                    " is not provided by a Plugin or annotated with a PluginClass annotation");
        }
        String identifier = Annotations.getIdentifier(registrant);
        String path = Annotations.getPath(registrant);

        File file = new File(plugin.getDataFolder(), path);

        if (file.getParentFile().mkdirs()) {
            LZLibBukkitLang.FILE_CREATE_SUCCESS.log(plugin.getLogger(), Level.INFO, file.getParentFile().getAbsolutePath());
        }

        try {
            if (file.createNewFile()) {
                LZLibBukkitLang.FILE_CREATE_SUCCESS.log(plugin.getLogger(), Level.INFO, file.getName());
            }
        } catch (IOException e) {
            LZLibBukkitLang.FILE_CREATE_FAILURE.log(plugin.getLogger(), Level.WARNING, file.getName());
        }

        FileData data = new YamlData(identifier, file);
        for (FileConfig config : registrant.getEnumConstants()) {
            config.setData(data);
            if (!config.isSet()) {
                config.setDefault();
            }
        }

        plugin.getServer().getPluginManager().callEvent(new ConfigRegisterEvent(registrant));
    }

}
