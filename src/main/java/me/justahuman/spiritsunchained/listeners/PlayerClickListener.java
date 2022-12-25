package me.justahuman.spiritsunchained.listeners;

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.common.ChatColors;
import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.justahuman.spiritsunchained.implementation.mobs.AbstractCustomMob;
import me.justahuman.spiritsunchained.utils.Keys;
import me.justahuman.spiritsunchained.utils.SpiritTraits;
import me.justahuman.spiritsunchained.utils.SpiritUtils;
import net.kyori.adventure.text.Component;
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
        final Player player = e.getPlayer();
        if (e.getAction() == Action.LEFT_CLICK_AIR && player.isSneaking()) {
            final ItemStack mainHand = player.getInventory().getItemInMainHand();
            if (mainHand.hasItemMeta() && PersistentDataAPI.hasByte(mainHand.getItemMeta(), Keys.imbuedKey)) {
                final ItemMeta mainMeta = mainHand.getItemMeta();
                final String Message;
                PersistentDataAPI.setByte(mainMeta, Keys.imbuedKey, PersistentDataAPI.getByte(mainMeta, Keys.imbuedKey) == (byte) 1 ? (byte) 2 : (byte) 1);
                Message = PersistentDataAPI.getByte(mainMeta, Keys.imbuedKey) == (byte) 1 ? SpiritUtils.getTranslation("messages.spirit_rune.toggled_off") : SpiritUtils.getTranslation("messages.spirit_rune.toggled_on");
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

            final List<Entity> lookingAt = SpiritUtils.getLookingList(player);
            for (Entity entity : lookingAt) {
                final AbstractCustomMob<?> maybe = SpiritsUnchained.getSpiritEntityManager().getCustomClass(entity, null);
                if (entity instanceof Allay && maybe != null && player.getLocation().distanceSquared(entity.getLocation()) < Math.pow(4, 2) && e.getHand() != null) {
                    if (!Slimefun.getProtectionManager().hasPermission(player, player.getLocation(), Interaction.INTERACT_ENTITY)) {
                        player.sendMessage(SpiritUtils.getTranslation("messages.general.no_permission_entity_interact"));
                        return;
                    }
                    e.setCancelled(true);
                    maybe.onInteract(new PlayerInteractEntityEvent(player, entity, e.getHand()));
                    return;
                }
            }
        }
    }

    private boolean rightClick(Player player, ItemStack item) {
        if (item.getType() == Material.FIREWORK_STAR && SpiritUtils.isSpiritItem(item)) {
            if (player.isSneaking()) {
                final ItemMeta meta = item.getItemMeta();
                PersistentDataAPI.setBoolean(meta, Keys.spiritLocked, !PersistentDataAPI.getBoolean(meta, Keys.spiritLocked));
                item.setItemMeta(meta);
                SpiritUtils.updateSpiritItemProgress(item, 0);
                player.sendActionBar(Component.text((PersistentDataAPI.getBoolean(meta, Keys.spiritLocked) ? SpiritUtils.getTranslation("messages.spirits.locked") : SpiritUtils.getTranslation("messages.spirits.unlocked"))));
                return true;
            }
            
            final String type = PersistentDataAPI.getString(item.getItemMeta(), Keys.spiritItemKey);
            final Map<String, Object> traitInfo = SpiritUtils.getTraitInfo(SpiritsUnchained.getSpiritsManager().getSpiritMap().get(EntityType.valueOf(type)).getTrait());
            final String message = SpiritTraits.useTrait(player, traitInfo, item);
            if (message != null) {
                player.sendActionBar(Component.text(ChatColors.color(message)));
            }
            
            return true;
        }
        if (item.getType() == Material.BAMBOO && SpiritUtils.useSpiritItem(player, EntityType.PANDA, null)) {
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 1, 1);
            item.subtract();
            player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 1, 1, false, false));
            return true;
        }
        return false;
    }
}
