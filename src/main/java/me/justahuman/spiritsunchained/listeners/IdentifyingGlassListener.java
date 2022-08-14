package me.justahuman.spiritsunchained.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.libraries.dough.common.ChatColors;
import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;

import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.justahuman.spiritsunchained.implementation.mobs.AbstractCustomMob;
import me.justahuman.spiritsunchained.implementation.mobs.Spirit;
import me.justahuman.spiritsunchained.implementation.mobs.UnIdentifiedSpirit;
import me.justahuman.spiritsunchained.spirits.SpiritDefinition;
import me.justahuman.spiritsunchained.implementation.tools.IdentifyingGlass;
import me.justahuman.spiritsunchained.utils.Keys;
import me.justahuman.spiritsunchained.utils.SpiritUtils;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Allay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class IdentifyingGlassListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onSpyglassLook(PlayerStatisticIncrementEvent evt) {
        Player player = evt.getPlayer();
        if (evt.getStatistic() != Statistic.USE_ITEM || evt.getMaterial() != Material.SPYGLASS) return;
        if (!(SlimefunItem.getByItem(player.getInventory().getItemInMainHand()) instanceof IdentifyingGlass || SlimefunItem.getByItem(player.getInventory().getItemInOffHand()) instanceof IdentifyingGlass)) return;
        List<Entity> lookingAt = SpiritUtils.getLookingList(player);
        for (Entity currentEntity : lookingAt) {
            if (currentEntity.getType() != EntityType.ALLAY) continue;
            AbstractCustomMob<?> maybeSpirit = SpiritsUnchained.getSpiritEntityManager().getCustomClass(currentEntity, null);
            if (maybeSpirit instanceof UnIdentifiedSpirit && !PersistentDataAPI.getBoolean(currentEntity, Keys.spiritIdentified)) {
                PersistentDataAPI.setBoolean(currentEntity, Keys.spiritIdentified, true);
                maybeSpirit.reveal((Allay) currentEntity, player);
            } else if (maybeSpirit instanceof Spirit spirit) {
                ProtocolManager manager = SpiritsUnchained.getProtocolManager();
                PacketContainer packet = manager.createPacket(PacketType.Play.Server.SET_ACTION_BAR_TEXT);
                SpiritDefinition definition = spirit.getDefinition();
                ChatColor tierColor = SpiritUtils.tierColor(definition.getTier());
                ChatColor stateColor = SpiritUtils.stateColor(PersistentDataAPI.getString(currentEntity, Keys.spiritStateKey));
                String actionBarMessage = ChatColors.color("&fSpirit Type: " + tierColor + ChatUtils.humanize(definition.getType().name()) + "   &fCurrent State: " + stateColor + PersistentDataAPI.getString(currentEntity, Keys.spiritStateKey) + "   &fTier: " + tierColor + definition.getTier());
                packet.getChatComponents().write(0, WrappedChatComponent.fromText(actionBarMessage));
                packet.setMeta("SpiritsUnchained", true);
                try {
                    manager.sendServerPacket(player, packet);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
