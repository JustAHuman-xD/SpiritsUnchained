package me.justahuman.spiritsunchained.listeners;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import lombok.Getter;
import me.justahuman.spiritsunchained.utils.SpiritUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerArmorListener implements Listener {
    @Getter
    private static final List<UUID> canSeeUUIDList = new ArrayList<>();
    
    @EventHandler
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
}
