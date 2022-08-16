package me.justahuman.spiritsunchained.listeners;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import me.justahuman.spiritsunchained.utils.SpiritUtils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

public class SpiritItemListeners implements Listener {

    @EventHandler
    public void onTryCraft(CraftItemEvent e) {
        for (ItemStack item : e.getInventory().getMatrix()) {
            if (item != null && item.getType() != Material.AIR && SpiritUtils.isSpiritItem(item)) {
                e.setCancelled(true);
                for (HumanEntity player : e.getInventory().getViewers()) {
                    player.sendMessage(ChatColor.RED + "You can't craft with a Spirit!");
                }
                return;
            }
        }
    }

    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent e) {
        if (e.getInventory().getResult() != null) {
            for (ItemStack item : e.getInventory().getContents()) {
                if (item != null && item.getType() != Material.AIR && SpiritUtils.isSpiritItem(item)) {
                    e.getInventory().setResult(null);
                    break;
                }
            }
        }
    }
}
