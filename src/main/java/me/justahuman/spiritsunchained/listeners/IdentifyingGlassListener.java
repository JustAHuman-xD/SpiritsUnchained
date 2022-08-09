package me.justahuman.spiritsunchained.listeners;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;

import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;
import me.justahuman.spiritsunchained.utils.LogUtils;
import me.justahuman.spiritsunchained.utils.MiscUtils;
import me.justahuman.spiritsunchained.implementation.tools.IdentifyingGlass;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class IdentifyingGlassListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onSpyglassLook(PlayerStatisticIncrementEvent evt) {
        Player player = evt.getPlayer();
        if (evt.getStatistic() != Statistic.USE_ITEM || evt.getMaterial() != Material.SPYGLASS) return;
        player.sendMessage("Event Spyglass");
        if (!(SlimefunItem.getByItem(player.getActiveItem()) instanceof IdentifyingGlass)) return;
        player.sendMessage("Knows IdentifyingGlass");
        List<Entity> lookingAt = getLookingList(player);
        for (Entity currentEntity : lookingAt) {
            if (currentEntity.getType() != EntityType.ARMOR_STAND) continue;
            if (PersistentDataAPI.getBoolean(currentEntity, MiscUtils.spiritEntityKey)) {
                PersistentDataAPI.setBoolean(currentEntity, MiscUtils.spiritRevealedKey, true);
            }
        }

    }

    private List<Entity> getLookingList(Player player){
        List<Entity> entities = new ArrayList<>();
        for(Entity e : player.getNearbyEntities(10, 10, 10)){
            if(e instanceof ArmorStand){
                if(getLookingAt(player, (LivingEntity) e)){
                    entities.add(e);
                }
            }
        }

        return entities;
    }

    private boolean getLookingAt(Player player, LivingEntity livingEntity){
        Location eye = player.getEyeLocation();
        Vector toEntity = livingEntity.getLocation().toVector().subtract(eye.toVector());
        double dot = toEntity.normalize().dot(eye.getDirection());
        LogUtils.LogInfo("Dot: " + dot);
        return dot > 0.99D;
    }
}
