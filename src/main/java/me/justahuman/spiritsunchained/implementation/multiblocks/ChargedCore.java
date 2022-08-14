package me.justahuman.spiritsunchained.implementation.multiblocks;

import io.github.thebusybiscuit.slimefun4.libraries.dough.common.ChatColors;
import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;
import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.justahuman.spiritsunchained.spirits.SpiritDefinition;
import me.justahuman.spiritsunchained.utils.Keys;

import me.justahuman.spiritsunchained.utils.ParticleUtils;
import me.justahuman.spiritsunchained.utils.SpiritUtils;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.List;

public class ChargedCore {
    public static void tick(@Nonnull Block b) {
        Location l = b.getLocation();
        Collection<Player> players = b.getWorld().getNearbyEntitiesByType(
                Player.class,
                l,
                2
        );
        if (!players.isEmpty() && isComplete(b)) {
            String ID = BlockStorage.checkID(b);
            String Times = BlockStorage.getLocationInfo(b.getLocation(),"particle");
            double Multiplier = Double.parseDouble(BlockStorage.getLocationInfo(b.getLocation(), "multiplier"));
            particle(Integer.valueOf(Times),l);
            for (Player player : players) {
                ItemStack spiritItem = getSpiritItem(player);
                if (spiritItem != null) {
                    ItemMeta itemMeta = spiritItem.getItemMeta();
                    List<Component> lore = itemMeta.lore();
                    SpiritDefinition definition = SpiritsUnchained.getSpiritsManager().getSpiritMap().get(EntityType.valueOf(PersistentDataAPI.getString(itemMeta, Keys.spiritItemKey)));
                    double currentProgress = PersistentDataAPI.getDouble(itemMeta, Keys.spiritProgressKey);
                    if (currentProgress < 100.0) {
                        double toIncrease = currentProgress+ Multiplier / (double) definition.getTier();
                        toIncrease = new BigDecimal(toIncrease).setScale(2, RoundingMode.HALF_UP).doubleValue();
                        if (toIncrease > 100.0) toIncrease = 100.0;
                        PersistentDataAPI.setDouble(itemMeta, Keys.spiritProgressKey, toIncrease);
                    } else {
                        List<String> states = SpiritUtils.getStates();
                        int indexIncrease = states.indexOf(PersistentDataAPI.getString(itemMeta, Keys.spiritStateKey))+1;
                        if (states.size() >= indexIncrease+1) {
                            String newState = states.get(indexIncrease);
                            PersistentDataAPI.setDouble(itemMeta, Keys.spiritProgressKey, 0);
                            PersistentDataAPI.setString(itemMeta, Keys.spiritStateKey, newState);
                        }
                    }
                    lore.set(2, Component.text(ChatColors.color("&fCurrent State: " + SpiritUtils.stateColor(PersistentDataAPI.getString(itemMeta, Keys.spiritStateKey)) + PersistentDataAPI.getString(itemMeta, Keys.spiritStateKey))));
                    lore.set(5, Component.text(ChatColors.color("&fProgress: " + SpiritUtils.getProgress(PersistentDataAPI.getDouble(itemMeta, Keys.spiritProgressKey)))));

                    itemMeta.lore(lore);
                    spiritItem.setItemMeta(itemMeta);
                }
            }
        }
    }

    private static void particle(Integer times, Location start) {
        ParticleUtils.spawnParticleRadius(start.clone().add(0, 1.5, 0), Particle.END_ROD, 2, times, true, false);
    }

    private static boolean isComplete(Block b) {
        String storage = BlockStorage.getLocationInfo(b.getLocation(),"complete");
        return Boolean.parseBoolean(storage);
    }

    @Nullable
    private static ItemStack getSpiritItem(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (SpiritUtils.isSpiritItem(item)) {
                return item;
            }
        }
        return null;
    }
}
