package me.justahuman.spiritsunchained.slimefun;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;

import io.github.thebusybiscuit.slimefun4.core.attributes.MachineTier;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineType;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;
import io.github.thebusybiscuit.slimefun4.utils.itemstack.ColoredFireworkStar;

import me.justahuman.spiritsunchained.utils.SpiritUtils;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ItemStacks {

    // Crafting Materials

    private static ItemStack getEnchanted(ItemStack itemStack) {
        final ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }

    private static ItemStack getPotionColor() {
        final ItemStack itemStack = new ItemStack(Material.SPLASH_POTION);
        final ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            PotionMeta potionMeta = (PotionMeta) itemMeta;
            potionMeta.setColor(Color.fromRGB(150, 150, 150));
            itemStack.setItemMeta(potionMeta);
        }
        return  itemStack;
    }

    private static String translate(String path) {
        return SpiritUtils.getTranslation("names.items." + path);
    }

    private static String name(String path) {
        return translate(path + ".name");
    }

    private static String[] lore(String path, String... addOn) {
        final String[] originalLore = SpiritUtils.getTranslationList("names.items." + path + ".lore").toArray(String[]::new);
        final String[] lore = new String[originalLore.length + addOn.length];
        int i = 0;
        for (String line : originalLore) {
            lore[i] = line;
            i++;
        }
        for (String line : addOn) {
            lore[i] = line;
            i++;
        }
        return lore;
    }

    private static ItemStack addPotionEffect(ItemStack itemStack) {
        final ItemMeta itemMeta = itemStack.getItemMeta();
        final PotionMeta potionMeta = (PotionMeta) itemMeta;
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.LEVITATION, 5 * 20, 1), true);
        itemStack.setItemMeta(potionMeta);
        return itemStack;
    }

    // Crafting Materials

    public static final SlimefunItemStack SU_INFUSED_MEMBRANE = new SlimefunItemStack(
            "SU_INFUSED_MEMBRANE",
            getEnchanted(new ItemStack(Material.PHANTOM_MEMBRANE)),
            name("infused_membrane"),
            lore("infused_membrane")
    );

    public static final SlimefunItemStack SU_INFUSED_FEATHER = new SlimefunItemStack(
            "SU_INFUSED_FEATHER",
            getEnchanted(new ItemStack(Material.FEATHER)),
            name("infused_feather"),
            lore("infused_feather")
    );

    public static final SlimefunItemStack SU_SOUL_STAINED_GLASS = new SlimefunItemStack(
            "SU_SOUL_STAINED_GLASS",
            getEnchanted(new ItemStack(Material.TINTED_GLASS)),
            name("soul_stained_glass"),
            lore("soul_stained_glass")
    );

    public static final SlimefunItemStack SU_ECTOPLASM = new SlimefunItemStack(
            "SU_ECTOPLASM",
            Material.SLIME_BALL,
            name("ectoplasm"),
            lore("ectoplasm")
    );

    public static final SlimefunItemStack SU_UNIDENTIFIED_SPIRIT = new SlimefunItemStack(
      "SU_UNIDENTIFIED_SPIRIT",
      new ColoredFireworkStar(Color.fromRGB(100, 100, 100),
              name("unidentified_spirit"),
              lore("unidentified_spirit")
      )
    );

    public static final SlimefunItemStack SU_SPIRIT_BOTTLE = new SlimefunItemStack(
            "SU_SPIRIT_BOTTLE",
            addPotionEffect(getPotionColor()),
            name("spirit_bottle"),
            lore("spirit_bottle")
    );

    // Spirit Guide

    public static final SlimefunItemStack SU_SPIRIT_GUIDEBOOK = new SlimefunItemStack(
            "SU_SPIRIT_GUIDEBOOK",
            Material.WRITTEN_BOOK,
            name("spirit_guide_book"),
            lore("spirit_guide_book")
    );

    // Tools

    public static final SlimefunItemStack SU_IDENTIFYING_GLASS = new SlimefunItemStack(
            "SU_IDENTIFYING_GLASS",
            Material.SPYGLASS,
            name("identifying_glass"),
            lore("identifying_glass")
    );

    public static final SlimefunItemStack SU_SPIRIT_LENSES = new SlimefunItemStack(
            "SU_SPIRIT_LENSES",
            Material.LEATHER_HELMET,
            name("spirit_lenses"),
            lore("spirit_lenses")
    );

    public static final SlimefunItemStack SU_SPIRIT_NET = new SlimefunItemStack(
            "SU_SPIRIT_NET",
            Material.COBWEB,
            name("spirit_net"),
            lore("spirit_net")
    );

    public static final SlimefunItemStack SU_SPIRIT_BOOK = new SlimefunItemStack(
            "SU_SPIRIT_BOOK",
            Material.BOOK,
            name("spirit_book"),
            lore("spirit_book")
    );

    public static final SlimefunItemStack SU_SPIRIT_RUNE = new SlimefunItemStack(
            "SU_SPIRIT_RUNE",
            new ColoredFireworkStar(Color.fromRGB(154,96,255),
                    name("spirit_rune"),
                    lore("spirit_rune")
                    )
    );

    // Machines

    public static final SlimefunItemStack SU_ELECTRIC_SPIRIT_BOTTLER = new SlimefunItemStack(
            "SU_ELECTRIC_SPIRIT_BOTTLER",
            Material.BREWING_STAND,
            name("electric_spirit_bottler"),
            lore("electric_spirit_bottler",
                    LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE),
                    LoreBuilder.speed(1),
                    LoreBuilder.powerBuffer(2000),
                    LoreBuilder.powerPerSecond(250))
    );

    public static final SlimefunItemStack SU_ELECTRIC_SPIRIT_CATCHER = new SlimefunItemStack(
            "SU_ELECTRIC_SPIRIT_CATCHER",
            Material.SCULK_SHRIEKER,
            name("electric_spirit_catcher"),
            lore("electric_spirit_catcher",
                    LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE),
                    LoreBuilder.speed(1),
                    LoreBuilder.powerBuffer(4000),
                    LoreBuilder.powerPerSecond(250))
    );

    public static final SlimefunItemStack SU_ELECTRIC_SPIRIT_WRITER = new SlimefunItemStack(
            "SU_ELECTRIC_SPIRIT_WRITER",
            Material.LECTERN,
            name("electric_spirit_writer"),
            lore("electric_spirit_writer",
                    LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE),
                    LoreBuilder.speed(1),
                    LoreBuilder.powerBuffer(2000),
                    LoreBuilder.powerPerSecond(125))
    );

    // Altar Building Blocks

    private static final String[] altarLore = lore("altars");

    public static final SlimefunItemStack SU_CHARGED_QUARTZ_I = new SlimefunItemStack(
            "SU_CHARGED_QUARTZ_I",
            Material.QUARTZ_BLOCK,
            name("charged_quartz_1"),
            altarLore
    );

    public static final SlimefunItemStack SU_CHARGED_STAIRS_I = new SlimefunItemStack(
            "SU_CHARGED_STAIRS_I",
            Material.QUARTZ_STAIRS,
            name("charged_stairs_1"),
            altarLore
    );

    public static final SlimefunItemStack SU_CHARGED_CORE_I = new SlimefunItemStack(
            "SU_CHARGED_CORE_I",
            Material.CHISELED_QUARTZ_BLOCK,
            name("charged_core_1"),
            altarLore
    );

    public static final SlimefunItemStack SU_CHARGED_QUARTZ_II = new SlimefunItemStack(
            "SU_CHARGED_QUARTZ_II",
            Material.QUARTZ_BLOCK,
            name("charged_quartz_2"),
            altarLore
    );

    public static final SlimefunItemStack SU_CHARGED_PILLAR_II = new SlimefunItemStack(
            "SU_CHARGED_PILLAR_II",
            Material.QUARTZ_PILLAR,
            name("charged_pillar_2"),
            altarLore
    );
    public static final SlimefunItemStack SU_CHARGED_STAIRS_II = new SlimefunItemStack(
            "SU_CHARGED_STAIRS_II",
            Material.QUARTZ_STAIRS,
            name("charged_stairs_2"),
            altarLore
    );

    public static final SlimefunItemStack SU_CHARGED_CORE_II = new SlimefunItemStack(
            "SU_CHARGED_CORE_II",
            Material.CHISELED_QUARTZ_BLOCK,
            name("charged_core_2"),
            altarLore
    );

    public static final SlimefunItemStack SU_CHARGED_QUARTZ_III = new SlimefunItemStack(
            "SU_CHARGED_QUARTZ_III",
            Material.QUARTZ_BLOCK,
            name("charged_quartz_3"),
            altarLore
    );

    public static final SlimefunItemStack SU_CHARGED_PILLAR_III = new SlimefunItemStack(
            "SU_CHARGED_PILLAR_III",
            Material.QUARTZ_PILLAR,
            name("charged_pillar_3"),
            altarLore
    );

    public static final SlimefunItemStack SU_CHARGED_STAIRS_III = new SlimefunItemStack(
            "SU_CHARGED_STAIRS_III",
            Material.QUARTZ_STAIRS,
            name("charged_stairs_3"),
            altarLore
    );

    public static final SlimefunItemStack SU_CHARGED_CORE_III = new SlimefunItemStack(
            "SU_CHARGED_CORE_III",
            Material.CHISELED_QUARTZ_BLOCK,
            name("charged_core_3"),
            altarLore
    );

    public static final SlimefunItemStack SU_SMOOTH_CHARGED_QUARTZ_III = new SlimefunItemStack(
            "SU_SMOOTH_CHARGED_QUARTZ_III",
            Material.SMOOTH_QUARTZ,
            name("smooth_charged_quartz_3"),
            altarLore
    );

    public static final SlimefunItemStack SU_SMOOTH_CHARGED_STAIRS_III = new SlimefunItemStack(
            "SU_SMOOTH_CHARGED_STAIRS_III",
            Material.SMOOTH_QUARTZ_STAIRS,
            name("smooth_charged_stairs_3"),
            altarLore
    );

}
