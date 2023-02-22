package me.justahuman.spiritsunchained.managers;

import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;

import me.justahuman.spiritsunchained.SpiritsUnchained;

import me.justahuman.spiritsunchained.implementation.mobs.AbstractCustomMob;
import me.justahuman.spiritsunchained.listeners.PlayerArmorListener;
import me.justahuman.spiritsunchained.utils.Keys;
import me.justahuman.spiritsunchained.utils.SpiritUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class SpiritEntityManager implements Listener {

    public final Map<String, AbstractCustomMob<?>> entityMap = new HashMap<>();
    public final Set<UUID> entitySet = new HashSet<>();

    public SpiritEntityManager() {
        final SpiritsUnchained instance = SpiritsUnchained.getInstance();
        final FileConfiguration config = instance.getConfig();
        final int tickRate = Math.min(config.getInt("options.tick-rate", 2), 20);
        final int spawnRate = config.getInt("options.spawn-rate", 10) * 20;

        instance.getServer().getPluginManager().registerEvents(this, instance);
        Bukkit.getScheduler().runTaskTimer(instance, this::tick, tickRate, Math.max(1, tickRate));
        Bukkit.getScheduler().runTaskTimer(instance, this::spawnTick, 1, spawnRate);
    }

    public void register(AbstractCustomMob<?> customMob) {
        if (this.entityMap.containsKey(customMob.getId())) {
            throw new IllegalArgumentException("Custom Entity Already Registered!" + customMob.getId());
        }
        this.entityMap.put(customMob.getId(), customMob);
    }

    public AbstractCustomMob<?> getCustomClass(Entity entity, String key) {
        String getKey = entity != null && PersistentDataAPI.hasString(entity, Keys.entityKey) ? PersistentDataAPI.getString(entity, Keys.entityKey) : key;
        return getKey == null ? null : this.entityMap.get(getKey);
    }
    
    public Set<LivingEntity> getCustomLivingEntities() {
        final Set<LivingEntity> toReturn = new HashSet<>();
        for (UUID uuid : entitySet) {
            final Entity entity = Bukkit.getEntity(uuid);
            if (entity instanceof LivingEntity livingEntity) {
                toReturn.add(livingEntity);
            }
        }
        return toReturn;
    }

    private void tick() {
        for (LivingEntity livingEntity : getCustomLivingEntities()) {
            final AbstractCustomMob<?> customMob = getCustomClass(livingEntity, null);
            if (customMob != null) {
                customMob.onEntityTick(livingEntity);
            }
        }
    }

    private void spawnTick() {
        for (UUID uuid : PlayerArmorListener.getCanSeeUUIDList()) {
            final Player player = Bukkit.getPlayer(uuid);
            if (player == null || player.getGameMode() != GameMode.SURVIVAL || SpiritsUnchained.getInstance().getConfig().getStringList("options.disabled-worlds").contains(player.getWorld().getName())) {
                continue;
            }
            
            final int spiritCount = SpiritUtils.getNearbySpirits(player.getLocation(), 64).size();
            if (SpiritUtils.canSpawn() && spiritCount < SpiritUtils.getPlayerCap() && SpiritUtils.chance(10)) {
                final Block block = SpiritUtils.getSpawnBlock(player.getLocation());
                if (block == null) {
                    continue;
                }
                
                final String maybeSpirit = SpiritUtils.getSpawnMob(block.getLocation());
                if (maybeSpirit != null && this.entityMap.get("UNIDENTIFIED_SPIRIT") != null) {
                    this.entityMap.get("UNIDENTIFIED_SPIRIT").spawn(block.getLocation(), player.getWorld(), "Natural", maybeSpirit);
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    private void onEntityTarget(@Nonnull EntityTargetEvent event) {
        final AbstractCustomMob<?> customMob = getCustomClass(event.getEntity(), null);
        if (customMob != null) {
            customMob.onTarget(event);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    private void onEntityInteract(@Nonnull PlayerInteractEntityEvent event) {
        final AbstractCustomMob<?> customMob = getCustomClass(event.getRightClicked(), null);
        if (customMob != null) {
            customMob.onInteract(event);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    private void onEntityHit(@Nonnull EntityDamageByEntityEvent event) {
        final AbstractCustomMob<?> customMob = getCustomClass(event.getEntity(), null);
        if (customMob != null) {
            customMob.onHit(event);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    private void onEntityDie(@Nonnull EntityDeathEvent event) {
        final AbstractCustomMob<?> customMob = getCustomClass(event.getEntity(), null);
        if (customMob != null) {
            customMob.onDeath(event);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    private void onEntityCombust(@Nonnull EntityCombustEvent event) {
        final AbstractCustomMob<?> customMob = getCustomClass(event.getEntity(), null);
        if (customMob != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    private void onEntityDamage(@Nonnull EntityDamageEvent event) {
        final AbstractCustomMob<?> customMob = getCustomClass(event.getEntity(), null);
        if (customMob != null) {
            customMob.onDamage(event);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    private void onEntityRemove(@Nonnull EntityRemoveFromWorldEvent event) {
        final Entity entity = event.getEntity();
        final UUID uuid = entity.getUniqueId();
        this.entitySet.remove(uuid);
    }
}
