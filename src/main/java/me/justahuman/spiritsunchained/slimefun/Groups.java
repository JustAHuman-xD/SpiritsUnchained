package me.justahuman.spiritsunchained.slimefun;

import io.github.thebusybiscuit.slimefun4.api.items.groups.FlexItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.groups.NestedItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.groups.SubItemGroup;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideMode;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.skins.PlayerHead;
import io.github.thebusybiscuit.slimefun4.libraries.dough.skins.PlayerSkin;

import me.justahuman.spiritsunchained.SpiritsUnchained;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

public class Groups {
    public static final NestedItemGroup SU_MAIN_GROUP = new NestedItemGroup(
            new NamespacedKey(SpiritsUnchained.getInstance(), "sun_main"),
            new CustomItemStack(PlayerHead.getItemStack(PlayerSkin.fromBase64(
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGZhMTMwMzJmYTkzOWYxODRkYWE2YTRlMTFlNmYzYTkxM2U0OGYyNTA0OTgxNjVjNTY2NWNjZjQ5YzcyYTE0MCJ9fX0=")),
                    "&aSpirits Unchained"));

    public static final SubItemGroup SU_RESOURCES = new SubItemGroup(
            new NamespacedKey(SpiritsUnchained.getInstance(), "sun_resources"),
            SU_MAIN_GROUP,
            new CustomItemStack(Material.PHANTOM_MEMBRANE, "&aCrafting Materials"));

    public static final SubItemGroup SU_TOOLS = new SubItemGroup(
            new NamespacedKey(SpiritsUnchained.getInstance(), "sun_tools"),
            SU_MAIN_GROUP,
            new CustomItemStack(Material.COBWEB, "&aTools"));

    public static final SubItemGroup SU_MACHINES = new SubItemGroup(
            new NamespacedKey(SpiritsUnchained.getInstance(), "sun_machines"),
            SU_MAIN_GROUP,
            new CustomItemStack(Material.LECTERN, "&aMachines"));

    public static final SubItemGroup SU_ALTAR_1 = new SubItemGroup(
            new NamespacedKey(SpiritsUnchained.getInstance(), "sun_altar_1"),
            SU_MAIN_GROUP,
            new CustomItemStack(Material.CHISELED_QUARTZ_BLOCK, "&aTier 1 Altar"));

    public static final SubItemGroup SU_ALTAR_2 = new SubItemGroup(
            new NamespacedKey(SpiritsUnchained.getInstance(), "sun_altar_2"),
            SU_MAIN_GROUP,
            new CustomItemStack(Material.QUARTZ_PILLAR, "&aTier 2 Altar"));

    public static final SubItemGroup SU_ALTAR_3 = new SubItemGroup(
            new NamespacedKey(SpiritsUnchained.getInstance(), "sun_altar_3"),
            SU_MAIN_GROUP,
            new CustomItemStack(Material.SMOOTH_QUARTZ, "&aTier 3 Altar"));
}
