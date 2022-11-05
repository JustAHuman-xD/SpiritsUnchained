package me.justahuman.spiritsunchained.listeners;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.common.ChatColors;
import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;

import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.justahuman.spiritsunchained.implementation.mobs.AbstractCustomMob;
import me.justahuman.spiritsunchained.implementation.mobs.Spirit;
import me.justahuman.spiritsunchained.implementation.mobs.UnIdentifiedSpirit;
import me.justahuman.spiritsunchained.slimefun.ItemStacks;
import me.justahuman.spiritsunchained.spirits.SpiritDefinition;
import me.justahuman.spiritsunchained.utils.Keys;
import me.justahuman.spiritsunchained.utils.SpiritUtils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Allay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;

public class IdentifyingGlassListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onSpyglassLook(PlayerStatisticIncrementEvent evt) {
        final Player player = evt.getPlayer();

        if (evt.getStatistic() != Statistic.USE_ITEM || evt.getMaterial() != Material.SPYGLASS || !Slimefun.getProtectionManager().hasPermission(player, player.getLocation(), Interaction.INTERACT_ENTITY)) {
            return;
        }

        SlimefunItem slimefunItem1 = SlimefunItem.getByItem(player.getInventory().getItemInMainHand());
        SlimefunItem slimefunItem2 = SlimefunItem.getByItem(player.getInventory().getItemInOffHand());
        if (!((slimefunItem1 != null && slimefunItem1.getId().equals(ItemStacks.SU_IDENTIFYING_GLASS.getItemId())) || (slimefunItem2 != null && slimefunItem2.getId().equals(ItemStacks.SU_IDENTIFYING_GLASS.getItemId())))) {
            return;
        }

        for (Entity currentEntity : SpiritUtils.getLookingList(player)) {
            if (currentEntity.getType() != EntityType.ALLAY) continue;
            final AbstractCustomMob<?> maybeSpirit = SpiritsUnchained.getSpiritEntityManager().getCustomClass(currentEntity, null);
            if (maybeSpirit instanceof UnIdentifiedSpirit && !PersistentDataAPI.getBoolean(currentEntity, Keys.spiritIdentified)) {
                PersistentDataAPI.setBoolean(currentEntity, Keys.spiritIdentified, true);
                maybeSpirit.reveal((Allay) currentEntity, player);
            } else if (maybeSpirit instanceof Spirit spirit) {
                final SpiritDefinition definition = spirit.getDefinition();
                final ChatColor tierColor = SpiritUtils.tierColor(definition.getTier());
                final ChatColor stateColor = SpiritUtils.stateColor(PersistentDataAPI.getString(currentEntity, Keys.spiritStateKey));
                final String actionBarMessage = ChatColors.color("&fSpirit Type: " + tierColor + ChatUtils.humanize(definition.getType().name()) + "   &fCurrent State: " + stateColor + PersistentDataAPI.getString(currentEntity, Keys.spiritStateKey) + "   &fTier: " + tierColor + definition.getTier());
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(actionBarMessage));
            }
        }
    }
}
