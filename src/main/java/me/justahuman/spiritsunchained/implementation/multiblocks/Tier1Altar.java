package me.justahuman.spiritsunchained.implementation.multiblocks;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;

import io.github.thebusybiscuit.slimefun4.libraries.dough.common.ChatColors;
import me.justahuman.spiritsunchained.slimefun.Groups;
import me.justahuman.spiritsunchained.slimefun.ItemStacks;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class Tier1Altar extends SlimefunItem {

    public static final String ITEM_PREFIX = ChatColors.color("&bSPIRITUAL ALTAR 1");

    public Tier1Altar() {
        super(Groups.SUN_ALTAR_1, ItemStacks.SUN_CHARGED_CORE_I, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                null, ItemStacks.SUN_CHARGED_QUARTZ_I, null,
                ItemStacks.SUN_CHARGED_QUARTZ_I, null, ItemStacks.SUN_CHARGED_QUARTZ_I,
                null, ItemStacks.SUN_CHARGED_QUARTZ_I, null
        });

        addItemHandler(new BlockTicker() {
            @Override
            public boolean isSynchronized() {
                return true;
            }

            @Override
            public void tick(Block block, SlimefunItem slimefunItem, Config config) {
                Tier1Altar.this.tick(block);
            }
        });
    }

    @Override
    public void preRegister() {
        addItemHandler(onPlace(), onUse(), onBreak());
    }

    private void tick(@Nonnull Block b) {
        Location l = b.getLocation();
        Collection<Player> players = b.getWorld().getNearbyEntitiesByType(
                Player.class,
                l,
                2
        );
        if (!players.isEmpty() && isComplete(b)) {
            l.getWorld().spawnParticle(Particle.END_ROD, radiusLocation(l), 1);
            l.getWorld().spawnParticle(Particle.END_ROD, radiusLocation(l), 1);
            l.getWorld().spawnParticle(Particle.END_ROD, radiusLocation(l), 1);
            l.getWorld().spawnParticle(Particle.END_ROD, radiusLocation(l), 1);
        }
    }

    private Location radiusLocation(Location start) {
        Random random = new Random();
        double X = start.getX() + random.nextDouble(1 + 1) - 1;
        double Y = start.getY() + random.nextDouble(3);
        double Z = start.getZ() + random.nextDouble(1 + 1) - 1;
        return new Location(start.getWorld(),X,Y,Z);
    }

    private BlockPlaceHandler onPlace() {
        return new BlockPlaceHandler(false) {

            @Override
            public void onPlayerPlace(@Nonnull BlockPlaceEvent e) {
                Block b = e.getBlockPlaced();
                if (isComplete(b)) {
                    BlockStorage.addBlockInfo(b, "complete", "true");
                    e.getPlayer().sendMessage(ChatColor.AQUA + "The Spiritual Altar (Tier 1) has been activated!");
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
                    e.getPlayer().sendMessage(ChatColor.AQUA + "The Spiritual Altar (Tier 1) has been activated!");
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

        return true;
    }

    private boolean isAltarPiece(@Nonnull Block b) {
        if (BlockStorage.getLocationInfo(b.getLocation(), "id") == null) {
            return false;
        }

        return switch (BlockStorage.getLocationInfo(b.getLocation(), "id")) {
            case "SUN_CHARGED_QUARTZ_I", "SUN_CHARGED_STAIRS_I" -> true;
            default -> false;
        };
    }
}