package me.justahuman.spiritsunchained.utils;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

import static me.justahuman.spiritsunchained.utils.SpiritUtils.getNearImbued;

public class ParticleUtils {

    private static double[][] sphere;

    public static void setup() {
        sphere = new double[11 * 10 * 2][];
        int sphereLoc = 0;
        for (double i = 0; i <= Math.PI; i += Math.PI / 10) {
            double radius = Math.sin(i);
            double y = Math.cos(i);
            for (double a = 0; a < Math.PI * 2; a+= Math.PI / 10) {
                double x = Math.cos(a) * radius;
                double z = Math.sin(a) * radius;
                sphere[sphereLoc] = new double[] { x, y, z };
                sphereLoc++;
            }
        }
    }

    public static void spawnParticleRadius(Location location, Particle particle, double radius, int amount, boolean stopMovements, boolean special, Object... other) {
        for (int i = 0; i < amount; i++) {
            final double x = ThreadLocalRandom.current().nextDouble(- radius, radius + 0.1);
            final double y = ThreadLocalRandom.current().nextDouble(- radius, radius + 0.1);
            final double z = ThreadLocalRandom.current().nextDouble(- radius, radius + 0.1);
            final World world = location.getWorld();
            if (!special) {
                if (stopMovements) {
                    if (particle == Particle.REDSTONE && other.length > 0) {
                        world.spawnParticle(particle, location.clone().add(x, y, z), 1, 0, 0, 0,(Particle.DustOptions) other[0]);
                    } else {
                        world.spawnParticle(particle, location.clone().add(x, y, z), 1, 0, 0, 0, 0);
                    }
                    continue;
                }
                world.spawnParticle(particle, location.clone().add(x, y, z), 1);
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
        final World world = location.getWorld();
        for (double[] offsets : sphere) {
            final Location particleLocation = location.clone().add(offsets[0], offsets[1], offsets[2]);
            final Vector direction = location.clone().subtract(particleLocation.clone()).toVector();
            world.spawnParticle(Particle.END_ROD, particleLocation, 0, direction.getX(), direction.getY(), direction.getZ(), 0.1);
        }
    }

    public static void bottleAnimation(Location location) {
        // TODO Polish and Add This
    }

    public static void passOnAnimation(Location location) {
        // TODO Polish and Add This
    }
}
