package me.justahuman.spiritsunchained.implementation.mobs;

import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;
import lombok.Getter;
import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.justahuman.spiritsunchained.managers.SpiritEntityManager;
import me.justahuman.spiritsunchained.utils.Keys;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Allay;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;


@Getter
public abstract class AbstractCustomMob<T extends LivingEntity> {

    private final Class<T> clazz;
    private final String id;
    private SpiritEntityManager spiritEntityManager;

    @ParametersAreNonnullByDefault
    protected AbstractCustomMob(@Nonnull Class<T> clazz, @Nonnull String id) {
        this.clazz = clazz;
        this.id = id;
    }

    @Nonnull
    public T spawn(@Nonnull Location loc, @Nonnull World world, String other1, String other2) {
        final T mob = world.spawn(loc, this.clazz);
        SpiritsUnchained.getSpiritEntityManager().entitySet.add(mob.getUniqueId());
        PersistentDataAPI.setString(mob, Keys.entityKey, this.id);
        mob.setCanPickupItems(false);
        mob.setRemoveWhenFarAway(true);

        onSpawn(mob, other1, other2);
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

    public void onSpawn(@Nonnull T spawned, String other1, String other2) { }

    public void onTick(@Nonnull T mob) { }

    public void onInteract(@Nonnull PlayerInteractEntityEvent event) { }

    public void onHit(@Nonnull EntityDamageByEntityEvent event) { }

    public void onTarget(@Nonnull EntityTargetEvent event) { }

    public void onDeath(@Nonnull EntityDeathEvent event) { }

    public void onDamage(@Nonnull EntityDamageEvent event) { }

    @ParametersAreNonnullByDefault
    public void reveal(Allay currentEntity, Player player) {}
}
