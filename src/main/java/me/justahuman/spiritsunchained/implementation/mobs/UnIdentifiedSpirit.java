package me.justahuman.spiritsunchained.implementation.mobs;

import me.justahuman.spiritsunchained.slimefun.ItemStacks;
import me.justahuman.spiritsunchained.utils.MiscUtils;

import org.bukkit.Particle;
import org.bukkit.entity.Allay;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class UnIdentifiedSpirit extends AbstractCustomMob<Allay> {

    private int particleCount = 5;

    public UnIdentifiedSpirit() {
        super(Allay.class, "UNIDENTIFIED_SPIRIT", "&7Unidentified Spirit", 10);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onSpawn(Allay allay) {
        allay.setCollidable(false);
        allay.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000000*20, 1, true));
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onTick(Allay allay) {
        MiscUtils.spawnParticleRadius(allay.getLocation(), Particle.SPELL_INSTANT, 0.1, particleCount, true);
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
        event.setCancelled(true);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onHit(EntityDamageByEntityEvent event) {

    }

    @Override
    @ParametersAreNonnullByDefault
    public void reveal(Allay allay, Player player) {
        allay.damage(allay.getHealth());
    }
}
