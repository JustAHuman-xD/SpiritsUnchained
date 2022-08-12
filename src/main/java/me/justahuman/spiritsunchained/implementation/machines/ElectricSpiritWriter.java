package me.justahuman.spiritsunchained.implementation.machines;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class ElectricSpiritWriter extends SlimefunItem implements EnergyNetComponent {
    @ParametersAreNonnullByDefault
    public ElectricSpiritWriter(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @NotNull
    @Override
    public EnergyNetComponentType getEnergyComponentType() {

        return EnergyNetComponentType.CONSUMER;
    }

    @Override
    public int getCapacity() {

        return 2000;
    }
}
