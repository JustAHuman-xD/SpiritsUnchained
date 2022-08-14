package me.justahuman.spiritsunchained.spirits;

import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.utils.itemstack.ColoredFireworkStar;

import me.justahuman.spiritsunchained.utils.Keys;

import org.bukkit.Color;

public class SpiritInteract extends RecipeType {

    public SpiritInteract() {
        super(
                Keys.spiritRecipeKey,
                new ColoredFireworkStar(Color.fromRGB(100, 100, 100),
                        "&fUse On Spirit",
                        "",
                        "&7Use the Item on the Right on any Spirit"
                )
        );
    }
}