package me.justahuman.spiritsunchained.slimefun;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.inventory.ItemStack;

public class SpiritItems extends SlimefunItem {
    public SpiritItems(ItemGroup group, SlimefunItemStack item, RecipeType type, ItemStack[] recipe) {
        super(group,item,type,recipe);
    }
}
