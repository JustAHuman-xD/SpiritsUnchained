package me.justahuman.spiritsunchained.slimefun;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.UnplaceableBlock;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Items {
    public static void setup(SlimefunAddon instance) {
        new UnplaceableBlock(Groups.SUN_RESOURCES, ItemStacks.SPS_INFUSED_MEMBRANE, RecipeType.MAGIC_WORKBENCH, new ItemStack[]{
                SlimefunItems.ENDER_LUMP_3, null, SlimefunItems.ENDER_LUMP_3,
                null, new ItemStack(Material.PHANTOM_MEMBRANE), null,
                SlimefunItems.ENDER_LUMP_3, null, SlimefunItems.ENDER_LUMP_3})
                .register(instance);

        new UnplaceableBlock(Groups.SUN_RESOURCES, ItemStacks.SPS_INFUSED_FEATHER, RecipeType.MAGIC_WORKBENCH, new ItemStack[]{
                SlimefunItems.ENDER_LUMP_3, null, SlimefunItems.ENDER_LUMP_3,
                null, new ItemStack(Material.FEATHER), null,
                SlimefunItems.ENDER_LUMP_3, null, SlimefunItems.ENDER_LUMP_3})
                .register(instance);

        new UnplaceableBlock(Groups.SUN_RESOURCES, ItemStacks.SPS_SOUL_STAINED_GLASS, RecipeType.ANCIENT_ALTAR, new ItemStack[]{
                SlimefunItems.ENDER_LUMP_3, SlimefunItems.ESSENCE_OF_AFTERLIFE, SlimefunItems.ENDER_LUMP_3,
                SlimefunItems.ENDER_RUNE, new ItemStack(Material.TINTED_GLASS), SlimefunItems.ENDER_RUNE,
                SlimefunItems.ENDER_LUMP_3, SlimefunItems.ESSENCE_OF_AFTERLIFE, SlimefunItems.ENDER_LUMP_3})
                .register(instance);
    }
}
