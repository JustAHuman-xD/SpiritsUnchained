package me.justahuman.spiritsunchained.slimefun;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;

import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Phantom;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemStacks {

    // Crafting Materials
    private static final ItemStack PHANTOM_MEMBRANE = new ItemStack(Material.PHANTOM_MEMBRANE);
    private static final ItemStack FEATHER = new ItemStack(Material.FEATHER);
    private static final ItemStack TINTED_GLASS = new ItemStack(Material.TINTED_GLASS);

    static{
        ItemMeta meta1 = PHANTOM_MEMBRANE.getItemMeta();
        if(meta1 != null) {
            meta1.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
            meta1.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            PHANTOM_MEMBRANE.setItemMeta(meta1);
        }

        ItemMeta meta2 = FEATHER.getItemMeta();
        if(meta2 != null) {
            meta2.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
            meta2.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            FEATHER.setItemMeta(meta2);
        }

        ItemMeta meta3 = TINTED_GLASS.getItemMeta();
        if(meta3 != null) {
            meta3.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
            meta3.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            TINTED_GLASS.setItemMeta(meta3);
        }

    }

    public static final SlimefunItemStack SUN_INFUSED_MEMBRANE = new SlimefunItemStack(
            "SUN_INFUSED_MEMBRANE",
            PHANTOM_MEMBRANE,
            "&5Infused Membrane",
            "",
            "A Membrane infused with Magic"
    );

    public static final SlimefunItemStack SUN_INFUSED_FEATHER = new SlimefunItemStack(
            "SUN_INFUSED_FEATHER",
            FEATHER,
            "&5Infused Feather",
            "",
            "A Feather infused with Magic"
    );

    public static final SlimefunItemStack SUN_SOUL_STAINED_GLASS = new SlimefunItemStack(
            "SUN_SOUL_STAINED_GLASS",
            TINTED_GLASS,
            "&bSoul Stained Glass",
            "",
            "Peer into Another Plane"
    );

    public static final SlimefunItemStack SUN_ECTOPLASM = new SlimefunItemStack(
      "SUN_ECTOPLASM",
      Material.SLIME_BALL,
      "&aEctoplasm",
      "",
      "Attained from those Impassable"
    );

    // Altar Building Blocks

    public static final SlimefunItemStack SUN_CHARGED_QUARTZ_I = new SlimefunItemStack(
            "SUN_CHARGED_QUARTZ_I",
            Material.QUARTZ_BLOCK,
            "&eCharged Quartz I",
            "",
            "Used to Build a Tier 1 Spiritual Altar"
    );

    public static final SlimefunItemStack SUN_CHARGED_STAIRS_I = new SlimefunItemStack(
            "SUN_CHARGED_STAIRS_I",
            Material.QUARTZ_STAIRS,
            "&eCharged Stairs I",
            "",
            "Used to Build a Tier 1 Spiritual Altar"
    );

    public static final SlimefunItemStack SUN_CHARGED_CORE_I = new SlimefunItemStack(
            "SUN_CHARGED_CORE_I",
            Material.CHISELED_QUARTZ_BLOCK,
            "&eCharged Core I",
            "",
            "Used to Build a Tier 1 Spiritual Altar"
    );

    public static final SlimefunItemStack SUN_CHARGED_QUARTZ_II = new SlimefunItemStack(
            "SUN_CHARGED_QUARTZ_II",
            Material.QUARTZ_BLOCK,
            "&eCharged Quartz II",
            "",
            "Used to Build a Tier 2 Spiritual Altar"
    );

    public static final SlimefunItemStack SUN_CHARGED_PILLAR_II = new SlimefunItemStack(
            "SUN_CHARGED_PILLAR_II",
            Material.QUARTZ_PILLAR,
            "&eCharged Pillar II",
            "",
            "Used to Build a Tier 2 Spiritual Altar"
    );
    public static final SlimefunItemStack SUN_CHARGED_STAIRS_II = new SlimefunItemStack(
            "SUN_CHARGED_STAIRS_II",
            Material.QUARTZ_STAIRS,
            "&eCharged Stairs II",
            "",
            "Used to Build a Tier 2 Spiritual Altar"
    );

    public static final SlimefunItemStack SUN_CHARGED_CORE_II = new SlimefunItemStack(
            "SUN_CHARGED_CORE_II",
            Material.CHISELED_QUARTZ_BLOCK,
            "&eCharged Core II",
            "",
            "Used to Build a Tier 2 Spiritual Altar"
    );

    public static final SlimefunItemStack SUN_CHARGED_QUARTZ_III = new SlimefunItemStack(
            "SUN_CHARGED_QUARTZ_III",
            Material.QUARTZ_BLOCK,
            "&eCharged Quartz III",
            "",
            "Used to Build a Tier 3 Spiritual Altar"
    );

    public static final SlimefunItemStack SUN_CHARGED_PILLAR_III = new SlimefunItemStack(
            "SUN_CHARGED_PILLAR_III",
            Material.QUARTZ_PILLAR,
            "&eCharged Pillar III",
            "",
            "Used to Build a Tier 3 Spiritual Altar"
    );

    public static final SlimefunItemStack SUN_CHARGED_STAIRS_III = new SlimefunItemStack(
            "SUN_CHARGED_STAIRS_III",
            Material.QUARTZ_STAIRS,
            "&eCharged Stairs III",
            "",
            "Used to Build a Tier 3 Spiritual Altar"
    );

    public static final SlimefunItemStack SUN_CHARGED_CORE_III = new SlimefunItemStack(
            "SUN_CHARGED_CORE_III",
            Material.CHISELED_QUARTZ_BLOCK,
            "&eCharged Core III",
            "",
            "Used to Build a Tier 3 Spiritual Altar"
    );

    public static final SlimefunItemStack SUN_SMOOTH_CHARGED_QUARTZ_III = new SlimefunItemStack(
            "SUN_SMOOTH_CHARGED_QUARTZ_III",
            Material.SMOOTH_QUARTZ,
            "&eSmooth Charged Quartz III",
            "",
            "Used to Build a Tier 3 Spiritual Altar"
    );

    public static final SlimefunItemStack SUN_SMOOTH_CHARGED_STAIRS_III = new SlimefunItemStack(
            "SUN_SMOOTH_CHARGED_STAIRS_III",
            Material.SMOOTH_QUARTZ_STAIRS,
            "&eSmooth Charged Stairs III",
            "",
            "Used to Build a Tier 3 Spiritual Altar"
    );

}
