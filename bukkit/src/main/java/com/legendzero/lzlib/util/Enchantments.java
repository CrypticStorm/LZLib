package com.legendzero.lzlib.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.logging.Level;

@Log
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Enchantments {

    public static boolean registerEnchantment(Enchantment enchantment) {
        if (!Enchantment.isAcceptingRegistrations()) {
            try {
                Field f = Enchantment.class.getDeclaredField("acceptingNew");
                f.setAccessible(true);
                f.set(null, true);
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                LOGGER.log(Level.SEVERE, "Error making Enchantments acceptable.");
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
            LOGGER.log(Level.SEVERE, "Error unregistering Enchantment.");
        }
        Enchantment.stopAcceptingRegistrations();
    }
}
