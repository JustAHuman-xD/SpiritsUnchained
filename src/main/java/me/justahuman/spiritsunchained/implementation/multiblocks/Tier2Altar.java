package me.justahuman.spiritsunchained.implementation.multiblocks;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

import me.justahuman.spiritsunchained.slimefun.Groups;
import me.justahuman.spiritsunchained.slimefun.ItemStacks;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class Tier2Altar extends SlimefunItem {
    public Tier2Altar() {
        super(Groups.SU_ALTAR_2, ItemStacks.SU_CHARGED_CORE_II, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                null, ItemStacks.SU_CHARGED_QUARTZ_II, null,
                ItemStacks.SU_CHARGED_QUARTZ_II, null, ItemStacks.SU_CHARGED_QUARTZ_II,
                null, ItemStacks.SU_CHARGED_QUARTZ_II, null
        });

        addItemHandler(new BlockTicker() {
            @Override
            public boolean isSynchronized() {
                return true;
            }

            @Override
            public void tick(Block block, SlimefunItem slimefunItem, Config config) {
                ChargedCore.tick(block);
            }
        });
    }

    @Override
    public void preRegister() {
        addItemHandler(onPlace(), onUse(), onBreak());
    }

    private BlockPlaceHandler onPlace() {
        return new BlockPlaceHandler(false) {

            @Override
            public void onPlayerPlace(@Nonnull BlockPlaceEvent e) {
                Block b = e.getBlockPlaced();
                BlockStorage.addBlockInfo(b, "particle", "4");
                BlockStorage.addBlockInfo(b, "multiplier", "2.0");
                if (isComplete(b)) {
                    BlockStorage.addBlockInfo(b, "complete", "true");
                    e.getPlayer().sendMessage(ChatColor.AQUA + "The Spiritual Altar (Tier 2) has been activated!");
                } else {
                    BlockStorage.addBlockInfo(b, "complete", "false");
                    e.getPlayer().sendMessage(ChatColor.AQUA + "Finish your Altar and click this block again to activate it!");
                }
            }
        };
    }

    private BlockUseHandler onUse() {
        return e -> {
            Block b = e.getClickedBlock().get();
            if (BlockStorage.getLocationInfo(b.getLocation(), "complete").equals("false")) {
                if (isComplete(b)) {
                    BlockStorage.addBlockInfo(b, "complete", "true");
                    e.getPlayer().sendMessage(ChatColor.AQUA + "The Spiritual Altar (Tier 2) has been activated!");
                } else {
                    BlockStorage.addBlockInfo(b, "complete", "false");
                    e.getPlayer().sendMessage(ChatColor.AQUA + "The Altar is not finished!");
                }
            }

            e.cancel();
        };
    }

    private BlockBreakHandler onBreak() {
        return new BlockBreakHandler(false, false) {
            @Override
            public void onPlayerBreak(@Nonnull BlockBreakEvent e, @Nonnull ItemStack item, @Nonnull List<ItemStack> drops) {
                BlockStorage.addBlockInfo(e.getBlock(), "complete", null);
                BlockStorage.clearBlockInfo(e.getBlock());
                e.getPlayer().sendMessage(ChatColor.AQUA + "The Altar has been broken!");
            }
        };
    }

    private boolean isComplete(@Nonnull Block b) {

        if (b.getRelative(1, 0, 1).getType() != Material.QUARTZ_BLOCK || !isAltarPiece(b.getRelative(1, 0, 1)) ||
                b.getRelative(-1, 0, 1).getType() != Material.QUARTZ_BLOCK || !isAltarPiece(b.getRelative(-1, 0, 1)) ||
                b.getRelative(1, 0, -1).getType() != Material.QUARTZ_BLOCK || !isAltarPiece(b.getRelative(1, 0, -1)) ||
                b.getRelative(-1, 0, -1).getType() != Material.QUARTZ_BLOCK || !isAltarPiece(b.getRelative(-1, 0, -1))) {
            return false;
        }

        if (b.getRelative(0, 0, 1).getType() != Material.QUARTZ_STAIRS || !isAltarPiece(b.getRelative(0, 0, 1)) ||
                b.getRelative(0, 0, -1).getType() != Material.QUARTZ_STAIRS || !isAltarPiece(b.getRelative(0, 0, -1)) ||
                b.getRelative(1, 0, 0).getType() != Material.QUARTZ_STAIRS || !isAltarPiece(b.getRelative(1, 0, 0)) ||
                b.getRelative(-1, 0, 0).getType() != Material.QUARTZ_STAIRS || !isAltarPiece(b.getRelative(-1, 0, 0))) {
            return false;
        }

        if (b.getRelative(2, 0, 2).getType() != Material.QUARTZ_PILLAR || !isAltarPiece(b.getRelative(2, 0, 2)) ||
                b.getRelative(-2, 0, 2).getType() != Material.QUARTZ_PILLAR || !isAltarPiece(b.getRelative(-2, 0, 2)) ||
                b.getRelative(2, 0, -2).getType() != Material.QUARTZ_PILLAR || !isAltarPiece(b.getRelative(2, 0, -2)) ||
                b.getRelative(-2, 0, -2).getType() != Material.QUARTZ_PILLAR || !isAltarPiece(b.getRelative(-2, 0, -2))) {
            return false;
        }

        if (b.getRelative(2, 1, 2).getType() != Material.QUARTZ_PILLAR || !isAltarPiece(b.getRelative(2, 1, 2)) ||
                b.getRelative(-2, 1, 2).getType() != Material.QUARTZ_PILLAR || !isAltarPiece(b.getRelative(-2, 1, 2)) ||
                b.getRelative(2, 1, -2).getType() != Material.QUARTZ_PILLAR || !isAltarPiece(b.getRelative(2, 1, -2)) ||
                b.getRelative(-2, 1, -2).getType() != Material.QUARTZ_PILLAR || !isAltarPiece(b.getRelative(-2, 1, -2))) {
            return false;
        }

        if (b.getRelative(2, 2, 2).getType() != Material.QUARTZ_STAIRS || !isAltarPiece(b.getRelative(2, 2, 2)) ||
                b.getRelative(-2, 2, 2).getType() != Material.QUARTZ_STAIRS || !isAltarPiece(b.getRelative(-2, 2, 2)) ||
                b.getRelative(2, 2, -2).getType() != Material.QUARTZ_STAIRS || !isAltarPiece(b.getRelative(2, 2, -2)) ||
                b.getRelative(-2, 2, -2).getType() != Material.QUARTZ_STAIRS || !isAltarPiece(b.getRelative(-2, 2, -2))) {
            return false;
        }

        return true;
    }

    private boolean isAltarPiece(@Nonnull Block b) {
        if (BlockStorage.getLocationInfo(b.getLocation(), "id") == null) {
            return false;
        }

        return switch (BlockStorage.getLocationInfo(b.getLocation(), "id")) {
            case "SU_CHARGED_QUARTZ_II", "SU_CHARGED_STAIRS_II", "SU_CHARGED_PILLAR_II" -> true;
            default -> false;
        };
    }
}
