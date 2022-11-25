package me.justahuman.spiritsunchained.implementation.mobs;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;


import lombok.Getter;

import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.justahuman.spiritsunchained.managers.SpiritEntityManager;
import me.justahuman.spiritsunchained.utils.Keys;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Allay;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import io.github.thebusybiscuit.slimefun4.libraries.commons.lang.Validate;
import io.github.thebusybiscuit.slimefun4.libraries.dough.common.ChatColors;
import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;


@Getter
public abstract class AbstractCustomMob<T extends LivingEntity> {

    private final Class<T> clazz;
    private final String id;
    private final String name;
    private final double maxHealth;
    private SpiritEntityManager spiritEntityManager;

    @ParametersAreNonnullByDefault
    protected AbstractCustomMob(@Nonnull Class<T> clazz, @Nonnull String id, @Nonnull String name, double maxHealth) {
        Validate.isTrue(maxHealth > 0);

        this.clazz = clazz;
        this.id = id;
        this.name = ChatColors.color(name);
        this.maxHealth = maxHealth;
    }

    @Nonnull
    public T spawn(@Nonnull Location loc, @Nonnull World world, String reason, String type) {
        final T mob = world.spawn(loc, this.clazz);
        SpiritsUnchained.getSpiritEntityManager().entityCollection.add(mob);

        PersistentDataAPI.setString(mob, Keys.entityKey, this.id);

        Objects.requireNonNull(mob.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(this.maxHealth);
        mob.setHealth(this.maxHealth);
        mob.setCustomName(this.name);
        mob.setCustomNameVisible(true);
        mob.setRemoveWhenFarAway(true);

        onSpawn(mob);
        return mob;
    }

    public final void register(@Nonnull SpiritEntityManager spiritEntityManager) {
        if (isRegistered()) {
            throw new IllegalStateException("Custom Mob Already Registered!");
        }
        this.spiritEntityManager = spiritEntityManager;
        spiritEntityManager.register(this);
    }

    public final boolean isRegistered() {
        return this.spiritEntityManager != null;
    }

    public final void onEntityTick(@Nonnull LivingEntity mob) {
        onTick(this.clazz.cast(mob));
    }

    public void onSpawn(@Nonnull T spawned) { }

    public void onTick(@Nonnull T mob) { }

    public void onInteract(@Nonnull PlayerInteractEntityEvent e) { }

    public void onHit(@Nonnull EntityDamageByEntityEvent e) { }

    public void onTarget(@Nonnull EntityTargetEvent e) { }

    public void onDeath(@Nonnull EntityDeathEvent e) { }

    public void onDamage(@Nonnull EntityDamageEvent e) { }

    @ParametersAreNonnullByDefault
    public void reveal(Allay currentEntity, Player player) {}
}
