package me.justahuman.spiritsunchained.utils;

import me.justahuman.spiritsunchained.SpiritsUnchained;

import org.bukkit.NamespacedKey;

public class Keys {

    private final static SpiritsUnchained instance = SpiritsUnchained.getInstance();
    public final static NamespacedKey entityKey = new NamespacedKey(instance, "living_entity");
    public final static NamespacedKey spiritTypeKey = new NamespacedKey(instance, "spirit_type");
    public final static NamespacedKey spiritIdentified = new NamespacedKey(instance, "identified");
    public final static NamespacedKey spiritStateKey = new NamespacedKey(instance, "state");
    public final static NamespacedKey imbuedKey = new NamespacedKey(instance, "imbued");
    public final static NamespacedKey spiritItemKey = new NamespacedKey(instance, "spirit_item");
    public final static NamespacedKey spiritProgressKey = new NamespacedKey(instance, "state_progress");
    public final static NamespacedKey spiritRecipeKey = new NamespacedKey(instance, "spirit_recipe");
    public final static NamespacedKey ownerKey = new NamespacedKey(instance, "owner");

    public final static NamespacedKey immuneKey = new NamespacedKey(instance, "immune");
    public final static NamespacedKey heavyHitKey = new NamespacedKey(instance, "heavy_hit");
    public final static NamespacedKey speedyEscape = new NamespacedKey(instance, "speedy_escape");
    public final static NamespacedKey strongBones = new NamespacedKey(instance, "strong_bones");
    public final static NamespacedKey playDead = new NamespacedKey(instance, "play_dead");
}
