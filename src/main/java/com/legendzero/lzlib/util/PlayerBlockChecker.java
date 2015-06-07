package com.legendzero.lzlib.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

/**
 *
 * @author CrypticStorm
 */
public final class PlayerBlockChecker {

    private PlayerBlockChecker() {
    }

    public static final float PLAYER_HEIGHT = 1.8f;
    public static final float PLAYER_WIDTH = 0.6f;
    public static final float PLAYER_RADIUS = PLAYER_WIDTH / 2.0f;

    public static boolean isClimbing(Entity entity) {
        Material material = entity.getWorld().getBlockAt(entity.getLocation()).getType();
        return material == Material.LADDER || material == Material.VINE;
    }

    public static boolean isSwimming(Entity entity) {
        Location location = entity.getLocation();
        World world = entity.getWorld();

        int x1 = (int) Math.floor(location.getX() - PLAYER_RADIUS);
        int y1 = (int) Math.floor(location.getY());
        int z1 = (int) Math.floor(location.getZ() - PLAYER_RADIUS);

        int x2 = (int) Math.floor(location.getX() + PLAYER_RADIUS);
        int y2 = (int) Math.floor(location.getY() + PLAYER_HEIGHT);
        int z2 = (int) Math.floor(location.getZ() + PLAYER_RADIUS);

        for (int x = x1; x <= x2; ++x) {
            for (int y = y1; y <= y2; ++y) {
                for (int z = z1; z <= z2; ++z) {
                    Block block = world.getBlockAt(x, y, z);
                    if (block != null) {
                        if (block.isLiquid()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static Collection<Block> getCollidedBlocks(Entity entity, Material material, Material... otherMaterials) {
        Set<Material> materials = Sets.immutableEnumSet(material, otherMaterials);
        Location location = entity.getLocation();
        World world = entity.getWorld();

        int x1 = (int) Math.floor(location.getX() - PLAYER_RADIUS);
        int y1 = (int) Math.floor(location.getY());
        int z1 = (int) Math.floor(location.getZ() - PLAYER_RADIUS);

        int x2 = (int) Math.floor(location.getX() + PLAYER_RADIUS);
        int y2 = (int) Math.floor(location.getY() + PLAYER_HEIGHT);
        int z2 = (int) Math.floor(location.getZ() + PLAYER_RADIUS);

        Collection<Block> collidedBlocks = Lists.newLinkedList();
        for (int x = x1; x <= x2; ++x) {
            for (int y = y1; y <= y2; ++y) {
                for (int z = z1; z <= z2; ++z) {
                    Block block = world.getBlockAt(x, y, z);
                    if (block != null) {
                        if (materials.contains(block.getType())) {
                            collidedBlocks.add(block);
                        }
                    }
                }
            }
        }
        return collidedBlocks;
    }

}
