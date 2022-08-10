package me.justahuman.spiritsunchained.listeners;

import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;

import me.justahuman.spiritsunchained.utils.MiscUtils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerToggleImbuedActiveListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getAction() == Action.LEFT_CLICK_AIR && player.isSneaking()) {
            ItemStack mainHand = player.getInventory().getItemInMainHand();
            if (mainHand.hasItemMeta() && PersistentDataAPI.hasByte(mainHand.getItemMeta(), MiscUtils.imbuedKey)) {
                ItemMeta mainMeta = mainHand.getItemMeta();
                String Message;
                PersistentDataAPI.setByte(mainMeta, MiscUtils.imbuedKey, PersistentDataAPI.getByte(mainMeta, MiscUtils.imbuedKey) == (byte) 1 ? (byte) 2 : (byte) 1);
                Message = PersistentDataAPI.getByte(mainMeta, MiscUtils.imbuedKey) == (byte) 1 ? "Toggled Off Visibility" : "Toggled On Visibility";
                mainHand.setItemMeta(mainMeta);
                player.sendMessage(ChatColor.LIGHT_PURPLE + Message);
            }
        }
    }
}
