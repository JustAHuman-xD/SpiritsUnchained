package me.justahuman.spiritsunchained.managers;

import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;

import me.justahuman.spiritsunchained.SpiritsUnchained;

import me.justahuman.spiritsunchained.implementation.mobs.AbstractCustomMob;
import me.justahuman.spiritsunchained.utils.MiscUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class SpiritEntityManager implements Listener {

    public final Map<String, AbstractCustomMob<?>> EntityMap = new HashMap<>();

    public SpiritEntityManager() {
        int tickRate = SpiritsUnchained.getInstance().getConfig().getInt("tick-rate", 2);
        if (tickRate > 20) {
            tickRate = 20;
        }
        SpiritsUnchained.getPluginManager().registerEvents(this, SpiritsUnchained.getInstance());
        Bukkit.getScheduler().runTaskTimer(SpiritsUnchained.getInstance(), this::tick, tickRate, Math.max(1, tickRate));
    }

    public void register(AbstractCustomMob<?> customMob) {
        if (this.EntityMap.containsKey(customMob.getId())) {
            throw new IllegalArgumentException("Custom Entity Already Registered!" + customMob.getId());
        }
        this.EntityMap.put(customMob.getId(), customMob);
    }

    public AbstractCustomMob<?> getCustomClass(Entity entity, String key) {
        String getKey = null;
        if (entity != null) {
            getKey = PersistentDataAPI.getString(entity, MiscUtils.EntityKey);
        } else if (key != null) {
            getKey = key;
        }
        return getKey == null ? null : this.EntityMap.get(getKey);
    }

    private void tick() {
        for (AbstractCustomMob<?> customMob : this.EntityMap.values()) {
            customMob.onUniqueTick();
        }

        for (World world : Bukkit.getWorlds()) {
            for (LivingEntity entity : world.getLivingEntities()) {
                AbstractCustomMob<?> customMob = getCustomClass(entity, null);
                if (customMob != null) {
                    customMob.onEntityTick(entity);
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    private void onEntityTarget(@Nonnull EntityTargetEvent e) {
        AbstractCustomMob<?> customMob = getCustomClass(e.getEntity(), null);
        if (customMob != null) {
            customMob.onTarget(e);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    private void onEntityInteract(@Nonnull PlayerInteractEntityEvent e) {
        AbstractCustomMob<?> customMob = getCustomClass(e.getRightClicked(), null);
        if (customMob != null) {
            customMob.onInteract(e);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    private void onEntityHit(@Nonnull EntityDamageByEntityEvent e) {
        AbstractCustomMob<?> customMob = getCustomClass(e.getEntity(), null);
        if (customMob != null) {
            customMob.onHit(e);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    private void onEntityDie(@Nonnull EntityDeathEvent e) {
        AbstractCustomMob<?> customMob = getCustomClass(e.getEntity(), null);
        if (customMob != null) {
            customMob.onDeath(e);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    private void onEntityCombust(@Nonnull EntityCombustEvent e) {
        AbstractCustomMob<?> customMob = getCustomClass(e.getEntity(), null);
        if (customMob != null) {
            e.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    private void onEntityDamage(@Nonnull EntityDamageEvent e) {
        AbstractCustomMob<?> customMob = getCustomClass(e.getEntity(), null);
        if (customMob != null) {
            customMob.onDamage(e);
        }
    }
}
