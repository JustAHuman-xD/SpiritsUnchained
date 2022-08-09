package me.justahuman.spiritsunchained.slimefun;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;

import io.github.thebusybiscuit.slimefun4.core.attributes.MachineTier;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineType;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;
import io.github.thebusybiscuit.slimefun4.utils.itemstack.ColoredFireworkStar;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
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

    public static final SlimefunItemStack SU_INFUSED_MEMBRANE = new SlimefunItemStack(
            "SU_INFUSED_MEMBRANE",
            PHANTOM_MEMBRANE,
            "&5Infused Membrane",
            "",
            "&7A Membrane infused with Magic"
    );

    public static final SlimefunItemStack SU_INFUSED_FEATHER = new SlimefunItemStack(
            "SU_INFUSED_FEATHER",
            FEATHER,
            "&5Infused Feather",
            "",
            "&7A Feather infused with Magic"
    );

    public static final SlimefunItemStack SU_SOUL_STAINED_GLASS = new SlimefunItemStack(
            "SU_SOUL_STAINED_GLASS",
            TINTED_GLASS,
            "&5Soul Stained Glass",
            "",
            "&7Peer into Another Plane"
    );

    public static final SlimefunItemStack SU_ECTOPLASM = new SlimefunItemStack(
      "SU_ECTOPLASM",
      Material.SLIME_BALL,
      "&aEctoplasm",
      "",
      "&7Attained from those Impassable"
    );

    // Tools

    public static final SlimefunItemStack SU_IDENTIFYING_GLASS = new SlimefunItemStack(
            "SU_IDENTIFYING_GLASS",
            Material.SPYGLASS,
            "&dIdentifying Glass",
            "",
            "Indentifys Un-Identified Spirits"
    );

    public static final SlimefunItemStack SU_SPIRIT_LENSES = new SlimefunItemStack(
            "SU_SPIRIT_LENSES",
            Material.LEATHER_HELMET,
            "&dSpirit Lenses",
            "",
            "&7Unlock the Ability to See &dSpirits",
            "&7While Wearing these Lenses"
    );

    public static final SlimefunItemStack SU_SPIRIT_CATCHER = new SlimefunItemStack(
            "SU_SPIRIT_CATCHER",
            Material.COBWEB,
            "&aSpirit Catcher",
            "",
            "&7Use this to catch a Spirit!"
    );

    public static final SlimefunItemStack SU_SPIRIT_BOOK = new SlimefunItemStack(
            "SU_SPIRIT_BOOK",
            Material.WRITABLE_BOOK,
            "&aSpirit Book",
            "",
            "&7A Book only Spirits can Write in"
    );

    public static final SlimefunItemStack SU_NEGATIVE_TRIDENT = new SlimefunItemStack(
            "SU_NEGATIVE_TRIDENT",
            Material.TRIDENT,
            "&dNegative Trident",
            "",
            "&7A Reversed Trident",
            "&7Use it to Harvest Ectoplasm"
    );

    public static final SlimefunItemStack SU_SPIRIT_RUNE = new SlimefunItemStack(
            "SU_SPIRIT_RUNE",
            new ColoredFireworkStar(Color.fromRGB(154,96,255),
                    "&7Ancient Rune &8&l[&d&lSpirit&8&l]",
                    "",
                    "&eDrop this rune onto a dropped item to",
                    "&egrant the ability to &dsee &ea different realm"
                    )
    );

    // Machines

    public static final SlimefunItemStack SU_ELECTRIC_SPIRIT_CATCHER = new SlimefunItemStack(
            "SU_ELECTRIC_SPIRIT_CATCHER",
            Material.SCULK_SHRIEKER,
            "&aElectric Spirit Catcher",
            "",
            "&7Automatically catch Spirits",
            "",
            LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE),
            LoreBuilder.speed(1), LoreBuilder.powerBuffer(1000) ,LoreBuilder.powerPerSecond(250));

    public static final SlimefunItemStack SU_ELECTRIC_SPIRIT_WRITER = new SlimefunItemStack(
            "SU_ELECTRIC_SPIRIT_WRITER",
            Material.LECTERN,
            "&aElectric Spirit WRITER",
            "",
            "&7Automatically let Spirits Write to A Spirit Book",
            "",
            LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE),
            LoreBuilder.speed(1), LoreBuilder.powerBuffer(1000) ,LoreBuilder.powerPerSecond(125));

    // Altar Building Blocks

    public static final SlimefunItemStack SU_CHARGED_QUARTZ_I = new SlimefunItemStack(
            "SU_CHARGED_QUARTZ_I",
            Material.QUARTZ_BLOCK,
            "&eCharged Quartz I",
            "",
            "&7Used to Build a Tier 1 Spiritual Altar"
    );

    public static final SlimefunItemStack SU_CHARGED_STAIRS_I = new SlimefunItemStack(
            "SU_CHARGED_STAIRS_I",
            Material.QUARTZ_STAIRS,
            "&eCharged Stairs I",
            "",
            "&7Used to Build a Tier 1 Spiritual Altar"
    );

    public static final SlimefunItemStack SU_CHARGED_CORE_I = new SlimefunItemStack(
            "SU_CHARGED_CORE_I",
            Material.CHISELED_QUARTZ_BLOCK,
            "&eCharged Core I",
            "",
            "&7Used to Build a Tier 1 Spiritual Altar"
    );

    public static final SlimefunItemStack SU_CHARGED_QUARTZ_II = new SlimefunItemStack(
            "SU_CHARGED_QUARTZ_II",
            Material.QUARTZ_BLOCK,
            "&eCharged Quartz II",
            "",
            "&7Used to Build a Tier 2 Spiritual Altar"
    );

    public static final SlimefunItemStack SU_CHARGED_PILLAR_II = new SlimefunItemStack(
            "SU_CHARGED_PILLAR_II",
            Material.QUARTZ_PILLAR,
            "&eCharged Pillar II",
            "",
            "&7Used to Build a Tier 2 Spiritual Altar"
    );
    public static final SlimefunItemStack SU_CHARGED_STAIRS_II = new SlimefunItemStack(
            "SU_CHARGED_STAIRS_II",
            Material.QUARTZ_STAIRS,
            "&eCharged Stairs II",
            "",
            "&7Used to Build a Tier 2 Spiritual Altar"
    );

    public static final SlimefunItemStack SU_CHARGED_CORE_II = new SlimefunItemStack(
            "SU_CHARGED_CORE_II",
            Material.CHISELED_QUARTZ_BLOCK,
            "&eCharged Core II",
            "",
            "&7Used to Build a Tier 2 Spiritual Altar"
    );

    public static final SlimefunItemStack SU_CHARGED_QUARTZ_III = new SlimefunItemStack(
            "SU_CHARGED_QUARTZ_III",
            Material.QUARTZ_BLOCK,
            "&eCharged Quartz III",
            "",
            "&7Used to Build a Tier 3 Spiritual Altar"
    );

    public static final SlimefunItemStack SU_CHARGED_PILLAR_III = new SlimefunItemStack(
            "SU_CHARGED_PILLAR_III",
            Material.QUARTZ_PILLAR,
            "&eCharged Pillar III",
            "",
            "&7Used to Build a Tier 3 Spiritual Altar"
    );

    public static final SlimefunItemStack SU_CHARGED_STAIRS_III = new SlimefunItemStack(
            "SU_CHARGED_STAIRS_III",
            Material.QUARTZ_STAIRS,
            "&eCharged Stairs III",
            "",
            "&7Used to Build a Tier 3 Spiritual Altar"
    );

    public static final SlimefunItemStack SU_CHARGED_CORE_III = new SlimefunItemStack(
            "SU_CHARGED_CORE_III",
            Material.CHISELED_QUARTZ_BLOCK,
            "&eCharged Core III",
            "",
            "&7Used to Build a Tier 3 Spiritual Altar"
    );

    public static final SlimefunItemStack SU_SMOOTH_CHARGED_QUARTZ_III = new SlimefunItemStack(
            "SU_SMOOTH_CHARGED_QUARTZ_III",
            Material.SMOOTH_QUARTZ,
            "&eSmooth Charged Quartz III",
            "",
            "&7Used to Build a Tier 3 Spiritual Altar"
    );

    public static final SlimefunItemStack SU_SMOOTH_CHARGED_STAIRS_III = new SlimefunItemStack(
            "SU_SMOOTH_CHARGED_STAIRS_III",
            Material.SMOOTH_QUARTZ_STAIRS,
            "&eSmooth Charged Stairs III",
            "",
            "&7Used to Build a Tier 3 Spiritual Altar"
    );

}
