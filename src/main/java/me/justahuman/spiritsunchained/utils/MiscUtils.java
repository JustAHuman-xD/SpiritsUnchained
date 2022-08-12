package me.justahuman.spiritsunchained.utils;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;

import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.justahuman.spiritsunchained.implementation.tools.SpiritLenses;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

public class MiscUtils {

    private final static SpiritsUnchained instance = SpiritsUnchained.getInstance();
    public final static NamespacedKey EntityKey = new NamespacedKey(instance, "living-entity");
    public final static NamespacedKey spiritTypeKey = new NamespacedKey(instance, "spirit-type");
    public final static NamespacedKey spiritIdentified = new NamespacedKey(instance, "identified");
    public final static NamespacedKey spiritStateKey = new NamespacedKey(instance, "state");
    public final static NamespacedKey imbuedKey = new NamespacedKey(instance, "imbued");
    public final static NamespacedKey spiritItemKey = new NamespacedKey(instance, "spirit-item");
    public final static NamespacedKey spiritProgressKey = new NamespacedKey(instance, "state-progress");
    public static int totalSpiritCount = 0;

    public static boolean imbuedCheck(ItemStack helmetItem) {
        return SlimefunItem.getByItem(helmetItem) instanceof SpiritLenses || PersistentDataAPI.hasByte(helmetItem.getItemMeta(), MiscUtils.imbuedKey) && PersistentDataAPI.getByte(helmetItem.getItemMeta(), MiscUtils.imbuedKey) == 2;
    }

    public static Collection<Player> getNearImbued(Location location) {
        Collection<Entity> collection = location.getWorld().getNearbyEntities(location, 48, 48, 48);
        Collection<Player> toReturn = new ArrayList<>();
        for (Entity entity : collection) {
            if (entity instanceof Player player) {
                ItemStack helmetItem = player.getInventory().getHelmet();
                if (helmetItem == null) {continue;}
                if (imbuedCheck(helmetItem)) {
                    toReturn.add(player);
                }
            }
        }
        return toReturn;
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
}
