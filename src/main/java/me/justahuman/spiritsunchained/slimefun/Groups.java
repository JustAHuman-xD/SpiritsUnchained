package me.justahuman.spiritsunchained.slimefun;

import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.skins.PlayerHead;
import io.github.thebusybiscuit.slimefun4.libraries.dough.skins.PlayerSkin;
import io.github.thebusybiscuit.slimefun4.utils.itemstack.ColoredFireworkStar;

import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.justahuman.spiritsunchained.spirits.SpiritsFlexGroup;

import dev.sefiraat.sefilib.slimefun.itemgroup.DummyItemGroup;
import dev.sefiraat.sefilib.slimefun.itemgroup.SimpleFlexGroup;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

public class Groups {
    public static final SimpleFlexGroup SU_MAIN_GROUP = new SimpleFlexGroup(
            SpiritsUnchained.getInstance(),
            "&aSpirits Unchained",
            new NamespacedKey(SpiritsUnchained.getInstance(), "su_main"),
            new CustomItemStack(PlayerHead.getItemStack(PlayerSkin.fromBase64(
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGZhMTMwMzJmYTkzOWYxODRkYWE2YTRlMTFlNmYzYTkxM2U0OGYyNTA0OTgxNjVjNTY2NWNjZjQ5YzcyYTE0MCJ9fX0=")),"&aSpirits Unchained"));

    public static final DummyItemGroup SU_RESOURCES = new DummyItemGroup(
            new NamespacedKey(SpiritsUnchained.getInstance(), "su_resources"),
            new CustomItemStack(Material.PHANTOM_MEMBRANE, "&aCrafting Materials"));

    public static final DummyItemGroup SU_TOOLS = new DummyItemGroup(
            new NamespacedKey(SpiritsUnchained.getInstance(), "su_tools"),
            new CustomItemStack(Material.COBWEB, "&aTools"));

    public static final DummyItemGroup SU_MACHINES = new DummyItemGroup(
            new NamespacedKey(SpiritsUnchained.getInstance(), "su_machines"),
            new CustomItemStack(Material.LECTERN, "&aMachines"));

    public static final SpiritsFlexGroup SU_SPIRITS_GROUP = new SpiritsFlexGroup(
            new NamespacedKey(SpiritsUnchained.getInstance(), "su_spirits_group"),
            new CustomItemStack(new ColoredFireworkStar(Color.fromRGB(255, 170, 0), "&aSpirits")));

    public static final DummyItemGroup SU_ALTAR_1 = new DummyItemGroup(
            new NamespacedKey(SpiritsUnchained.getInstance(), "su_altar_1"),
            new CustomItemStack(Material.CHISELED_QUARTZ_BLOCK, "&aTier 1 Altar"));

    public static final DummyItemGroup SU_ALTAR_2 = new DummyItemGroup(
            new NamespacedKey(SpiritsUnchained.getInstance(), "su_altar_2"),
            new CustomItemStack(Material.QUARTZ_PILLAR, "&aTier 2 Altar"));

    public static final DummyItemGroup SU_ALTAR_3 = new DummyItemGroup(
            new NamespacedKey(SpiritsUnchained.getInstance(), "su_altar_3"),
            new CustomItemStack(Material.SMOOTH_QUARTZ, "&aTier 3 Altar"));
}
