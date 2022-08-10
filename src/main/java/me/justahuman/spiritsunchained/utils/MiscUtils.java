package me.justahuman.spiritsunchained.utils;

import me.justahuman.spiritsunchained.SpiritsUnchained;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;

import java.util.concurrent.ThreadLocalRandom;

public class MiscUtils {

    private final static SpiritsUnchained instance = SpiritsUnchained.getInstance();
    public final static NamespacedKey EntityKey = new NamespacedKey(instance, "living-entity");
    public final static NamespacedKey spiritTypeKey = new NamespacedKey(instance, "spirit-type");
    public final static NamespacedKey spiritStateKey = new NamespacedKey(instance, "state");

    public static void spawnParticleRadius(Location location, Particle particle, double radius, int amount, boolean stopMovements) {
        for (int i = 0; i < amount; i++) {
            double x = ThreadLocalRandom.current().nextDouble(- radius, radius + 0.1);
            double y = ThreadLocalRandom.current().nextDouble(- radius, radius + 0.1);
            double z = ThreadLocalRandom.current().nextDouble(- radius, radius + 0.1);
            if (stopMovements) {
                location.getWorld().spawnParticle(particle, location.clone().add(x, y, z), 1, 0, 0, 0, 0);
                continue;
            }
            location.getWorld().spawnParticle(particle, location.clone().add(x, y, z), 1);
        }
    }
}
