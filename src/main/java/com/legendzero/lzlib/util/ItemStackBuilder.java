package com.legendzero.lzlib.util;

import com.google.common.collect.Lists;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Iterator;
import java.util.Map;

public final class ItemStackBuilder implements Cloneable {

    private final ItemStack itemStack;

    public ItemStackBuilder() {
        this(new ItemStack(Material.AIR));
    }

    public ItemStackBuilder(ItemStackBuilder itemStackBuilder) {
        this.itemStack = itemStackBuilder.getItemStack();
    }

    public ItemStackBuilder(ItemStack itemStack) {
        this.itemStack = itemStack.clone();
    }

    @Override
    public ItemStackBuilder clone() {
        return new ItemStackBuilder(this);
    }

    public ItemStack getItemStack() {
        return this.itemStack.clone();
    }

    public ItemStackBuilder setType(Material material) {
        this.itemStack.setType(material);
        return this;
    }

    public ItemStackBuilder setAmount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public ItemStackBuilder setDurability(short durability) {
        this.itemStack.setDurability(durability);
        return this;
    }

    public ItemStackBuilder setData(MaterialData materialData) {
        this.itemStack.setData(materialData);
        return this;
    }

    public ItemStackBuilder addEnchantment(Enchantment ench, int level) {
        this.itemStack.addEnchantment(ench, level);
        return this;
    }


    public ItemStackBuilder addEnchantments(Map<Enchantment, Integer> enchantments) {
        this.itemStack.addEnchantments(enchantments);
        return this;
    }

    public ItemStackBuilder addUnsafeEnchantment(Enchantment ench, int level) {
        this.itemStack.addUnsafeEnchantment(ench, level);
        return this;
    }

    public ItemStackBuilder addUnsafeEnchantments(Map<Enchantment, Integer> enchantments) {
        this.itemStack.addUnsafeEnchantments(enchantments);
        return this;
    }

    public ItemStackBuilder removeEnchantment(Enchantment ench) {
        this.itemStack.removeEnchantment(ench);
        return this;
    }

    public ItemStackBuilder setItemMeta(ItemMeta itemMeta) {
        this.itemStack.setItemMeta(itemMeta.clone());
        return this;
    }

    public ItemStackBuilder setDisplayName(String displayName) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemStackBuilder addItemFlags(ItemFlag... itemFlags) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.addItemFlags(itemFlags);
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemStackBuilder removeItemFlags(ItemFlag... itemFlags) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.removeItemFlags(itemFlags);
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemStackBuilder setLore(String... lore) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.setLore(Lists.newArrayList(lore));
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemStackBuilder setLore(Iterable<String> lore) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.setLore(Lists.newArrayList(lore));
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemStackBuilder setLore(Iterator<String> lore) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.setLore(Lists.newArrayList(lore));
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemStackBuilder setUnbreakable(boolean unbreakable) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.spigot().setUnbreakable(unbreakable);
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemStackBuilder addPattern(Pattern pattern) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta instanceof BannerMeta) {
            ((BannerMeta) itemMeta).addPattern(pattern);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    public ItemStackBuilder setBaseColor(DyeColor color) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta instanceof BannerMeta) {
            ((BannerMeta) itemMeta).setBaseColor(color);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    public ItemStackBuilder setPattern(int i, Pattern pattern) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta instanceof BannerMeta) {
            ((BannerMeta) itemMeta).setPattern(i, pattern);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    public ItemStackBuilder setPatterns(Pattern... patterns) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta instanceof BannerMeta) {
            ((BannerMeta) itemMeta).setPatterns(Lists.newArrayList(patterns));
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    public ItemStackBuilder setPatterns(Iterable<Pattern> patterns) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta instanceof BannerMeta) {
            ((BannerMeta) itemMeta).setPatterns(Lists.newArrayList(patterns));
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    public ItemStackBuilder setPatterns(Iterator<Pattern> patterns) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta instanceof BannerMeta) {
            ((BannerMeta) itemMeta).setPatterns(Lists.newArrayList(patterns));
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    public ItemStackBuilder addStoredEnchant(Enchantment ench, int level,
                                                 boolean ignoreLevelRestriction) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta instanceof EnchantmentStorageMeta) {
            ((EnchantmentStorageMeta) itemMeta).addStoredEnchant(ench, level, ignoreLevelRestriction);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    public ItemStackBuilder removeStoredEnchant(Enchantment ench) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta instanceof EnchantmentStorageMeta) {
            ((EnchantmentStorageMeta) itemMeta).removeStoredEnchant(ench);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    public ItemStackBuilder setEffect(FireworkEffect effect) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta instanceof FireworkEffectMeta) {
            ((FireworkEffectMeta) itemMeta).setEffect(effect);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    public ItemStackBuilder addEffect(FireworkEffect effect) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta instanceof FireworkMeta) {
            ((FireworkMeta) itemMeta).addEffect(effect);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    public ItemStackBuilder addEffects(FireworkEffect... effects) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta instanceof FireworkMeta) {
            ((FireworkMeta) itemMeta).addEffects(effects);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    public ItemStackBuilder addEffects(Iterable<FireworkEffect> effects) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta instanceof FireworkMeta) {
            ((FireworkMeta) itemMeta).addEffects(effects);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    public ItemStackBuilder addEffects(Iterator<FireworkEffect> effects) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta instanceof FireworkMeta) {
            ((FireworkMeta) itemMeta).addEffects(Lists.newArrayList(effects));
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    public ItemStackBuilder removeEffect(int index) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta instanceof FireworkMeta) {
            ((FireworkMeta) itemMeta).removeEffect(index);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    public ItemStackBuilder setPower(int power) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta instanceof FireworkMeta) {
            ((FireworkMeta) itemMeta).setPower(power);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    public ItemStackBuilder setColor(Color color) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta instanceof LeatherArmorMeta) {
            ((LeatherArmorMeta) itemMeta).setColor(color);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    public ItemStackBuilder setScaling(boolean value) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta instanceof MapMeta) {
            ((MapMeta) itemMeta).setScaling(value);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    public ItemStackBuilder addCustomEffect(PotionEffect effect, boolean overwrite) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta instanceof PotionMeta) {
            ((PotionMeta) itemMeta).addCustomEffect(effect, overwrite);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    public ItemStackBuilder clearCustomEffects() {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta instanceof PotionMeta) {
            ((PotionMeta) itemMeta).clearCustomEffects();
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    public ItemStackBuilder removeCustomEffect(PotionEffectType type) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta instanceof PotionMeta) {
            ((PotionMeta) itemMeta).removeCustomEffect(type);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    public ItemStackBuilder setMainEffect(PotionEffectType type) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta instanceof PotionMeta) {
            ((PotionMeta) itemMeta).setMainEffect(type);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    public ItemStackBuilder setOwner(String owner) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta instanceof SkullMeta) {
            ((SkullMeta) itemMeta).setOwner(owner);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }
}
