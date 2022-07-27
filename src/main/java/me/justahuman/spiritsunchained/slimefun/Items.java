package me.justahuman.spiritsunchained.slimefun;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.UnplaceableBlock;

import me.justahuman.spiritsunchained.implementation.multiblocks.*;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Items {
    public static void setup(SlimefunAddon instance) {

        // Crafting Materials
        new UnplaceableBlock(Groups.SUN_RESOURCES, ItemStacks.SUN_INFUSED_MEMBRANE, RecipeType.MAGIC_WORKBENCH, new ItemStack[]{
                SlimefunItems.ENDER_LUMP_3, null, SlimefunItems.ENDER_LUMP_3,
                null, new ItemStack(Material.PHANTOM_MEMBRANE), null,
                SlimefunItems.ENDER_LUMP_3, null, SlimefunItems.ENDER_LUMP_3})
                .register(instance);

        new UnplaceableBlock(Groups.SUN_RESOURCES, ItemStacks.SUN_INFUSED_FEATHER, RecipeType.MAGIC_WORKBENCH, new ItemStack[]{
                SlimefunItems.ENDER_LUMP_3, null, SlimefunItems.ENDER_LUMP_3,
                null, new ItemStack(Material.FEATHER), null,
                SlimefunItems.ENDER_LUMP_3, null, SlimefunItems.ENDER_LUMP_3})
                .register(instance);

        new UnplaceableBlock(Groups.SUN_RESOURCES, ItemStacks.SUN_SOUL_STAINED_GLASS, RecipeType.ANCIENT_ALTAR, new ItemStack[]{
                SlimefunItems.ENDER_LUMP_3, SlimefunItems.ESSENCE_OF_AFTERLIFE, SlimefunItems.ENDER_LUMP_3,
                SlimefunItems.ENDER_RUNE, new ItemStack(Material.TINTED_GLASS), SlimefunItems.ENDER_RUNE,
                SlimefunItems.ENDER_LUMP_3, SlimefunItems.ESSENCE_OF_AFTERLIFE, SlimefunItems.ENDER_LUMP_3})
                .register(instance);

        new UnplaceableBlock(Groups.SUN_RESOURCES, ItemStacks.SUN_ECTOPLASM, RecipeType.NULL, new ItemStack[]{
                null, null, null,
                null, null, null,
                null, null, null})
                .register(instance);

        //Altar Blocks
        //Tier 1
        new SpiritualAltarPiece1(ItemStacks.SUN_CHARGED_QUARTZ_I, RecipeType.ANCIENT_ALTAR, new ItemStack[] {
                ItemStacks.SUN_INFUSED_MEMBRANE, ItemStacks.SUN_ECTOPLASM, new ItemStack(Material.QUARTZ_BLOCK),
                ItemStacks.SUN_ECTOPLASM, SlimefunItems.BLANK_RUNE, ItemStacks.SUN_ECTOPLASM,
                new ItemStack(Material.QUARTZ_BLOCK), ItemStacks.SUN_ECTOPLASM, ItemStacks.SUN_INFUSED_MEMBRANE
        }, 8).register(instance);

        new SpiritualAltarPiece1(ItemStacks.SUN_CHARGED_STAIRS_I, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                null, null, null,
                null, ItemStacks.SUN_CHARGED_QUARTZ_I, null,
                ItemStacks.SUN_CHARGED_QUARTZ_I, ItemStacks.SUN_CHARGED_QUARTZ_I, ItemStacks.SUN_CHARGED_QUARTZ_I
        }, 4).register(instance);

        new Tier1Altar().register(instance);

        //Tier 2
        new SpiritualAltarPiece2(ItemStacks.SUN_CHARGED_QUARTZ_II, RecipeType.ANCIENT_ALTAR, new ItemStack[] {
                ItemStacks.SUN_INFUSED_MEMBRANE, ItemStacks.SUN_ECTOPLASM, ItemStacks.SUN_CHARGED_QUARTZ_I,
                ItemStacks.SUN_ECTOPLASM, SlimefunItems.BLANK_RUNE, ItemStacks.SUN_ECTOPLASM,
                ItemStacks.SUN_CHARGED_QUARTZ_I, ItemStacks.SUN_ECTOPLASM, ItemStacks.SUN_INFUSED_MEMBRANE
        }, 8).register(instance);

        new SpiritualAltarPiece2(ItemStacks.SUN_CHARGED_PILLAR_II, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                ItemStacks.SUN_CHARGED_QUARTZ_II, null, null,
                null, null, null,
                null, null, null
        }, 1).register(instance);

        new SpiritualAltarPiece2(ItemStacks.SUN_CHARGED_STAIRS_II, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                null, null, null,
                null, ItemStacks.SUN_CHARGED_QUARTZ_II, null,
                ItemStacks.SUN_CHARGED_QUARTZ_II, ItemStacks.SUN_CHARGED_QUARTZ_II, ItemStacks.SUN_CHARGED_QUARTZ_II
        }, 4).register(instance);

        new Tier2Altar().register(instance);

        //Tier 3
        new SpiritualAltarPiece3(ItemStacks.SUN_CHARGED_QUARTZ_III, RecipeType.ANCIENT_ALTAR, new ItemStack[] {
                ItemStacks.SUN_INFUSED_MEMBRANE, ItemStacks.SUN_ECTOPLASM, ItemStacks.SUN_CHARGED_QUARTZ_II,
                ItemStacks.SUN_ECTOPLASM, SlimefunItems.BLANK_RUNE, ItemStacks.SUN_ECTOPLASM,
                ItemStacks.SUN_CHARGED_QUARTZ_II, ItemStacks.SUN_ECTOPLASM, ItemStacks.SUN_INFUSED_MEMBRANE
        }, 8).register(instance);

        new SpiritualAltarPiece3(ItemStacks.SUN_CHARGED_PILLAR_III, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                ItemStacks.SUN_CHARGED_QUARTZ_III, null, null,
                null, null, null,
                null, null, null
        }, 1).register(instance);

        new SpiritualAltarPiece3(ItemStacks.SUN_CHARGED_STAIRS_III, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                null, null, null,
                null, ItemStacks.SUN_CHARGED_QUARTZ_III, null,
                ItemStacks.SUN_CHARGED_QUARTZ_III, ItemStacks.SUN_CHARGED_QUARTZ_III, ItemStacks.SUN_CHARGED_QUARTZ_III
        }, 4).register(instance);

        new SpiritualAltarPiece3(ItemStacks.SUN_SMOOTH_CHARGED_QUARTZ_III, RecipeType.SMELTERY, new ItemStack[] {
                ItemStacks.SUN_CHARGED_QUARTZ_III, null, null,
                null, null, null,
                null, null, null
        }, 1).register(instance);

        new SpiritualAltarPiece3(ItemStacks.SUN_SMOOTH_CHARGED_STAIRS_III, RecipeType.SMELTERY, new ItemStack[] {
                ItemStacks.SUN_CHARGED_STAIRS_III, null, null,
                null, null, null,
                null, null, null
        }, 1).register(instance);

        new Tier3Altar().register(instance);
    }
}
