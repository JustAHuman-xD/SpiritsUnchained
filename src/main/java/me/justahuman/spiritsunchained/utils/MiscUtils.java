package me.justahuman.spiritsunchained.utils;

import me.justahuman.spiritsunchained.SpiritsUnchained;

import org.bukkit.NamespacedKey;

public class MiscUtils {

    private final static SpiritsUnchained instance = SpiritsUnchained.getInstance();
    public final static NamespacedKey spiritEntityKey = new NamespacedKey(instance, "spirit");
    public final static NamespacedKey spiritRevealedKey = new NamespacedKey(instance, "revealed");
}
