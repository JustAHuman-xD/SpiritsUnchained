package me.justahuman.spiritsunchained.runnables;

import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;
import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.justahuman.spiritsunchained.spirits.SpiritDefinition;
import me.justahuman.spiritsunchained.utils.Keys;
import me.justahuman.spiritsunchained.utils.ParticleUtils;
import me.justahuman.spiritsunchained.utils.SpiritUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class RelationsAndStateRunnable extends BukkitRunnable {
    @Override
    public void run() {
        Map<EntityType, SpiritDefinition> spiritMap = SpiritsUnchained.getSpiritsManager().getSpiritMap();
        for (World world : Bukkit.getWorlds()) {
            for (Player player : world.getPlayers()) {
                checkSpirits(player, spiritMap);
            }
        }
    }

    private void checkSpirits(Player player, Map<EntityType, SpiritDefinition> spiritMap) {
        final Inventory inventory = player.getInventory();
        final ItemStack[] contents = inventory.getContents();
        for (ItemStack item : contents) {
            //If it's not a Spirit I don't want it
            if (item == null || item.getType() != Material.FIREWORK_STAR || ! SpiritUtils.isSpiritItem(item)) {
                continue;
            }
            final Location location = player.getLocation();
            final World world = player.getWorld();
            final ItemMeta meta = item.getItemMeta();
            final String state = PersistentDataAPI.getString(meta, Keys.spiritStateKey);
            final SpiritDefinition definition = spiritMap.get(EntityType.valueOf(PersistentDataAPI.getString(meta, Keys.spiritItemKey)));
            final Map<String, List<EntityType>> relations = definition.getRelations();

            for (EntityType type : relations.get("Scare")) {
                final ItemStack scared = SpiritUtils.getSpiritItem(player, type);

                if (scared == null) {
                    continue;
                }

                SpiritUtils.updateSpiritItemProgress(scared, (double) 1 / new Random().nextInt(1, 5));
                world.dropItemNaturally(location, scared.clone());
                inventory.remove(scared);
                world.playSound(location, Sound.ENTITY_ITEM_PICKUP, 2, 1);
                ParticleUtils.spawnParticleRadius(location, Particle.REDSTONE, 3, 30, "Colored", new Particle.DustOptions(Color.fromRGB(255,0,0), 1));
            }

            if (SpiritsUnchained.getInstance().getConfig().getBoolean("hostile-movement", true) && state.equals("Hostile")) {
                final int index = new Random().nextInt(contents.length);
                final ItemStack toMove = inventory.getItem(index) != null ? inventory.getItem(index).clone() : null;
                final int moveTo = new Random().nextInt(contents.length);
                final ItemStack toSwitch = inventory.getItem(moveTo) != null ? inventory.getItem(moveTo).clone() : null;
                inventory.setItem(index, toSwitch);
                inventory.setItem(moveTo, toMove);
            }

            if (SpiritsUnchained.getInstance().getConfig().getBoolean("aggressive-damage", true) && (state.equals("Hostile") || state.equals("Aggressive"))) {
                player.damage(new Random().nextInt(2));
            }
        }
    }
}
