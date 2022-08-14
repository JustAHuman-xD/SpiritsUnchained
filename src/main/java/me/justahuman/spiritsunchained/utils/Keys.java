package me.justahuman.spiritsunchained.utils;

import me.justahuman.spiritsunchained.SpiritsUnchained;

import org.bukkit.NamespacedKey;

public class Keys {

    private final static SpiritsUnchained instance = SpiritsUnchained.getInstance();
    public final static NamespacedKey EntityKey = new NamespacedKey(instance, "living-entity");
    public final static NamespacedKey spiritTypeKey = new NamespacedKey(instance, "spirit-type");
    public final static NamespacedKey spiritIdentified = new NamespacedKey(instance, "identified");
    public final static NamespacedKey spiritStateKey = new NamespacedKey(instance, "state");
    public final static NamespacedKey imbuedKey = new NamespacedKey(instance, "imbued");
    public final static NamespacedKey spiritItemKey = new NamespacedKey(instance, "spirit-item");
    public final static NamespacedKey spiritProgressKey = new NamespacedKey(instance, "state-progress");
    public final static NamespacedKey spiritRecipeKey = new NamespacedKey(instance, "spirit_recipe");

}
