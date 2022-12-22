package me.justahuman.spiritsunchained.listeners;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import lombok.Getter;
import me.justahuman.spiritsunchained.utils.SpiritUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseArmorEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerArmorListener implements Listener {
    @Getter
    private static final Set<UUID> canSeeUUIDList = new HashSet<>();
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerArmorChange(PlayerArmorChangeEvent event) {
        final Player player = event.getPlayer();
        final ItemStack newItem = event.getNewItem();
        final PlayerArmorChangeEvent.SlotType slotType = event.getSlotType();
        
        if (slotType != PlayerArmorChangeEvent.SlotType.HEAD) {
            return;
        }
        
        if (!SpiritUtils.imbuedCheck(newItem)) {
            canSeeUUIDList.remove(player.getUniqueId());
            return;
        }
        
        canSeeUUIDList.add(player.getUniqueId());
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockDispenseArmor(BlockDispenseArmorEvent event) {
        final Entity entity = event.getTargetEntity();
        final ItemStack newItem = event.getItem();
        
        if (!(entity instanceof Player player)) {
            return;
        }
    
        if (!SpiritUtils.imbuedCheck(newItem)) {
            canSeeUUIDList.remove(player.getUniqueId());
            return;
        }
    
        canSeeUUIDList.add(player.getUniqueId());
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onItemBreak(PlayerItemBreakEvent event) {
        final Player player = event.getPlayer();
        final ItemStack brokenItem = event.getBrokenItem();
        
        if (SpiritUtils.imbuedCheck(brokenItem)) {
            canSeeUUIDList.remove(player.getUniqueId());
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPostRespawn(PlayerPostRespawnEvent event) {
        updatePlayer(event.getPlayer());
    }
    
    public static void updatePlayer(Player player) {
        final ItemStack helmetItem = player.getInventory().getItem(EquipmentSlot.HEAD);
        if (SpiritUtils.imbuedCheck(helmetItem)) {
            canSeeUUIDList.add(player.getUniqueId());
        } else {
            canSeeUUIDList.remove(player.getUniqueId());
        }
    }
}
