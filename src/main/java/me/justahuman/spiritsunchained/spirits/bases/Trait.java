package me.justahuman.spiritsunchained.spirits.bases;

import lombok.Getter;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
public abstract class Trait {
    private final ItemStack item;
    private final int cooldown;
    private final int tier;
    private final String name;
    private final List<String> description;

    public Trait(ItemStack item, int cooldown, int tier, String name, List<String> description) {
        this.item = item;
        this.cooldown = cooldown;
        this.tier = tier;
        this.name = name;
        this.description = description;
    }

    protected abstract void Use(Player player);
}
