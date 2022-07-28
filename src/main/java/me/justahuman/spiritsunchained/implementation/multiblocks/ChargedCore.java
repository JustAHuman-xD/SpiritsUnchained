package me.justahuman.spiritsunchained.implementation.multiblocks;

import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Random;

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
        for (int i = 0; i < times; i++){
            Random random = new Random();
            double X = start.getX() + random.nextDouble(2 + 2) - 2;
            double Y = start.getY() + random.nextDouble(3);
            double Z = start.getZ() + random.nextDouble(2 + 2) - 2;
            Location end = new Location(start.getWorld(),X,Y,Z);
            start.getWorld().spawnParticle(Particle.END_ROD, end, 1, 0, 0,0,0);
        }
    }

    private static boolean isComplete(Block b) {
        String storage = BlockStorage.getLocationInfo(b.getLocation(),"complete");
        return Boolean.parseBoolean(storage);
    }
}
