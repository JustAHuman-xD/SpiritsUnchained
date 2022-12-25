package me.justahuman.spiritsunchained.listeners;

import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.justahuman.spiritsunchained.implementation.mobs.AbstractCustomMob;
import me.justahuman.spiritsunchained.spirits.SpiritDefinition;
import me.justahuman.spiritsunchained.utils.SpiritUtils;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class PlayerReleaseSpiritListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void playerReleaseSpirit(EntityDeathEvent e) {
        final SpiritsUnchained instance = SpiritsUnchained.getInstance();
        final FileConfiguration config = instance.getConfig();
        final LivingEntity killedEntity = e.getEntity();
        final Player player = killedEntity.getKiller();
        final boolean spawnerSpirits = config.getBoolean("options.spawner-spirits", false);

        if (player == null || (player.getGameMode() != GameMode.SURVIVAL && config.getBoolean("options.require-survival", true))) {
            return;
        }

        final EntityType type = killedEntity.getType();
        final AbstractCustomMob<?> spirit = SpiritsUnchained.getSpiritEntityManager().getCustomClass(null, type + "_SPIRIT");

        if ( spirit == null) {
            return;
        }

        final SpiritDefinition definition = SpiritsUnchained.getSpiritsManager().getSpiritMap().get(type);
        if (PlayerArmorListener.getCanSeeUUIDList().contains(player.getUniqueId()) && SpiritUtils.chance(10/definition.getTier()) && SpiritUtils.getNearbySpirits(killedEntity.getLocation(), 64).size() < SpiritUtils.getPlayerCap() && (spawnerSpirits || ! killedEntity.fromMobSpawner()) && ! config.getStringList("options.disabled-worlds").contains(killedEntity.getWorld().getName())) {
            spirit.spawn(killedEntity.getLocation(), killedEntity.getWorld(), "Hostile", null);
        }
    }
}
