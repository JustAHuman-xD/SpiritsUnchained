package me.justahuman.spiritsunchained.listeners;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;
import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.justahuman.spiritsunchained.managers.SpiritsManager;
import me.justahuman.spiritsunchained.utils.Keys;
import me.justahuman.spiritsunchained.utils.SpiritTraits;
import me.justahuman.spiritsunchained.utils.SpiritUtils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

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

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack item = e.getPlayer().getEquipment().getItem(e.getHand());
            ItemMeta meta = item.getItemMeta();
            if (meta == null) {
                return;
            }
            if (SpiritUtils.isSpiritItem(item)) {
                String type = PersistentDataAPI.getString(meta, Keys.spiritItemKey);
                Map<String, Object> traitInfo = SpiritUtils.getTraitInfo(SpiritsUnchained.getSpiritsManager().getSpiritMap().get(EntityType.valueOf(type)).getTrait());
                SpiritTraits.useTrait(e.getPlayer(), traitInfo);
            }
        }
    }
}
