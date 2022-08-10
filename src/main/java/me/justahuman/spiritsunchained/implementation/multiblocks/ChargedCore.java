package me.justahuman.spiritsunchained.implementation.multiblocks;

import me.justahuman.spiritsunchained.utils.MiscUtils;

import me.justahuman.spiritsunchained.utils.SpiritUtils;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

public class ChargedCore {
    public static void tick(@Nonnull Block b) {
        Location l = b.getLocation();
        Collection<Player> players = b.getWorld().getNearbyEntitiesByType(
                Player.class,
                l,
                2
        );
        if (!players.isEmpty() && isComplete(b)) {
            String ID = BlockStorage.checkID(b);
            String Times = BlockStorage.getLocationInfo(b.getLocation(),"particle");
            particle(Integer.valueOf(Times),l);
            for (Player player : players) {
                ItemStack spiritItem = getSpiritItem(player);
                if (spiritItem != null) {

                }
            }
        }
    }

    private static void particle(Integer times, Location start) {
        MiscUtils.spawnParticleRadius(start.clone().add(0, 1.5, 0), Particle.END_ROD, 2, times, true, false);
    }

    private static boolean isComplete(Block b) {
        String storage = BlockStorage.getLocationInfo(b.getLocation(),"complete");
        return Boolean.parseBoolean(storage);
    }

    @Nullable
    private static ItemStack getSpiritItem(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (SpiritUtils.isSpiritItem(item)) {
                return item;
            }
        }
        return null;
    }
}
