package me.justahuman.spiritsunchained.utils;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static me.justahuman.spiritsunchained.utils.SpiritUtils.getNearImbued;

public class ParticleUtils {

    private static Set<Vector> sphereList;

    public static void setup() {
        sphereList = new HashSet<>();
        int loc = 0;
        for (double i = 0; i <= Math.PI; i += Math.PI / 10) {
            double radius = Math.sin(i);
            double y = Math.cos(i);
            for (double a = 0; a < Math.PI * 2; a+= Math.PI / 10) {
                double x = Math.cos(a) * radius;
                double z = Math.sin(a) * radius;
                sphereList.add(new Vector(x,y,z));
            }
        }
    }

    public static void spawnParticleRadius(Location location, Particle particle, double radius, int amount, boolean stopMovements, boolean special) {
        for (int i = 0; i < amount; i++) {
            double x = ThreadLocalRandom.current().nextDouble(- radius, radius + 0.1);
            double y = ThreadLocalRandom.current().nextDouble(- radius, radius + 0.1);
            double z = ThreadLocalRandom.current().nextDouble(- radius, radius + 0.1);
            if (!special) {
                if (stopMovements) {
                    location.getWorld().spawnParticle(particle, location.clone().add(x, y, z), 1, 0, 0, 0, 0);
                    continue;
                }
                location.getWorld().spawnParticle(particle, location.clone().add(x, y, z), 1);
            } else {
                Collection<Player> collection = getNearImbued(location);
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

    public static Set<Vector> getSphereList() {
        return sphereList;
    }

    public static void catchAnimation(Location location) {
        for (Vector vector : getSphereList()) {
            Location particle = location.clone();
            particle.add(vector);
            location.getWorld().spawnParticle(Particle.END_ROD, particle, 1, location.getX(), location.getY(), location.getZ(), 0.01);
        }
    }

    public static void bottleAnimation(Location location) {

    }
}
