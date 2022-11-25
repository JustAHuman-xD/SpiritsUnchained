package me.justahuman.spiritsunchained.slimefun;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.UnplaceableBlock;

import io.github.thebusybiscuit.slimefun4.utils.itemstack.ColoredFireworkStar;
import me.justahuman.spiritsunchained.implementation.machines.ElectricSpiritBottler;
import me.justahuman.spiritsunchained.implementation.machines.ElectricSpiritCatcher;
import me.justahuman.spiritsunchained.implementation.machines.ElectricSpiritWriter;
import me.justahuman.spiritsunchained.implementation.multiblocks.*;
import me.justahuman.spiritsunchained.implementation.tools.*;

import me.justahuman.spiritsunchained.utils.Keys;
import me.justahuman.spiritsunchained.utils.SpiritUtils;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Items {
    public static void setup(SlimefunAddon instance) {

        final RecipeType spiritInteract = new RecipeType(Keys.spiritInteractKey,
                new ColoredFireworkStar(Color.fromRGB(100, 100, 100),
                        name("spirit_interact"),
                        lore("spirit_interact")
                ));

        // Crafting Materials
        new UnplaceableBlock(Groups.SU_RESOURCES, ItemStacks.SU_INFUSED_MEMBRANE, RecipeType.MAGIC_WORKBENCH, new ItemStack[]{
                SlimefunItems.ENDER_LUMP_1, null, SlimefunItems.ENDER_LUMP_1,
                null, new ItemStack(Material.PHANTOM_MEMBRANE), null,
                SlimefunItems.ENDER_LUMP_1, null, SlimefunItems.ENDER_LUMP_1})
                .register(instance);

        new UnplaceableBlock(Groups.SU_RESOURCES, ItemStacks.SU_INFUSED_FEATHER, RecipeType.MAGIC_WORKBENCH, new ItemStack[]{
                SlimefunItems.ENDER_LUMP_1, null, SlimefunItems.ENDER_LUMP_1,
                null, new ItemStack(Material.FEATHER), null,
                SlimefunItems.ENDER_LUMP_1, null, SlimefunItems.ENDER_LUMP_1})
                .register(instance);

        new UnplaceableBlock(Groups.SU_RESOURCES, ItemStacks.SU_SOUL_STAINED_GLASS, RecipeType.ANCIENT_ALTAR, new ItemStack[]{
                SlimefunItems.ENDER_LUMP_1, SlimefunItems.ESSENCE_OF_AFTERLIFE, SlimefunItems.ENDER_LUMP_1,
                SlimefunItems.ENDER_RUNE, new ItemStack(Material.TINTED_GLASS), SlimefunItems.ENDER_RUNE,
                SlimefunItems.ENDER_LUMP_1, SlimefunItems.ESSENCE_OF_AFTERLIFE, SlimefunItems.ENDER_LUMP_1})
                .register(instance);

        new SlimefunItem(Groups.SU_RESOURCES, ItemStacks.SU_UNIDENTIFIED_SPIRIT, spiritInteract, new ItemStack[]{
                null, null, null,
                null, ItemStacks.SU_SPIRIT_NET, null,
                null, null, null
        }).register(instance);

        new SlimefunItem(Groups.SU_RESOURCES, ItemStacks.SU_SPIRIT_BOTTLE, spiritInteract, new ItemStack[]{
                null, null, null,
                null, new ItemStack(Material.GLASS_BOTTLE), null,
                null, null, null
        }).register(instance);

        ItemStack outputEctoplasm = ItemStacks.SU_ECTOPLASM.clone();
        outputEctoplasm.setAmount(2);
        new SlimefunItem(Groups.SU_RESOURCES, ItemStacks.SU_ECTOPLASM, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                null, null, null,
                null, ItemStacks.SU_SPIRIT_BOTTLE, null,
                null, null, null
        }, outputEctoplasm).register(instance);

        //Tools
        new SlimefunItem(Groups.SU_TOOLS, ItemStacks.SU_SPIRIT_LENSES, RecipeType.MAGIC_WORKBENCH, new ItemStack[]{
                SlimefunItems.CLOTH, SlimefunItems.CLOTH, SlimefunItems.CLOTH,
                ItemStacks.SU_INFUSED_MEMBRANE, ItemStacks.SU_SOUL_STAINED_GLASS, ItemStacks.SU_INFUSED_MEMBRANE,
                null, null, null
        }).register(instance);

        new SlimefunItem(Groups.SU_TOOLS,ItemStacks.SU_IDENTIFYING_GLASS,RecipeType.MAGIC_WORKBENCH,new ItemStack[]{
                null, null, ItemStacks.SU_SOUL_STAINED_GLASS,
                null, SlimefunItems.MAGNESIUM_INGOT, null,
                SlimefunItems.MAGNESIUM_INGOT, null, null
        }).register(instance);

        new SlimefunItem(Groups.SU_TOOLS,ItemStacks.SU_SPIRIT_BOOK,RecipeType.MAGIC_WORKBENCH,new ItemStack[]{
                new ItemStack(Material.GLOW_INK_SAC), ItemStacks.SU_ECTOPLASM, ItemStacks.SU_INFUSED_FEATHER,
                null, new ItemStack(Material.BOOK), null,
                ItemStacks.SU_INFUSED_FEATHER, ItemStacks.SU_ECTOPLASM, new ItemStack(Material.GLOW_INK_SAC)
        }).register(instance);

        new UnplaceableBlock(Groups.SU_TOOLS,ItemStacks.SU_SPIRIT_NET,RecipeType.MAGIC_WORKBENCH,new ItemStack[]{
                new ItemStack(Material.STRING), ItemStacks.SU_ECTOPLASM, new ItemStack(Material.STRING),
                ItemStacks.SU_ECTOPLASM, ItemStacks.SU_INFUSED_MEMBRANE, ItemStacks.SU_ECTOPLASM,
                new ItemStack(Material.STRING), ItemStacks.SU_ECTOPLASM, new ItemStack(Material.STRING)
        }).register(instance);

        new SpiritRune(Groups.SU_TOOLS,ItemStacks.SU_SPIRIT_RUNE,RecipeType.ANCIENT_ALTAR,new ItemStack[]{
                ItemStacks.SU_ECTOPLASM, SlimefunItems.ESSENCE_OF_AFTERLIFE, ItemStacks.SU_INFUSED_MEMBRANE,
                ItemStacks.SU_SOUL_STAINED_GLASS, SlimefunItems.ENCHANTMENT_RUNE,ItemStacks.SU_SOUL_STAINED_GLASS,
                ItemStacks.SU_INFUSED_MEMBRANE,SlimefunItems.ESSENCE_OF_AFTERLIFE,ItemStacks.SU_ECTOPLASM
        }).register(instance);

        //Machines

        new ElectricSpiritCatcher(Groups.SU_MACHINES, ItemStacks.SU_ELECTRIC_SPIRIT_CATCHER, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                ItemStacks.SU_ECTOPLASM, SlimefunItems.INFUSED_HOPPER, ItemStacks.SU_ECTOPLASM,
                SlimefunItems.REINFORCED_PLATE, new ItemStack(Material.SCULK_SHRIEKER), SlimefunItems.REINFORCED_PLATE,
                ItemStacks.SU_ECTOPLASM, SlimefunItems.COOLING_UNIT, ItemStacks.SU_ECTOPLASM,
        }).register(instance);

        new ElectricSpiritBottler(Groups.SU_MACHINES, ItemStacks.SU_ELECTRIC_SPIRIT_BOTTLER, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                ItemStacks.SU_ECTOPLASM, SlimefunItems.INFUSED_HOPPER, ItemStacks.SU_ECTOPLASM,
                SlimefunItems.REINFORCED_PLATE, new ItemStack(Material.BREWING_STAND), SlimefunItems.REINFORCED_PLATE,
                ItemStacks.SU_ECTOPLASM, new ItemStack(Material.PISTON), ItemStacks.SU_ECTOPLASM,
        }).register(instance);

        new ElectricSpiritWriter(Groups.SU_MACHINES, ItemStacks.SU_ELECTRIC_SPIRIT_WRITER, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                ItemStacks.SU_INFUSED_FEATHER, SlimefunItems.ANDROID_MEMORY_CORE, ItemStacks.SU_ECTOPLASM,
                SlimefunItems.COOLING_UNIT, new ItemStack(Material.LECTERN), SlimefunItems.COOLING_UNIT,
                ItemStacks.SU_ECTOPLASM, SlimefunItems.POWER_CRYSTAL, ItemStacks.SU_INFUSED_FEATHER,
        }).register(instance);

        //Altar Blocks
        //Tier 1
        new SpiritualAltarPiece(Groups.SU_ALTAR_1,ItemStacks.SU_CHARGED_QUARTZ_I, RecipeType.ANCIENT_ALTAR, new ItemStack[] {
                ItemStacks.SU_INFUSED_MEMBRANE, ItemStacks.SU_ECTOPLASM, new ItemStack(Material.QUARTZ_BLOCK),
                ItemStacks.SU_ECTOPLASM, ItemStacks.SU_UNIDENTIFIED_SPIRIT, ItemStacks.SU_ECTOPLASM,
                new ItemStack(Material.QUARTZ_BLOCK), ItemStacks.SU_ECTOPLASM, ItemStacks.SU_INFUSED_MEMBRANE
        }, 8).register(instance);

        new SpiritualAltarPiece(Groups.SU_ALTAR_1,ItemStacks.SU_CHARGED_STAIRS_I, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                null, null, null,
                null, ItemStacks.SU_CHARGED_QUARTZ_I, null,
                ItemStacks.SU_CHARGED_QUARTZ_I, ItemStacks.SU_CHARGED_QUARTZ_I, ItemStacks.SU_CHARGED_QUARTZ_I
        }, 4).register(instance);

        new Tier1Altar().register(instance);

        //Tier 2
        new SpiritualAltarPiece(Groups.SU_ALTAR_2,ItemStacks.SU_CHARGED_QUARTZ_II, RecipeType.ANCIENT_ALTAR, new ItemStack[] {
                ItemStacks.SU_INFUSED_MEMBRANE, ItemStacks.SU_ECTOPLASM, ItemStacks.SU_CHARGED_QUARTZ_I,
                ItemStacks.SU_ECTOPLASM, ItemStacks.SU_UNIDENTIFIED_SPIRIT, ItemStacks.SU_ECTOPLASM,
                ItemStacks.SU_CHARGED_QUARTZ_I, ItemStacks.SU_ECTOPLASM, ItemStacks.SU_INFUSED_MEMBRANE
        }, 8).register(instance);

        new SpiritualAltarPiece(Groups.SU_ALTAR_2,ItemStacks.SU_CHARGED_PILLAR_II, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                ItemStacks.SU_CHARGED_QUARTZ_II, null, null,
                null, null, null,
                null, null, null
        }, 1).register(instance);

        new SpiritualAltarPiece(Groups.SU_ALTAR_2,ItemStacks.SU_CHARGED_STAIRS_II, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                null, null, null,
                null, ItemStacks.SU_CHARGED_QUARTZ_II, null,
                ItemStacks.SU_CHARGED_QUARTZ_II, ItemStacks.SU_CHARGED_QUARTZ_II, ItemStacks.SU_CHARGED_QUARTZ_II
        }, 4).register(instance);

        new Tier2Altar().register(instance);

        //Tier 3
        new SpiritualAltarPiece(Groups.SU_ALTAR_3,ItemStacks.SU_CHARGED_QUARTZ_III, RecipeType.ANCIENT_ALTAR, new ItemStack[] {
                ItemStacks.SU_INFUSED_MEMBRANE, ItemStacks.SU_ECTOPLASM, ItemStacks.SU_CHARGED_QUARTZ_II,
                ItemStacks.SU_ECTOPLASM, ItemStacks.SU_UNIDENTIFIED_SPIRIT, ItemStacks.SU_ECTOPLASM,
                ItemStacks.SU_CHARGED_QUARTZ_II, ItemStacks.SU_ECTOPLASM, ItemStacks.SU_INFUSED_MEMBRANE
        }, 8).register(instance);

        new SpiritualAltarPiece(Groups.SU_ALTAR_3,ItemStacks.SU_CHARGED_PILLAR_III, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                ItemStacks.SU_CHARGED_QUARTZ_III, null, null,
                null, null, null,
                null, null, null
        }, 1).register(instance);

        new SpiritualAltarPiece(Groups.SU_ALTAR_3,ItemStacks.SU_CHARGED_STAIRS_III, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                null, null, null,
                null, ItemStacks.SU_CHARGED_QUARTZ_III, null,
                ItemStacks.SU_CHARGED_QUARTZ_III, ItemStacks.SU_CHARGED_QUARTZ_III, ItemStacks.SU_CHARGED_QUARTZ_III
        }, 4).register(instance);

        new SpiritualAltarPiece(Groups.SU_ALTAR_3,ItemStacks.SU_SMOOTH_CHARGED_QUARTZ_III, RecipeType.SMELTERY, new ItemStack[] {
                ItemStacks.SU_CHARGED_QUARTZ_III, null, null,
                null, null, null,
                null, null, null
        }, 1).register(instance);

        new SpiritualAltarPiece(Groups.SU_ALTAR_3,ItemStacks.SU_SMOOTH_CHARGED_STAIRS_III, RecipeType.SMELTERY, new ItemStack[] {
                ItemStacks.SU_CHARGED_STAIRS_III, null, null,
                null, null, null,
                null, null, null
        }, 1).register(instance);

        new Tier3Altar().register(instance);
    }

    private static String translate(String path) {
        return SpiritUtils.getTranslation("names.recipe_type." + path);
    }

    private static String name(String path) {
        return translate(path + ".name");
    }

    private static String[] lore(String path) {
        return SpiritUtils.getTranslationList("names.recipe_type." + path + ".lore").toArray(String[]::new);
    }
}
