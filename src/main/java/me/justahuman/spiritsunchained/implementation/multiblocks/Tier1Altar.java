package me.justahuman.spiritsunchained.implementation.multiblocks;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import lombok.Getter;
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
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Tier1Altar extends SlimefunItem {

    @Getter
    private static final Map<Vector, Material> blocks = new LinkedHashMap<>();
    private static final String complete = "complete";

    public Tier1Altar() {
        super(Groups.SU_ALTAR_1, ItemStacks.SU_CHARGED_CORE_I, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                null, ItemStacks.SU_CHARGED_QUARTZ_I, null,
                ItemStacks.SU_CHARGED_QUARTZ_I, null, ItemStacks.SU_CHARGED_QUARTZ_I,
                null, ItemStacks.SU_CHARGED_QUARTZ_I, null
        });

        blocks.put(new Vector(1,0,-1), Material.QUARTZ_BLOCK);
        blocks.put (new Vector(1,0,0), Material.QUARTZ_STAIRS);
        blocks.put(new Vector(1,0,1), Material.QUARTZ_BLOCK);
        blocks.put(new Vector(0, 0, -1), Material.QUARTZ_STAIRS);
        blocks.put(new Vector(0, 0, 0), Material.CHISELED_QUARTZ_BLOCK);
        blocks.put(new Vector(0, 0, 1), Material.QUARTZ_STAIRS);
        blocks.put(new Vector(-1,0,-1), Material.QUARTZ_BLOCK);
        blocks.put(new Vector(-1, 0, 0), Material.QUARTZ_STAIRS);
        blocks.put(new Vector(-1,0,1), Material.QUARTZ_BLOCK);

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
                final Block b = e.getBlockPlaced();
                BlockStorage.addBlockInfo(b, "particle", "2");
                BlockStorage.addBlockInfo(b, "multiplier", "1.0");
                if (isComplete(b)) {
                    BlockStorage.addBlockInfo(b, complete, "true");
                    e.getPlayer().sendMessage(ChatColor.AQUA + "The Spiritual Altar (Tier 1) has been activated!");
                } else {
                    BlockStorage.addBlockInfo(b, complete, "false");
                    e.getPlayer().sendMessage(ChatColor.AQUA + "Finish your Altar and click this block again to activate it!");
                }
            }
        };
    }

    private BlockUseHandler onUse() {
        return e -> {
            final Block b = e.getClickedBlock().get();
            if (BlockStorage.getLocationInfo(b.getLocation(), complete).equals("false")) {
                if (isComplete(b)) {
                    BlockStorage.addBlockInfo(b, complete, "true");
                    e.getPlayer().sendMessage(ChatColor.AQUA + "The Spiritual Altar (Tier 1) has been activated!");
                } else {
                    BlockStorage.addBlockInfo(b, complete, "false");
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
                BlockStorage.addBlockInfo(e.getBlock(), complete, null);
                BlockStorage.clearBlockInfo(e.getBlock());
                e.getPlayer().sendMessage(ChatColor.AQUA + "The Altar has been broken!");
            }
        };
    }

    private boolean isComplete(@Nonnull Block b) {

        for (Map.Entry<Vector, Material> entry : blocks.entrySet()) {
            final Vector relative = entry.getKey();
            final Material relativeMaterial = entry.getValue();
            final Block relativeBlock = b.getRelative(relative.getBlockX(), relative.getBlockY(), relative.getBlockZ());
            if (relativeBlock.getType() != relativeMaterial || !isAltarPiece(relativeBlock)) {
                return false;
            }
        }

        return true;
    }

    private boolean isAltarPiece(@Nonnull Block b) {
        if (BlockStorage.getLocationInfo(b.getLocation(), "id") == null) {
            return false;
        }

        return switch (BlockStorage.getLocationInfo(b.getLocation(), "id")) {
            case "SU_CHARGED_QUARTZ_I", "SU_CHARGED_STAIRS_I", "SU_CHARGED_CORE_I" -> true;
            default -> false;
        };
    }
}