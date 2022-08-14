package me.justahuman.spiritsunchained.slimefun;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;

import io.github.thebusybiscuit.slimefun4.core.attributes.MachineTier;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;
import io.github.thebusybiscuit.slimefun4.utils.itemstack.ColoredFireworkStar;

import me.justahuman.spiritsunchained.utils.LogUtils;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;

public class ItemStacks {

    // Crafting Materials

    private static ItemStack getEnchanted(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }

    private static ItemStack getPotionColor(int r, int g, int b) {
        ItemStack itemStack = new ItemStack(Material.POTION);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            PotionMeta potionMeta = (PotionMeta) itemMeta;
            potionMeta.setColor(Color.fromRGB(r, g, b));
            itemStack.setItemMeta(potionMeta);
        }
        return  itemStack;
    }

    // Crafting Materials

    public static final SlimefunItemStack SU_INFUSED_MEMBRANE = new SlimefunItemStack(
            "SU_INFUSED_MEMBRANE",
            getEnchanted(new ItemStack(Material.PHANTOM_MEMBRANE)),
            "&5Infused Membrane",
            "",
            "&7A Membrane infused with Magic"
    );

    public static final SlimefunItemStack SU_INFUSED_FEATHER = new SlimefunItemStack(
            "SU_INFUSED_FEATHER",
            getEnchanted(new ItemStack(Material.FEATHER)),
            "&5Infused Feather",
            "",
            "&7A Feather infused with Magic"
    );

    public static final SlimefunItemStack SU_SOUL_STAINED_GLASS = new SlimefunItemStack(
            "SU_SOUL_STAINED_GLASS",
            getEnchanted(new ItemStack(Material.TINTED_GLASS)),
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

    public static final SlimefunItemStack SU_UNIDENTIFIED_SPIRIT = new SlimefunItemStack(
      "SU_UNIDENTIFIED_SPIRIT",
      new ColoredFireworkStar(Color.fromRGB(100, 100, 100),
              "&fUnidentified Spirit",
              "",
              "&7A Captured Unidentified Spirit",
              "&7Useless on its Own"
      )
    );

    public static final SlimefunItemStack SU_SPIRIT_BOTTLE = new SlimefunItemStack(
            "SU_SPIRIT_BOTTLE",
            getPotionColor(150,150,150),
            "&fSpirit in a Bottle",
            "",
            "&7A Spirit in a Bottle",
            "&7Use it to get Ectoplasm"
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

    public static final SlimefunItemStack SU_SPIRIT_NET = new SlimefunItemStack(
            "SU_SPIRIT_NET",
            Material.COBWEB,
            "&aSpirit Net",
            "",
            "&7Use this to catch a Spirit!"
    );

    public static final SlimefunItemStack SU_SPIRIT_BOOK = new SlimefunItemStack(
            "SU_SPIRIT_BOOK",
            Material.WRITTEN_BOOK,
            "&aSpirit Book",
            "",
            "&7A Book only Spirits can Write in"
    );

    public static final SlimefunItemStack SU_SPIRIT_RUNE = new SlimefunItemStack(
            "SU_SPIRIT_RUNE",
            new ColoredFireworkStar(Color.fromRGB(154,96,255),
                    "&7Ancient Rune &8&l[&d&lSpirit&8&l]",
                    "",
                    "&eDrop this rune onto a dropped &dHelmet &eto",
                    "&egrant the ability to &dsee &ea different realm",
                    "&eShift Left Click on Helmet Once Added to Toggle Visibility"
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
            LoreBuilder.speed(1), LoreBuilder.powerBuffer(4000) ,LoreBuilder.powerPerSecond(250));

    public static final SlimefunItemStack SU_ELECTRIC_SPIRIT_WRITER = new SlimefunItemStack(
            "SU_ELECTRIC_SPIRIT_WRITER",
            Material.LECTERN,
            "&aElectric Spirit Writer",
            "",
            "&7Automatically let Spirits Write to A Spirit Book",
            "",
            LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE),
            LoreBuilder.speed(1), LoreBuilder.powerBuffer(2000) ,LoreBuilder.powerPerSecond(125));

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
