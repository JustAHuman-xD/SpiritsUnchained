package me.justahuman.spiritsunchained.spirits;

import lombok.Getter;

import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
public class Trait {
    private final ItemStack item;
    private final int cooldown;
    private final int tier;
    private final String id;
    private final String name;
    private final List<String> description;

    public Trait(ItemStack item, int cooldown, int tier, String id, String name, List<String> description) {
        this.item = item;
        this.cooldown = cooldown;
        this.tier = tier;
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
