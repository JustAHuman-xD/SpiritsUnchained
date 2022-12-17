package me.justahuman.spiritsunchained.utils;

import me.justahuman.spiritsunchained.SpiritsUnchained;

import org.bukkit.NamespacedKey;

public class Keys {

    private static final SpiritsUnchained instance = SpiritsUnchained.getInstance();

    public static final NamespacedKey despawnKey = new NamespacedKey(instance, "despawn");
    public static final NamespacedKey immuneKey = new NamespacedKey(instance, "immune");
    public static final NamespacedKey imbuedKey = new NamespacedKey(instance, "imbued");
    public static final NamespacedKey spiritIdentified = new NamespacedKey(instance, "identified");
    public static final NamespacedKey ownerKey = new NamespacedKey(instance, "owner");
    public static final NamespacedKey spiritPassOnKey = new NamespacedKey(instance, "pass_on");
    public static final NamespacedKey spiritStateKey = new NamespacedKey(instance, "state");
    public static final NamespacedKey spiritProgressKey = new NamespacedKey(instance, "state_progress");
    public static final NamespacedKey spiritInteractKey = new NamespacedKey(instance, "spirit_interact");
    public static final NamespacedKey spiritItemKey = new NamespacedKey(instance, "spirit_item");
    public static final NamespacedKey spiritLocked = new NamespacedKey(instance, "spirit_locked");
    public static final NamespacedKey spiritTypeKey = new NamespacedKey(instance, "spirit_type");
    public static final NamespacedKey spiritUniqueKey = new NamespacedKey(instance, "unique_item");
    public static final NamespacedKey visualizing = new NamespacedKey(instance, "visualizing");
    public static final NamespacedKey entityKey = new NamespacedKey(instance, "living_entity");

    public static final NamespacedKey heavyHitKey = new NamespacedKey(instance, "heavy_hit");
    public static final NamespacedKey speedyEscape = new NamespacedKey(instance, "speedy_escape");
    public static final NamespacedKey strongBones = new NamespacedKey(instance, "strong_bones");
    public static final NamespacedKey playDead = new NamespacedKey(instance, "play_dead");
    public static final NamespacedKey morningGift = new NamespacedKey(instance, "morning_gift");

    public static NamespacedKey newKey(String key) {
        return new NamespacedKey(instance, key);
    }
}
