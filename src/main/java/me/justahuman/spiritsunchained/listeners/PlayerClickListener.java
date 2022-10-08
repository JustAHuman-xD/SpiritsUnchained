package me.justahuman.spiritsunchained.listeners;

import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;

import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.justahuman.spiritsunchained.implementation.mobs.AbstractCustomMob;
import me.justahuman.spiritsunchained.utils.Keys;
import me.justahuman.spiritsunchained.utils.SpiritTraits;
import me.justahuman.spiritsunchained.utils.SpiritUtils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Allay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Map;

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
            if (!rightClick(player, player.getInventory().getItemInMainHand())) {
               if (rightClick(player, player.getInventory().getItemInOffHand())) {
                   return;
                }
            } else {
                return;
            }

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

    private boolean rightClick(Player player, ItemStack item) {
        player.sendMessage("Right Clicked");
        if (item.getType() == Material.FIREWORK_STAR) {
            player.sendMessage("Fire Star *insert eyes*");
            if (SpiritUtils.isSpiritItem(item)) {
                player.sendMessage("Spirit *double eyes*");
                String type = PersistentDataAPI.getString(item.getItemMeta(), Keys.spiritItemKey);
                Map<String, Object> traitInfo = SpiritUtils.getTraitInfo(SpiritsUnchained.getSpiritsManager().getSpiritMap().get(EntityType.valueOf(type)).getTrait());
                player.sendMessage(SpiritTraits.useTrait(player, traitInfo));
                return true;
            }
        }
        if (item.getType() == Material.BAMBOO && SpiritUtils.useSpiritItem(player, EntityType.PANDA)) {
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 1, 1);
            item.subtract();
            player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 1, 4, false, false));
            return true;
        }
        return false;
    }
}
