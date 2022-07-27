package me.justahuman.spiritsunchained.slimefun;

import io.github.thebusybiscuit.slimefun4.api.items.groups.NestedItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.groups.SubItemGroup;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.skins.PlayerHead;
import io.github.thebusybiscuit.slimefun4.libraries.dough.skins.PlayerSkin;

import me.justahuman.spiritsunchained.SpiritsUnchained;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

public class Groups {
    public static final NestedItemGroup SUN_MAIN_GROUP = new NestedItemGroup(
            new NamespacedKey(SpiritsUnchained.getInstance(), "sun_main"),
            new CustomItemStack(PlayerHead.getItemStack(PlayerSkin.fromBase64(
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGZhMTMwMzJmYTkzOWYxODRkYWE2YTRlMTFlNmYzYTkxM2U0OGYyNTA0OTgxNjVjNTY2NWNjZjQ5YzcyYTE0MCJ9fX0=")),
                    "&aSlimy Spirits"));

    public static final SubItemGroup SUN_RESOURCES = new SubItemGroup(
            new NamespacedKey(SpiritsUnchained.getInstance(), "sun_resources"),
            SUN_MAIN_GROUP,
            new CustomItemStack(Material.PHANTOM_MEMBRANE, "&aCrafting Materials"));

    public static final SubItemGroup SUN_TOOLS = new SubItemGroup(
            new NamespacedKey(SpiritsUnchained.getInstance(), "sun_tools"),
            SUN_MAIN_GROUP,
            new CustomItemStack(Material.COBWEB, "&aTools"));

    public static final SubItemGroup SUN_MACHINES = new SubItemGroup(
            new NamespacedKey(SpiritsUnchained.getInstance(), "sun_machines"),
            SUN_MAIN_GROUP,
            new CustomItemStack(Material.LECTERN, "&aMachines"));
}
