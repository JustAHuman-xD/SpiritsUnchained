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
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Allay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.Random;

public class UnIdentifiedSpirit extends AbstractCustomMob<Allay> {

    public UnIdentifiedSpirit() {
        super(Allay.class, "UNIDENTIFIED_SPIRIT", "&7Unidentified Spirit", 10);
    }

    @Nonnull
    @Override
    public Allay spawn(@Nonnull Location loc, @Nonnull World world, String reason, String type) {
        final Allay mob = world.spawn(loc, this.getClazz());
        SpiritUtils.spiritIdMap.put(mob.getEntityId(), mob);
        SpiritsUnchained.getSpiritEntityManager().entityCollection.add(mob);
        final SpiritDefinition definition = SpiritsUnchained.getSpiritsManager().getSpiritMap().get(EntityType.valueOf(type));
        final String state;

        if (reason.equals("Natural")) {
            state = definition.getStates().get(new Random().nextInt(definition.getStates().size()));
        } else {
            state = reason;
        }

        if (type == null) {
            type = "COW_SPIRIT";
        } else {
            type = type + "_SPIRIT";
        }

        PersistentDataAPI.setString(mob, Keys.entityKey, this.getId());
        PersistentDataAPI.setString(mob, Keys.spiritStateKey, state);
        PersistentDataAPI.setString(mob, Keys.spiritTypeKey, type);
        PersistentDataAPI.setBoolean(mob, Keys.spiritIdentified, false);
        PersistentDataAPI.setLong(mob, Keys.despawnKey, System.currentTimeMillis() + SpiritUtils.random((long) (definition.getTier() * 60L * 0.75), definition.getTier() * 60L * SpiritUtils.random(1, 3)) * 1000);
    
        Objects.requireNonNull(mob.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(this.getMaxHealth());
        mob.setHealth(this.getMaxHealth());
        mob.setRemoveWhenFarAway(true);
        mob.setCanPickupItems(false);

        onSpawn(mob);
        return mob;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onSpawn(Allay allay) {
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
            getSpiritEntityManager().entityCollection.remove(allay);
            allay.remove();
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onDeath(EntityDeathEvent event) {
        final Allay allay = (Allay) event.getEntity();
        if (allay.getKiller() != null) {
            event.getDrops().add(ItemStacks.SU_ECTOPLASM);
            return;
        }
        event.setShouldPlayDeathSound(false);
        getSpiritEntityManager().entityCollection.remove(allay);
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
            getSpiritEntityManager().entityCollection.remove((LivingEntity) entity);
            ParticleUtils.catchAnimation(entity.getLocation());
            entity.remove();
            item.setAmount(item.getAmount() - 1);
            player.getInventory().addItem(ItemStacks.SU_UNIDENTIFIED_SPIRIT);
        } else if (item.getType() == Material.GLASS_BOTTLE && item.getItemMeta().getPersistentDataContainer().isEmpty()) {
            getSpiritEntityManager().entityCollection.remove((LivingEntity) entity);
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
        SpiritsUnchained.getSpiritEntityManager().getCustomClass(null, PersistentDataAPI.getString(allay, Keys.spiritTypeKey)).spawn(allay.getLocation(), allay.getWorld(), "Reveal", PersistentDataAPI.getString(allay, Keys.spiritStateKey));
        getSpiritEntityManager().entityCollection.remove(allay);
        allay.remove();
    }
}
