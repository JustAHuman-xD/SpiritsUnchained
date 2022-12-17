package me.justahuman.spiritsunchained.implementation.mobs;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;

import lombok.Getter;

import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.justahuman.spiritsunchained.slimefun.ItemStacks;
import me.justahuman.spiritsunchained.spirits.SpiritDefinition;
import me.justahuman.spiritsunchained.utils.Keys;
import me.justahuman.spiritsunchained.utils.ParticleUtils;
import me.justahuman.spiritsunchained.utils.PlayerUtils;
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

public class Spirit extends AbstractCustomMob<Allay> {

    @Getter
    private static final int particleCount = 4;
    @Getter
    private final SpiritDefinition definition;

    public Spirit(String id, String name, EntityType soulType) {
        super(Allay.class, id, name, 10);
        this.definition = SpiritsUnchained.getSpiritsManager().getSpiritMap().get(soulType);
    }

    @Override
    @Nonnull
    public final Allay spawn(@Nonnull Location loc, @Nonnull World world, String reason, String revealState) {
        final double health = this.getMaxHealth()*definition.getTier();
        final String state;
        if (reason.equals("Natural")) {
            state = definition.getStates().get(new Random().nextInt(definition.getStates().size()));
        } else if (reason.equals("Reveal")) {
            state = revealState;
        } else {
            state = reason;
        }

        final Allay mob = world.spawn(loc, this.getClazz());
        SpiritsUnchained.getSpiritEntityManager().entityCollection.add(mob);
        SpiritUtils.spiritIdMap.put(mob.getEntityId(), mob);
        PersistentDataAPI.setString(mob, Keys.entityKey, this.getId());
        PersistentDataAPI.setString(mob, Keys.spiritStateKey, state);
        PersistentDataAPI.setLong(mob, Keys.despawnKey, System.currentTimeMillis() + SpiritUtils.random((long) (definition.getTier() * 60L * 0.75), definition.getTier() * 60 * SpiritUtils.random(1, 3)) * 1000);

        Objects.requireNonNull(mob.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(health);
        mob.setHealth(health);
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
        allay.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000000 * 20, 1, true, false));
        allay.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1000000 * 20, 1, true, false));
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onTick(Allay allay) {
        final Location location = allay.getLocation();
        final String state = PersistentDataAPI.getString(allay, Keys.spiritStateKey);
        ParticleUtils.spawnParticleRadius(location, Particle.SPELL_INSTANT, 0.1, particleCount, "Spirit");
        SpiritUtils.spawnStateParticle(state, location);
        
        if (PersistentDataAPI.hasLong(allay, Keys.despawnKey) && System.currentTimeMillis() >= PersistentDataAPI.getLong(allay, Keys.despawnKey)) {
            ParticleUtils.passOnAnimation(location);
            getSpiritEntityManager().entityCollection.remove(allay);
            allay.remove();
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onDeath(EntityDeathEvent event) {
        final ItemStack toDrop = ItemStacks.SU_ECTOPLASM.clone();
        toDrop.setAmount(definition.getTier() + 1);
        event.getDrops().add(toDrop);
        getSpiritEntityManager().entityCollection.remove(event.getEntity());
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onHit(EntityDamageByEntityEvent event) {
        event.setCancelled(true);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onInteract(PlayerInteractEntityEvent event) {
        final Player player = event.getPlayer();
        final EquipmentSlot hand = event.getHand();
        final Entity entity = event.getRightClicked();
        final EntityType type = this.definition.getType();
        final ItemStack item = player.getInventory().getItem(hand);
        final int tier = this.definition.getTier();

        if (item.getType() == Material.AIR) {
            return;
        }
        SlimefunItem slimefunItem = SlimefunItem.getByItem(item);
        if (slimefunItem != null && slimefunItem.getId().equals(ItemStacks.SU_SPIRIT_NET.getItemId())) {
            if (new Random().nextInt(1,100) <= SpiritUtils.getTierChance(tier)) {
                ParticleUtils.catchAnimation(entity.getLocation());
                getSpiritEntityManager().entityCollection.remove((LivingEntity) entity);
                entity.remove();
                PlayerUtils.addOrDropItem(player, SpiritUtils.spiritItem(PersistentDataAPI.getString(entity, Keys.spiritStateKey), this.definition));
                PlayerUtils.learnKnowledgePiece(player, type, 1);
            } else {
                player.sendMessage(SpiritUtils.getTranslation("messages.spirits.escape"));
            }
            item.subtract();
        } else if(slimefunItem != null && slimefunItem.getId().equals(ItemStacks.SU_SPIRIT_BOOK.getItemId())) {
            if (new Random().nextInt(1, 100) <= SpiritUtils.getTierChance(tier)) {
                if (!PlayerUtils.hasKnowledgePiece(player, type, 2)) {
                    PlayerUtils.addOrDropItem(player, SpiritUtils.getFilledBook(this.definition));
                    PlayerUtils.learnKnowledgePiece(player, type, 2);
                } else {
                    player.sendMessage(SpiritUtils.getTranslation("messages.spirits.max_knowledge"));
                    return;
                }
            } else {
                player.sendMessage(SpiritUtils.getTranslation("messages.spirits.rip_book"));
            }
            item.subtract();
        } else if (item.getType() == Material.GLASS_BOTTLE && item.getItemMeta().getPersistentDataContainer().isEmpty()) {
            ParticleUtils.bottleAnimation(entity.getLocation());
            getSpiritEntityManager().entityCollection.remove((LivingEntity) entity);
            entity.remove();
            item.subtract();
            PlayerUtils.addOrDropItem(player, ItemStacks.SU_SPIRIT_BOTTLE);
        }
    }
}
