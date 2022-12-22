package me.justahuman.spiritsunchained.runnables;

import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;
import lombok.Getter;
import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.justahuman.spiritsunchained.spirits.SpiritDefinition;
import me.justahuman.spiritsunchained.utils.Keys;
import me.justahuman.spiritsunchained.utils.ParticleUtils;
import me.justahuman.spiritsunchained.utils.SpiritUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class RelationsAndStateRunnable extends BukkitRunnable {
    @Getter
    static final HashMap<UUID, EnumMap<EntityType, Set<ItemStack>>> spiritCache = new HashMap<>();
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
        final EnumMap<EntityType, Set<ItemStack>> cacheEntry = new EnumMap<>(EntityType.class);
        final Inventory inventory = player.getInventory();
        final ItemStack[] contents = inventory.getContents();
        for (ItemStack item : contents) {
            // Cache all spirits
            if (SpiritUtils.isSpiritItem(item)) {
                final ItemMeta meta = item.getItemMeta();
                final EntityType type = EntityType.valueOf(PersistentDataAPI.getString(meta, Keys.spiritItemKey));
                final Set<ItemStack> spirits = cacheEntry.getOrDefault(type, new HashSet<>());
                spirits.add(item);
                cacheEntry.put(type, spirits);
            }
        }
        for (Map.Entry<EntityType, Set<ItemStack>> entry : cacheEntry.entrySet()) {
            final EntityType type = entry.getKey();
            final Set<ItemStack> spirits = entry.getValue();
            
            for (ItemStack itemStack : new HashSet<>(spirits)) {
                final ItemMeta itemMeta = itemStack.getItemMeta();
                final Location location = player.getLocation();
                final World world = player.getWorld();
                final String state = PersistentDataAPI.getString(itemMeta, Keys.spiritStateKey);
                final SpiritDefinition definition = spiritMap.get(type);
                final Map<String, List<EntityType>> relations = definition.getRelations();
    
                for (EntityType scareType : relations.get("Scare")) {
                    final Set<ItemStack> scared = cacheEntry.getOrDefault(scareType, new HashSet<>());
                    for (ItemStack scare : new HashSet<>(scared)) {
                        scared.remove(scare);
                        cacheEntry.put(scareType, scared);
                        SpiritUtils.updateSpiritItemProgress(scare, (double) - 1 / SpiritUtils.random(1, 5));
                        world.dropItemNaturally(location, scare.clone());
                        inventory.remove(scare);
                        world.playSound(location, Sound.ENTITY_ITEM_PICKUP, 2, 1);
                        ParticleUtils.spawnParticleRadius(location, Particle.REDSTONE, 3, 30, "Colored", new Particle.DustOptions(Color.fromRGB(255,0,0), 1));
                    }
                }
    
                if (SpiritsUnchained.getInstance().getConfig().getBoolean("options.hostile-movement", true) && state.equals("Hostile")) {
                    final int index = SpiritUtils.random(0, contents.length);
                    final ItemStack toMove = inventory.getItem(index);
                    final int moveTo = SpiritUtils.random(0, contents.length);
                    final ItemStack toSwitch = inventory.getItem(moveTo);
                    inventory.setItem(index, toSwitch);
                    inventory.setItem(moveTo, toMove);
                }
    
                if (SpiritsUnchained.getInstance().getConfig().getBoolean("options.aggressive-damage", true) && (state.equals("Hostile") || state.equals("Aggressive"))) {
                    player.damage(SpiritUtils.random(0, 2));
                }
            }
        }
        spiritCache.put(player.getUniqueId(), cacheEntry);
    }
}
