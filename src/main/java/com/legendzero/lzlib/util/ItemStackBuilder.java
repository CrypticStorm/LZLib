package com.legendzero.lzlib.util;

import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

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
}
