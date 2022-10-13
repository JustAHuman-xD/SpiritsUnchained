package me.justahuman.spiritsunchained.utils;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

import static me.justahuman.spiritsunchained.utils.SpiritUtils.getNearImbued;

public class ParticleUtils {

    public static void setup() {
        // TODO might need to use this for later Visuals
    }

    public static void spawnParticleRadius(Location location, Particle particle, double radius, int amount, boolean stopMovements, boolean special, Particle.DustOptions... other) {
        for (int i = 0; i < amount; i++) {
            final double x = ThreadLocalRandom.current().nextDouble(- radius, radius + 0.1);
            final double y = ThreadLocalRandom.current().nextDouble(- radius, radius + 0.1);
            final double z = ThreadLocalRandom.current().nextDouble(- radius, radius + 0.1);
            if (!special) {
                if (stopMovements) {
                    if (particle == Particle.REDSTONE && other.length > 0) {
                        location.getWorld().spawnParticle(particle, location.clone().add(x, y, z), 1, 0, 0, 0, other[0]);
                    } else {
                        location.getWorld().spawnParticle(particle, location.clone().add(x, y, z), 1, 0, 0, 0, 0);
                    }
                    continue;
                }
                location.getWorld().spawnParticle(particle, location.clone().add(x, y, z), 1);
            } else {
                final Collection<Player> collection = getNearImbued(location);
                for (Player player : collection) {
                    if (stopMovements) {
                        player.spawnParticle(particle, location.clone().add(x, y, z), 1, 0, 0, 0, 0);
                        continue;
                    }
                    player.spawnParticle(particle, location.clone().add(x, y, z), 1);
                }
            }
        }
    }

    public static void catchAnimation(Location location) {
        // TODO Polish and Add This
    }

    public static void bottleAnimation(Location location) {
        // TODO Polish and Add This
    }
}
