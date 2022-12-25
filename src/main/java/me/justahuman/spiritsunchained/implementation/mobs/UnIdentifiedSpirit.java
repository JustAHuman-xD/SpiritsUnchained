package me.justahuman.spiritsunchained.implementation.mobs;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;
import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.justahuman.spiritsunchained.slimefun.ItemStacks;
import me.justahuman.spiritsunchained.spirits.SpiritDefinition;
import me.justahuman.spiritsunchained.utils.Keys;
import me.justahuman.spiritsunchained.utils.ParticleUtils;
import me.justahuman.spiritsunchained.utils.SpiritUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Allay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.ParametersAreNonnullByDefault;

public class UnIdentifiedSpirit extends AbstractCustomMob<Allay> {

    public UnIdentifiedSpirit() {
        super(Allay.class, "UNIDENTIFIED_SPIRIT");
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onSpawn(Allay allay, String reason, String type) {
        final SpiritDefinition definition = SpiritsUnchained.getSpiritsManager().getSpiritMap().get(EntityType.valueOf(type));
        final String state;
    
        if (reason.equals("Natural")) {
            state = definition.getStates().get(SpiritUtils.random(0, definition.getStates().size()));
        } else {
            state = reason;
        }
        type += "_SPIRIT";
    
        PersistentDataAPI.setString(allay, Keys.spiritStateKey, state);
        PersistentDataAPI.setString(allay, Keys.spiritTypeKey, type);
        PersistentDataAPI.setBoolean(allay, Keys.spiritIdentified, false);
        PersistentDataAPI.setLong(allay, Keys.despawnKey, System.currentTimeMillis() + SpiritUtils.random((int) (definition.getTier() * 60L * 0.75), definition.getTier() * 60 * SpiritUtils.random(1, 3)) * 1000L);
        
        for (Player player : allay.getWorld().getPlayers()) {
            if (player.canSee(allay)) {
                player.hideEntity(SpiritsUnchained.getInstance(), allay);
            }
        }
        
        allay.setCollidable(false);
        allay.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1000000 * 20, 1, true, false));
        allay.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000000*20, 1, true, false));
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onTick(Allay allay) {
        final Location location = allay.getLocation();
        ParticleUtils.spawnParticleRadius(location, Particle.SPELL_INSTANT, 0.1, 5, "Spirit");
        if (PersistentDataAPI.hasLong(allay, Keys.despawnKey) && System.currentTimeMillis() >= PersistentDataAPI.getLong(allay, Keys.despawnKey)) {
            ParticleUtils.passOnAnimation(location);
            allay.remove();
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onInteract(PlayerInteractEntityEvent event) {
        final Player player = event.getPlayer();
        final Entity entity = event.getRightClicked();
        final EquipmentSlot hand = event.getHand();
        final ItemStack item = player.getInventory().getItem(hand);

        if (item.getType() == Material.AIR) {
            return;
        }

        SlimefunItem slimefunItem = SlimefunItem.getByItem(item);
        if (slimefunItem != null && slimefunItem.getId().equals(ItemStacks.SU_SPIRIT_NET.getItemId())) {
            ParticleUtils.catchAnimation(entity.getLocation());
            entity.remove();
            item.setAmount(item.getAmount() - 1);
            player.getInventory().addItem(ItemStacks.SU_UNIDENTIFIED_SPIRIT);
        } else if (item.getType() == Material.GLASS_BOTTLE && item.getItemMeta().getPersistentDataContainer().isEmpty()) {
            ParticleUtils.bottleAnimation(entity.getLocation());
            entity.remove();
            item.setAmount(item.getAmount() - 1);
            player.getInventory().addItem(ItemStacks.SU_SPIRIT_BOTTLE);
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onHit(EntityDamageByEntityEvent event) {
        event.setCancelled(true);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void reveal(Allay allay, Player player) {
        SpiritsUnchained.getSpiritEntityManager().getCustomClass(null, PersistentDataAPI.getString(allay, Keys.spiritTypeKey)).spawn(allay.getLocation(), allay.getWorld(), PersistentDataAPI.getString(allay, Keys.spiritStateKey), null);
        allay.remove();
    }
}
