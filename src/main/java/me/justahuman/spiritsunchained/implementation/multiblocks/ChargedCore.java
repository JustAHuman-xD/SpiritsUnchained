package me.justahuman.spiritsunchained.implementation.multiblocks;

import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;
import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.justahuman.spiritsunchained.spirits.SpiritDefinition;
import me.justahuman.spiritsunchained.utils.Keys;

import me.justahuman.spiritsunchained.utils.ParticleUtils;
import me.justahuman.spiritsunchained.utils.PlayerUtils;
import me.justahuman.spiritsunchained.utils.SpiritUtils;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

public class ChargedCore {
    public static void tick(@Nonnull Block b) {
        final Location l = b.getLocation();
        final Collection<Player> players = b.getWorld().getNearbyPlayers(l, 2);

        if (!players.isEmpty() && isComplete(b)) {
            final String Times = BlockStorage.getLocationInfo(b.getLocation(),"particle");
            final double Multiplier = Double.parseDouble(BlockStorage.getLocationInfo(b.getLocation(), "multiplier"));
            particle(Integer.valueOf(Times),l);
            for (Player player : players) {
                final ItemStack spiritItem = getSpiritItem(player);
                if (spiritItem != null) {
                    ItemMeta itemMeta = spiritItem.getItemMeta();

                    final  SpiritDefinition definition = SpiritsUnchained.getSpiritsManager().getSpiritMap().get(EntityType.valueOf(PersistentDataAPI.getString(itemMeta, Keys.spiritItemKey)));
                    SpiritUtils.updateSpiritItemProgress(spiritItem, Multiplier / definition.getTier());

                    Bukkit.getScheduler().runTaskLater(SpiritsUnchained.getInstance(), () -> {
                        if (PersistentDataAPI.getString(itemMeta, Keys.spiritStateKey).equals("Friendly")) {
                            PlayerUtils.learnKnowledgePiece(player, definition.getType(), 3);
                        }
                    }, 1L);
                }
            }
        }
    }

    private static void particle(Integer times, Location start) {
        ParticleUtils.spawnParticleRadius(start.clone().add(0, 1.5, 0), Particle.END_ROD, 2, times, "Freeze");
    }

    private static boolean isComplete(Block b) {
        final String storage = BlockStorage.getLocationInfo(b.getLocation(),"complete");
        return Boolean.parseBoolean(storage);
    }

    @Nullable
    private static ItemStack getSpiritItem(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() != Material.AIR && item.hasItemMeta() && SpiritUtils.isSpiritItem(item) && ! (PersistentDataAPI.getString(item.getItemMeta(), Keys.spiritStateKey).equals("Friendly") && PersistentDataAPI.getDouble(item.getItemMeta(), Keys.spiritProgressKey) == 100.0)) {
                return item;
            }
        }
        return null;
    }
}
