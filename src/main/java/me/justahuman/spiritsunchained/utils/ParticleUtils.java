package me.justahuman.spiritsunchained.utils;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.Set;

import static me.justahuman.spiritsunchained.utils.SpiritUtils.getNearImbued;

public class ParticleUtils {
    private static double[][] sphere;
    public static void setup() {
        sphere = new double[7 * 6 * 2][];
        int sphereLoc = 0;
        for (double i = 0; i <= Math.PI; i += Math.PI / 6) {
            double radius = Math.sin(i);
            double y = Math.cos(i);
            for (double a = 0; a < Math.PI * 2; a+= Math.PI / 6) {
                double x = Math.cos(a) * radius;
                double z = Math.sin(a) * radius;
                sphere[sphereLoc] = new double[] { x, y, z };
                sphereLoc++;
            }
        }
    }

    public static void spawnParticleRadius(Location location, Particle particle, double radius, int amount, String type, Object... other) {
        for (int i = 0; i < amount; i++) {
            final double x = SpiritUtils.random(- radius, radius + 0.1);
            final double y = SpiritUtils.random(- radius, radius + 0.1);
            final double z = SpiritUtils.random(- radius, radius + 0.1);
            final World world = location.getWorld();
            switch (type) {
                case "Colored" -> world.spawnParticle(particle, location.clone().add(x, y, z), 1, 0, 0, 0,(Particle.DustOptions) other[0]);
                case "Freeze" -> world.spawnParticle(particle, location.clone().add(x, y, z), 1, 0, 0, 0, 0);
                case "Spirit" -> {
                    final Set<Player> set = getNearImbued(location);
                    for (Player player : set) {
                        player.spawnParticle(particle, location.clone().add(x, y, z), 1, 0, 0, 0, 0);
                    }
                }
                default -> world.spawnParticle(particle, location.clone().add(x,y,z), 1);
            }
        }
    }

    public static void catchAnimation(Location location) {
        final World world = location.getWorld();
        world.playSound(location, Sound.ENTITY_ENDER_EYE_DEATH, 1, 1);
        for (double[] offsets : sphere) {
            final Location particleLocation = location.clone().add(offsets[0], offsets[1], offsets[2]);
            final Vector direction = location.clone().subtract(particleLocation.clone()).toVector();
            world.spawnParticle(Particle.END_ROD, particleLocation, 0, direction.getX(), direction.getY(), direction.getZ(), 0.1);
        }
    }

    public static void bottleAnimation(Location location) {
        final World world = location.getWorld();
        world.playSound(location, Sound.ENTITY_ENDER_EYE_DEATH, 1, 1);
        double[] speeds = new double[] {0.1, 0.15, 0.2, 0.25,};
        for (double[] offsets : sphere) {
            world.spawnParticle(Particle.END_ROD, location.clone().add(offsets[0], offsets[1], offsets[2]), 0, 0, 5, 0, speeds[SpiritUtils.random(0, speeds.length)]);
        }
    }

    public static void passOnAnimation(Location location) {
        final World world = location.getWorld();
        location = location.clone().add(0, 0.5, 0);
        world.playSound(location, Sound.ENTITY_ENDER_EYE_DEATH, 1, 1);
        for (double[] offsets : sphere) {
            final Location particleLocation = location.clone().add(offsets[0], offsets[1], offsets[2]);
            final Vector direction = particleLocation.clone().subtract(location.clone()).toVector();
            world.spawnParticle(Particle.END_ROD, particleLocation, 0, direction.getX(), direction.getY(), direction.getZ(), 0.25);
        }
    }
}
