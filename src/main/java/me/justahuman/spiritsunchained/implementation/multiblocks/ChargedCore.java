package me.justahuman.spiritsunchained.implementation.multiblocks;

import dev.sefiraat.sefilib.misc.ParticleUtils;

import me.mrCookieSlime.Slimefun.api.BlockStorage;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
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
        }
    }

    private static void particle(Integer times, Location start) {
        ParticleUtils.displayParticleRandomly(start.clone().add(0, 1.5, 0), Particle.SPELL_INSTANT, 2, times);
    }

    private static boolean isComplete(Block b) {
        String storage = BlockStorage.getLocationInfo(b.getLocation(),"complete");
        return Boolean.parseBoolean(storage);
    }
}
