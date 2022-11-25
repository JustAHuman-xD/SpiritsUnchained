package me.justahuman.spiritsunchained.managers;

import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;

import me.justahuman.spiritsunchained.SpiritsUnchained;

import me.justahuman.spiritsunchained.implementation.mobs.AbstractCustomMob;
import me.justahuman.spiritsunchained.utils.Keys;
import me.justahuman.spiritsunchained.utils.SpiritUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
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
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SpiritEntityManager implements Listener {

    public final Map<String, AbstractCustomMob<?>> entityMap = new HashMap<>();
    public final Collection<LivingEntity> entityCollection = new ArrayList<>();

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

    private void tick() {
        for (World world : Bukkit.getWorlds()) {
            for (LivingEntity entity : world.getLivingEntities()) {
                final AbstractCustomMob<?> customMob = getCustomClass(entity, null);
                if (customMob != null) {
                    customMob.onEntityTick(entity);
                }
            }
        }
    }

    private void spawnTick() {
        for (World world : Bukkit.getWorlds()) {
            if (SpiritsUnchained.getInstance().getConfig().getStringList("options.disabled-worlds").contains(world.getName())) {
                continue;
            }
            for (Player player : world.getPlayers()) {
                final int spiritCount = SpiritUtils.getNearbySpirits(player.getLocation()).size();
                final ItemStack helmetItem = player.getInventory().getHelmet();
                if (helmetItem == null || !SpiritUtils.imbuedCheck(helmetItem)) {
                    continue;
                }
                if (SpiritUtils.canSpawn() && spiritCount < SpiritUtils.getPlayerCap() && SpiritUtils.chance(10)) {
                    final Block b = SpiritUtils.getSpawnBlock(player.getLocation());
                    final String maybeSpirit = SpiritUtils.getSpawnMob(b.getLocation());
                    if (maybeSpirit != null && this.entityMap.get("UNIDENTIFIED_SPIRIT") != null) {
                        this.entityMap.get("UNIDENTIFIED_SPIRIT").spawn(b.getLocation(), player.getWorld(), "Natural", maybeSpirit);
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    private void onEntityTarget(@Nonnull EntityTargetEvent e) {
        final AbstractCustomMob<?> customMob = getCustomClass(e.getEntity(), null);
        if (customMob != null) {
            customMob.onTarget(e);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    private void onEntityInteract(@Nonnull PlayerInteractEntityEvent e) {
        final AbstractCustomMob<?> customMob = getCustomClass(e.getRightClicked(), null);
        if (customMob != null) {
            customMob.onInteract(e);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    private void onEntityHit(@Nonnull EntityDamageByEntityEvent e) {
        final AbstractCustomMob<?> customMob = getCustomClass(e.getEntity(), null);
        if (customMob != null) {
            customMob.onHit(e);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    private void onEntityDie(@Nonnull EntityDeathEvent e) {
        final AbstractCustomMob<?> customMob = getCustomClass(e.getEntity(), null);
        if (customMob != null) {
            customMob.onDeath(e);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    private void onEntityCombust(@Nonnull EntityCombustEvent e) {
        final AbstractCustomMob<?> customMob = getCustomClass(e.getEntity(), null);
        if (customMob != null) {
            e.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    private void onEntityDamage(@Nonnull EntityDamageEvent e) {
        final AbstractCustomMob<?> customMob = getCustomClass(e.getEntity(), null);
        if (customMob != null) {
            customMob.onDamage(e);
        }
    }
}
