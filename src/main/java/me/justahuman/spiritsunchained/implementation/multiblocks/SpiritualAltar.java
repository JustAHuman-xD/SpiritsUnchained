package me.justahuman.spiritsunchained.implementation.multiblocks;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;
import lombok.Getter;
import me.justahuman.spiritsunchained.spirits.SpiritDefinition;
import me.justahuman.spiritsunchained.utils.Keys;
import me.justahuman.spiritsunchained.utils.ParticleUtils;
import me.justahuman.spiritsunchained.utils.PlayerUtils;
import me.justahuman.spiritsunchained.utils.SpiritUtils;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class SpiritualAltar extends SlimefunItem {
    
    @Getter
    private final Map<Vector, SlimefunItemStack> blocks;
    private final int tier;
    private final int multiplier;
    private static final String COMPLETE = "complete";
    private static final String TRUE = "true";
    private static final String FALSE = "false";
    
    public SpiritualAltar(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Map<Vector, SlimefunItemStack> blocks, final int tier, final int multiplier) {
        super(itemGroup, item, recipeType, recipe);
        
        this.blocks = blocks;
        this.tier = tier;
        this.multiplier = multiplier;
    
        addItemHandler(new BlockTicker() {
            @Override
            public boolean isSynchronized() {
                return true;
            }
        
            @Override
            public void tick(Block block, SlimefunItem slimefunItem, Config config) {
                SpiritualAltar.this.tick(block);
            }
        });
    }
    
    @Override
    public void preRegister() {
        addItemHandler(onPlace(), onUse(), onBreak());
    }
    
    protected void tick(@Nonnull Block block) {
        final Location location = block.getLocation();
        final Collection<Player> players = block.getWorld().getNearbyPlayers(location, 2);
        
        if (players.isEmpty() || isNotComplete(block)) {
            return;
        }
        
        particle(this.multiplier * 2, location);
        
        for (Player player : players) {
            final ItemStack itemStack = getSpiritItem(player);
            
            if (itemStack == null) {
                continue;
            }
            
            final SpiritDefinition spiritDefinition = SpiritUtils.getSpiritDefinition(itemStack);
            SpiritUtils.updateSpiritItemProgress(itemStack, (double) this.multiplier / spiritDefinition.getTier());
            
            if (PersistentDataAPI.getString(itemStack.getItemMeta(), Keys.spiritStateKey).equals("Friendly")) {
                PlayerUtils.learnKnowledgePiece(player, spiritDefinition.getType(), 3);
            }
        }
    }
    
    private void particle(Integer times, Location start) {
        ParticleUtils.spawnParticleRadius(start.clone().add(0, 1.5, 0), Particle.END_ROD, 2, times, "Freeze");
    }
    
    @Nullable
    private static ItemStack getSpiritItem(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (SpiritUtils.isSpiritItem(item) && ! (PersistentDataAPI.getString(item.getItemMeta(), Keys.spiritStateKey).equals("Friendly") && PersistentDataAPI.getDouble(item.getItemMeta(), Keys.spiritProgressKey) == 100.0)) {
                return item;
            }
        }
        return null;
    }
    
    private BlockPlaceHandler onPlace() {
        return new BlockPlaceHandler(false) {
            
            @Override
            public void onPlayerPlace(@Nonnull BlockPlaceEvent event) {
                final Block block = event.getBlockPlaced();
                if (checkComplete(block)) {
                    BlockStorage.addBlockInfo(block, COMPLETE, TRUE);
                    event.getPlayer().sendMessage(SpiritUtils.getTranslation("messages.altar.completed." + tier));
                } else {
                    BlockStorage.addBlockInfo(block, COMPLETE, FALSE);
                    event.getPlayer().sendMessage(SpiritUtils.getTranslation("messages.altar.complete_prompt"));
                }
            }
        };
    }
    
    private BlockUseHandler onUse() {
        return event -> {
            final Block block = event.getClickedBlock().isPresent() ? event.getClickedBlock().get() : null;
            if (block != null && isNotComplete(block)) {
                if (checkComplete(block)) {
                    BlockStorage.addBlockInfo(block, COMPLETE, TRUE);
                    event.getPlayer().sendMessage(SpiritUtils.getTranslation("messages.altar.completed." + this.tier));
                } else {
                    BlockStorage.addBlockInfo(block, COMPLETE, FALSE);
                    event.getPlayer().sendMessage(SpiritUtils.getTranslation("messages.altar.not_completed"));
                }
            }
            
            event.cancel();
        };
    }
    
    private BlockBreakHandler onBreak() {
        return new BlockBreakHandler(false, false) {
            @Override
            public void onPlayerBreak(@Nonnull BlockBreakEvent event, @Nonnull ItemStack item, @Nonnull List<ItemStack> drops) {
                BlockStorage.addBlockInfo(event.getBlock(), COMPLETE, null);
                BlockStorage.clearBlockInfo(event.getBlock());
                event.getPlayer().sendMessage(SpiritUtils.getTranslation("messages.altar.broken"));
            }
        };
    }
    
    private boolean checkComplete(@Nonnull Block block) {
        
        for (Map.Entry<Vector, SlimefunItemStack> entry : this.blocks.entrySet()) {
            final Vector relative = entry.getKey();
            final SlimefunItemStack relativeItemStack = entry.getValue();
            final String relativeId = relativeItemStack.getItemId();
            final Material relativeMaterial = relativeItemStack.getType();
            final Block relativeBlock = block.getRelative(relative.getBlockX(), relative.getBlockY(), relative.getBlockZ());
            if (relativeBlock.getType() != relativeMaterial || !isAltarPiece(relativeBlock, relativeId)) {
                return false;
            }
        }
        
        return true;
    }
    
    private static boolean isNotComplete(Block block) {
        final String storage = BlockStorage.getLocationInfo(block.getLocation(),"complete");
        return ! Boolean.parseBoolean(storage);
    }
    
    private boolean isAltarPiece(@Nonnull Block block, String relativeId) {
        final String id = BlockStorage.getLocationInfo(block.getLocation(), "id");
        if (id == null) {
            return false;
        }
        
        return id.equals(relativeId);
    }
}
