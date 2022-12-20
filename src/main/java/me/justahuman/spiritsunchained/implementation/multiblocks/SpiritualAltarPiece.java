package me.justahuman.spiritsunchained.implementation.multiblocks;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import me.justahuman.spiritsunchained.utils.SpiritUtils;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class SpiritualAltarPiece extends SlimefunItem {
    public SpiritualAltarPiece(ItemGroup category, @Nonnull SlimefunItemStack item, @Nonnull RecipeType recipeType, @Nonnull ItemStack[] recipe, int amount) {
        super(category, item, recipeType, recipe, new SlimefunItemStack(item, amount));
    }

    @Override
    public void preRegister() {
        addItemHandler(onBlockBreak());
    }

    private BlockBreakHandler onBlockBreak() {
        return new BlockBreakHandler(false, false) {
            @ParametersAreNonnullByDefault
            @Override
            public void onPlayerBreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops) {
                Block b = findAltar(e.getBlock());

                if (b != null && BlockStorage.getLocationInfo(b.getLocation(), "complete") != null && BlockStorage.getLocationInfo(b.getLocation(), "complete").equals("true")) {
                    BlockStorage.addBlockInfo(b, "complete", "false");
                    e.getPlayer().sendMessage(SpiritUtils.getTranslation("messages.altar.broken"));
                }

                BlockStorage.clearBlockInfo(e.getBlock());
            }
        };
    }

    @Nullable
    private Block findAltar(@Nonnull Block b) {
        for (int x = -2; x <= 2; x++) {
            for (int y = -3; y <= 0; y++) {
                for (int z = -2; z <= 2; z++) {
                    if (x == 0 && y == 0 && z == 0) {
                        continue;
                    }

                    final Block block = b.getRelative(x, y, z);

                    if (block.getType() == Material.CHISELED_QUARTZ_BLOCK && BlockStorage.getLocationInfo(block.getLocation(), "id") != null && BlockStorage.getLocationInfo(block.getLocation(), "id").contains("SU_CHARGED_CORE")) {
                        return block;
                    }
                }
            }
        }

        return null;
    }
}