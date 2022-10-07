package me.justahuman.spiritsunchained.implementation.multiblocks;

import io.github.thebusybiscuit.slimefun4.libraries.dough.common.ChatColors;
import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;
import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.justahuman.spiritsunchained.spirits.SpiritDefinition;
import me.justahuman.spiritsunchained.utils.Keys;

import me.justahuman.spiritsunchained.utils.ParticleUtils;
import me.justahuman.spiritsunchained.utils.PlayerUtils;
import me.justahuman.spiritsunchained.utils.SpiritUtils;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
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

                    SpiritDefinition definition = SpiritsUnchained.getSpiritsManager().getSpiritMap().get(EntityType.valueOf(PersistentDataAPI.getString(itemMeta, Keys.spiritItemKey)));
                    String currentState = PersistentDataAPI.getString(itemMeta, Keys.spiritStateKey);
                    double currentProgress = PersistentDataAPI.getDouble(itemMeta, Keys.spiritProgressKey);
                    if (SpiritUtils.updateSpiritItemProgress(spiritItem, Multiplier / (double) definition.getTier())) {
                        PlayerUtils.setKnowledgeLevel(player, definition.getType(), 3);
                    }
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
            if (item != null && item.getType() != Material.AIR && item.hasItemMeta() && SpiritUtils.isSpiritItem(item)) {
                return item;
            }
        }
        return null;
    }
}
