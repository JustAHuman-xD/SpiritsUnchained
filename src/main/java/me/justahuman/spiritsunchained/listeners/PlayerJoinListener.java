package me.justahuman.spiritsunchained.listeners;

import me.justahuman.spiritsunchained.SpiritsUnchained;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        PlayerArmorListener.updatePlayer(player);
        for (LivingEntity entity : SpiritsUnchained.getSpiritEntityManager().getCustomLivingEntities()) {
            if (player.canSee(entity)) {
                player.hideEntity(SpiritsUnchained.getInstance(), entity);
            }
        }
    }
}
