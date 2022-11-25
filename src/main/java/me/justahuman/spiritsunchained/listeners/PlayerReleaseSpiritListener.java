package me.justahuman.spiritsunchained.listeners;

import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.justahuman.spiritsunchained.implementation.mobs.AbstractCustomMob;
import me.justahuman.spiritsunchained.spirits.SpiritDefinition;
import me.justahuman.spiritsunchained.utils.SpiritUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

public class PlayerReleaseSpiritListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerReleaseSpirit(EntityDeathEvent e) {
        final SpiritsUnchained instance = SpiritsUnchained.getInstance();
        final FileConfiguration config = instance.getConfig();
        final LivingEntity killedEntity = e.getEntity();
        final Player player = killedEntity.getKiller();
        final boolean spawnerSpirits = config.getBoolean("options.spawner-spirits", false);

        if (player == null) {
            return;
        }

        final ItemStack helmetItem = player.getInventory().getHelmet();

        if (helmetItem == null) {
            return;
        }

        final EntityType type = killedEntity.getType();
        final AbstractCustomMob<?> spirit = SpiritsUnchained.getSpiritEntityManager().getCustomClass(null, type + "_SPIRIT");

        if ( spirit == null) {
            return;
        }

        final SpiritDefinition definition = SpiritsUnchained.getSpiritsManager().getSpiritMap().get(type);
        final int chance = ThreadLocalRandom.current().nextInt(1, 100);
        if (SpiritUtils.imbuedCheck(helmetItem) && chance <= 10/definition.getTier() && SpiritUtils.getNearbySpirits(killedEntity.getLocation()).size() < SpiritUtils.getPlayerCap() && (spawnerSpirits || ! killedEntity.fromMobSpawner()) && ! config.getStringList("options.disabled-worlds").contains(killedEntity.getWorld().getName())) {
            spirit.spawn(killedEntity.getLocation(), killedEntity.getWorld(), "Hostile", null);
        }
    }
}
