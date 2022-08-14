package me.justahuman.spiritsunchained.listeners;

import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;

import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.justahuman.spiritsunchained.implementation.mobs.AbstractCustomMob;
import me.justahuman.spiritsunchained.utils.Keys;
import me.justahuman.spiritsunchained.utils.SpiritUtils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Allay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class PlayerClickListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getAction() == Action.LEFT_CLICK_AIR && player.isSneaking()) {
            ItemStack mainHand = player.getInventory().getItemInMainHand();
            if (mainHand.hasItemMeta() && PersistentDataAPI.hasByte(mainHand.getItemMeta(), Keys.imbuedKey)) {
                ItemMeta mainMeta = mainHand.getItemMeta();
                String Message;
                PersistentDataAPI.setByte(mainMeta, Keys.imbuedKey, PersistentDataAPI.getByte(mainMeta, Keys.imbuedKey) == (byte) 1 ? (byte) 2 : (byte) 1);
                Message = PersistentDataAPI.getByte(mainMeta, Keys.imbuedKey) == (byte) 1 ? "Toggled Off Visibility" : "Toggled On Visibility";
                mainHand.setItemMeta(mainMeta);
                player.sendMessage(ChatColor.LIGHT_PURPLE + Message);
            }
        } else if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            List<Entity> lookingAt = SpiritUtils.getLookingList(player);
            for (Entity entity : lookingAt) {
                AbstractCustomMob<?> maybe = SpiritsUnchained.getSpiritEntityManager().getCustomClass(entity, null);
                if (entity instanceof Allay && maybe != null && player.getLocation().distance(entity.getLocation()) < 4) {
                    e.setCancelled(true);
                    maybe.onInteract(new PlayerInteractEntityEvent(player, entity, e.getHand()));
                    break;
                }
            }
        }
    }
}
