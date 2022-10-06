package me.justahuman.spiritsunchained.listeners;

import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;
import me.justahuman.spiritsunchained.utils.Keys;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

public class TraitListeners implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        if (!PersistentDataAPI.hasString(projectile, Keys.entityKey)) {
            return;
        }

        switch (PersistentDataAPI.getString(projectile, Keys.entityKey)) {
            default -> {}
            case "Eggpult" -> {
                TNTPrimed tnt = (TNTPrimed) projectile.getWorld().spawnEntity(projectile.getLocation(), EntityType.PRIMED_TNT);
                tnt.setFuseTicks(1);
                PersistentDataAPI.setString(tnt, Keys.entityKey, "DullExplosion");
                PersistentDataAPI.setString(tnt, Keys.immuneKey, PersistentDataAPI.getString(projectile, Keys.ownerKey));
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onTntExplode(EntityExplodeEvent event) {
        Entity exploding = event.getEntity();
        if (!PersistentDataAPI.hasString(exploding, Keys.entityKey)) {
            return;
        }

        switch (PersistentDataAPI.getString(exploding, Keys.entityKey)) {
            default -> {}
            case "DullExplosion" -> {
                event.blockList().clear();
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        Entity attacker = event.getDamager();

        if (PersistentDataAPI.hasString(attacker, Keys.immuneKey) && PersistentDataAPI.getString(attacker, Keys.immuneKey).equals(player.getUniqueId().toString())) {
            event.setCancelled(true);
            return;
        } else if(PersistentDataAPI.hasBoolean(attacker, Keys.heavyHitKey) && PersistentDataAPI.getBoolean(attacker, Keys.heavyHitKey)) {
            player.setVelocity(new Vector(0, 15, 0));
        }
    }
}
