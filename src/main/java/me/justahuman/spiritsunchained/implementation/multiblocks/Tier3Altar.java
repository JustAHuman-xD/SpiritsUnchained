package me.justahuman.spiritsunchained.implementation.multiblocks;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import lombok.Getter;
import me.justahuman.spiritsunchained.utils.SpiritUtils;
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

public class Tier3Altar extends SlimefunItem {

    @Getter
    private static final Map<Vector, Material> blocks = new LinkedHashMap<>();
    private static final String complete = "complete";

    public Tier3Altar() {
        super(Groups.SU_ALTAR_3, ItemStacks.SU_CHARGED_CORE_III, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                null, ItemStacks.SU_CHARGED_QUARTZ_III, null,
                ItemStacks.SU_CHARGED_QUARTZ_III, null, ItemStacks.SU_CHARGED_QUARTZ_III,
                null, ItemStacks.SU_CHARGED_QUARTZ_III, null
        });

        blocks.put(new Vector(2, 0, -2), Material.QUARTZ_PILLAR);
        blocks.put(new Vector(1,0,-1), Material.QUARTZ_BLOCK);
        blocks.put (new Vector(1,0,0), Material.SMOOTH_QUARTZ);
        blocks.put(new Vector(1,0,1), Material.QUARTZ_BLOCK);
        blocks.put(new Vector(2, 0, 2), Material.QUARTZ_PILLAR);
        blocks.put(new Vector(0, 0, -1), Material.SMOOTH_QUARTZ);
        blocks.put(new Vector(0, 0, 0), Material.CHISELED_QUARTZ_BLOCK);
        blocks.put(new Vector(0, 0, 1), Material.SMOOTH_QUARTZ);
        blocks.put(new Vector(-2, 0, -2), Material.QUARTZ_PILLAR);
        blocks.put(new Vector(-1,0,-1), Material.QUARTZ_BLOCK);
        blocks.put(new Vector(-1, 0, 0), Material.SMOOTH_QUARTZ);
        blocks.put(new Vector(-1,0,1), Material.QUARTZ_BLOCK);
        blocks.put(new Vector(-2, 0, 2), Material.QUARTZ_PILLAR);

        blocks.put(new Vector(2, 0, -1), Material.SMOOTH_QUARTZ_STAIRS);
        blocks.put(new Vector(2, 0, 0), Material.SMOOTH_QUARTZ_STAIRS);
        blocks.put(new Vector(2, 0, 1), Material.SMOOTH_QUARTZ_STAIRS);

        blocks.put(new Vector(-1, 0, 2), Material.SMOOTH_QUARTZ_STAIRS);
        blocks.put(new Vector(0, 0, 2), Material.SMOOTH_QUARTZ_STAIRS);
        blocks.put(new Vector(1, 0, 2), Material.SMOOTH_QUARTZ_STAIRS);

        blocks.put(new Vector(-2, 0, 1), Material.SMOOTH_QUARTZ_STAIRS);
        blocks.put(new Vector(-2, 0, 0), Material.SMOOTH_QUARTZ_STAIRS);
        blocks.put(new Vector(-2, 0, -1), Material.SMOOTH_QUARTZ_STAIRS);

        blocks.put(new Vector(1, 0, -2), Material.SMOOTH_QUARTZ_STAIRS);
        blocks.put(new Vector(0, 0, -2), Material.SMOOTH_QUARTZ_STAIRS);
        blocks.put(new Vector(-1, 0, -2), Material.SMOOTH_QUARTZ_STAIRS);

        blocks.put(new Vector(2, 1, -2), Material.QUARTZ_PILLAR);
        blocks.put(new Vector(2, 1, 2), Material.QUARTZ_PILLAR);
        blocks.put(new Vector(-2, 1, -2), Material.QUARTZ_PILLAR);
        blocks.put(new Vector(-2, 1, 2), Material.QUARTZ_PILLAR);

        blocks.put(new Vector(2, 2, -2), Material.QUARTZ_PILLAR);
        blocks.put(new Vector(2, 2, 2), Material.QUARTZ_PILLAR);
        blocks.put(new Vector(-2, 2, -2), Material.QUARTZ_PILLAR);
        blocks.put(new Vector(-2, 2, 2), Material.QUARTZ_PILLAR);

        blocks.put(new Vector(2, 3, -2), Material.SMOOTH_QUARTZ_STAIRS);
        blocks.put(new Vector(2, 3, 2), Material.SMOOTH_QUARTZ_STAIRS);
        blocks.put(new Vector(-2, 3, -2), Material.SMOOTH_QUARTZ_STAIRS);
        blocks.put(new Vector(-2, 3, 2), Material.SMOOTH_QUARTZ_STAIRS);

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
                BlockStorage.addBlockInfo(b, "particle", "8");
                BlockStorage.addBlockInfo(b, "multiplier", "4.0");
                if (isComplete(b)) {
                    BlockStorage.addBlockInfo(b, complete, "true");
                    e.getPlayer().sendMessage(SpiritUtils.getTranslation("messages.altar.completed.3"));
                } else {
                    BlockStorage.addBlockInfo(b, complete, "false");
                    e.getPlayer().sendMessage(SpiritUtils.getTranslation("messages.altar.complete_prompt"));
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
                    e.getPlayer().sendMessage(SpiritUtils.getTranslation("messages.altar.completed.3"));
                } else {
                    BlockStorage.addBlockInfo(b, complete, "false");
                    e.getPlayer().sendMessage(SpiritUtils.getTranslation("messages.altar.not_completed"));
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
                e.getPlayer().sendMessage(SpiritUtils.getTranslation("messages.altar.broken"));
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
            case "SU_CHARGED_QUARTZ_III", "SU_SMOOTH_CHARGED_QUARTZ_III", "SU_CHARGED_PILLAR_III", "SU_SMOOTH_CHARGED_STAIRS_III", "SU_CHARGED_CORE_III" -> true;
            default -> false;
        };
    }
}