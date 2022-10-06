package me.justahuman.spiritsunchained.implementation.mobs;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;

import lombok.Getter;

import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.justahuman.spiritsunchained.implementation.tools.SpiritBook;
import me.justahuman.spiritsunchained.implementation.tools.SpiritNet;
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
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Spirit extends AbstractCustomMob<Allay> {

    @Getter
    private int particleCount = 4;
    @Getter
    private final SpiritDefinition definition;

    public Spirit(String id, String name, EntityType soulType) {
        super(Allay.class, id, name, 10);
        this.definition = SpiritsUnchained.getSpiritsManager().getSpiritMap().get(soulType);
    }

    @Nonnull
    public final Allay spawn(@Nonnull Location loc, @Nonnull World world, String reason, String revealState) {
        double health = this.getMaxHealth()*definition.getTier();
        String state;
        if (reason.equals("Natural")) {
            state = definition.getStates().get(new Random().nextInt(definition.getStates().size()));
        } else if (reason.equals("Reveal")) {
            state = revealState;
        } else {
            state = reason;
        }

        Allay mob = world.spawn(loc, this.getClazz());
        SpiritUtils.SpiritIdMap.put(mob.getEntityId(), mob);
        PersistentDataAPI.setString(mob, Keys.entityKey, this.getId());
        PersistentDataAPI.setString(mob, Keys.spiritStateKey, state);

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

        allay.setCollidable(false);
        allay.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000000 * 20, 1, true, false));
        allay.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1000000 * 20, 1, true, false));
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onTick(Allay allay) {
        String state = PersistentDataAPI.getString(allay, Keys.spiritStateKey);
        ParticleUtils.spawnParticleRadius(allay.getLocation(), Particle.SPELL_INSTANT, 0.1, particleCount, true, true);
        SpiritUtils.spawnStateParticle(state, allay.getLocation());

        for (Player player : allay.getWorld().getPlayers()) {
            if (player.canSee(allay)) {
                player.hideEntity(SpiritsUnchained.getInstance(), allay);
            }
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onDeath(EntityDeathEvent event) {
        ItemStack todrop = ItemStacks.SU_ECTOPLASM.clone();
        todrop.setAmount(definition.getTier() + 1);
        event.getDrops().add(todrop);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        EquipmentSlot hand = event.getHand();
        Entity entity = event.getRightClicked();
        ItemStack item = player.getInventory().getItem(hand);
        if (item.getType() == Material.AIR) {return;}
        if (SlimefunItem.getByItem(item) instanceof SpiritNet) {
            if (new Random().nextInt(1,100) <= SpiritUtils.getTierChance(this.definition.getTier())) {
                ParticleUtils.catchAnimation(entity.getLocation());
                entity.remove();
                item.setAmount(item.getAmount() - 1);
                player.getInventory().addItem(SpiritUtils.SpiritItem(PersistentDataAPI.getString(entity, Keys.spiritStateKey), this.definition));
            } else {
                player.sendMessage("The Spirit Escaped the Net!");
                item.setAmount(item.getAmount() - 1);
            }
        } else if (SlimefunItem.getByItem(item) instanceof SpiritBook) {
            player.sendMessage("Book PlaceHolder");
            item.setAmount(item.getAmount() - 1);
        } else if (item.getType() == Material.GLASS_BOTTLE && item.getItemMeta().getPersistentDataContainer().isEmpty()) {
            ParticleUtils.bottleAnimation(entity.getLocation());
            entity.remove();
            item.setAmount(item.getAmount() - 1);
            player.getInventory().addItem(ItemStacks.SU_SPIRIT_BOTTLE);
        }
    }
}
