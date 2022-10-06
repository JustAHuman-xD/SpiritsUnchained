package me.justahuman.spiritsunchained.implementation.mobs;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;

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

    private int particleCount = 5;

    public UnIdentifiedSpirit() {
        super(Allay.class, "UNIDENTIFIED_SPIRIT", "&7Unidentified Spirit", 10);
    }

    @Nonnull
    @Override
    public Allay spawn(@Nonnull Location loc, @Nonnull World world, String reason, String type) {
        Allay mob = world.spawn(loc, this.getClazz());
        SpiritUtils.SpiritIdMap.put(mob.getEntityId(), mob);
        SpiritDefinition definition = SpiritsUnchained.getSpiritsManager().getSpiritMap().get(EntityType.valueOf(type));
        String state;

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
        allay.setCollidable(false);
        allay.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1000000 * 20, 1, true, false));
        allay.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000000*20, 1, true, false));
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onTick(Allay allay) {
        ParticleUtils.spawnParticleRadius(allay.getLocation(), Particle.SPELL_INSTANT, 0.1, particleCount, true, true);

        for (Player player : allay.getWorld().getPlayers()) {
            if (player.canSee(allay)) {
                player.hideEntity(SpiritsUnchained.getInstance(), allay);
            }
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onDeath(EntityDeathEvent event) {
        Allay allay = (Allay) event.getEntity();
        if (allay.getKiller() != null) {
            event.getDrops().add(ItemStacks.SU_ECTOPLASM);
            return;
        }
        event.setShouldPlayDeathSound(false);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        EquipmentSlot hand = event.getHand();
        ItemStack item = player.getInventory().getItem(hand);

        if (item.getType() == Material.AIR) {return;}

        if (SlimefunItem.getByItem(item) instanceof SpiritNet) {
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

    }

    @Override
    @ParametersAreNonnullByDefault
    public void reveal(Allay allay, Player player) {
        SpiritsUnchained.getSpiritEntityManager().getCustomClass(null, PersistentDataAPI.getString(allay, Keys.spiritTypeKey)).spawn(allay.getLocation(), allay.getWorld(), "Reveal", PersistentDataAPI.getString(allay, Keys.spiritStateKey));
        allay.remove();
    }
}
