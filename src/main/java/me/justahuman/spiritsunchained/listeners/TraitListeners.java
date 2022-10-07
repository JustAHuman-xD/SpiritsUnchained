package me.justahuman.spiritsunchained.listeners;

import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;
import io.papermc.paper.event.player.PlayerDeepSleepEvent;
import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.justahuman.spiritsunchained.slimefun.ItemStacks;
import me.justahuman.spiritsunchained.utils.Keys;
import me.justahuman.spiritsunchained.utils.PlayerUtils;
import me.justahuman.spiritsunchained.utils.SpiritUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.BatToggleSleepEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.LingeringPotionSplashEvent;
import org.bukkit.event.entity.PigZombieAngerEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Vector;
import org.spigotmc.event.entity.EntityDismountEvent;
import org.spigotmc.event.entity.EntityMountEvent;

import java.util.List;
import java.util.Random;

public class TraitListeners implements Listener {
    SpiritsUnchained instance = SpiritsUnchained.getInstance();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        if (!PersistentDataAPI.hasString(projectile, Keys.entityKey)) {
            return;
        }

        switch (PersistentDataAPI.getString(projectile, Keys.entityKey)) {
            default -> {}
            case "Eggpult" -> {
                TNTPrimed tnt = (TNTPrimed) projectile.getWorld().spawnEntity(projectile.getLocation(), EntityType.PRIMED_TNT);
                tnt.setFuseTicks(1);
                PersistentDataAPI.setString(tnt, Keys.entityKey, "DullExplosion");
                PersistentDataAPI.setString(tnt, Keys.immuneKey, PersistentDataAPI.getString(projectile, Keys.ownerKey));
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onTntExplode(EntityExplodeEvent event) {
        Entity exploding = event.getEntity();
        if (!PersistentDataAPI.hasString(exploding, Keys.entityKey)) {
            return;
        }

        if (PersistentDataAPI.getString(exploding, Keys.entityKey).equals("DullExplosion")) {
            event.blockList().clear();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (event.getCause() == EntityDamageEvent.DamageCause.FALL && isUsed(player, EntityType.SLIME)) {
            event.setDamage(event.getDamage() / 2);
        }

        if (event.getCause() == EntityDamageEvent.DamageCause.CONTACT && isUsed(player, EntityType.FOX)) {
            event.setCancelled(true);
            return;
        }

        if (event.getCause() == EntityDamageEvent.DamageCause.FREEZE && isUsed(player, EntityType.SNOWMAN)) {
            event.setCancelled(true);
            return;
        }
        Entity attacker = event.getDamager();
        double finalHealth = player.getHealth() - event.getFinalDamage();
        double finalHealthPercentage = finalHealth / player.getMaxHealth();

        if (PersistentDataAPI.getString(attacker, Keys.immuneKey).equals(player.getUniqueId().toString())) {
            event.setCancelled(true);
            return;
        }
        if(PersistentDataAPI.getBoolean(attacker, Keys.heavyHitKey)) {
            player.setVelocity(new Vector(0, 15, 0));
            PersistentDataAPI.setBoolean(attacker, Keys.heavyHitKey, false);
        }
        if (attacker instanceof Player attackingPlayer) {
            if (event.getCause() == EntityDamageEvent.DamageCause.THORNS && new Random().nextInt(1,101) >= 75 && isUsed(attackingPlayer, EntityType.PUFFERFISH)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 10*20, 1, true));
            }
        }
        if(finalHealthPercentage <= 0.5 && !onCooldown(player, Keys.strongBones) && isUsed(player, EntityType.SKELETON)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 45*20, 1, true));
            startCooldown(player, Keys.strongBones, 90);
        }
        if(finalHealthPercentage <= 0.25 && !onCooldown(player, Keys.playDead) && isUsed(player, EntityType.AXOLOTL)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 15*20, 1, true));
            startCooldown(player, Keys.strongBones, 60);
        }
        if(finalHealthPercentage <= 0.1 && !onCooldown(player, Keys.speedyEscape) && isUsed(player, EntityType.OCELOT)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 15*20, 5, true));
            startCooldown(player, Keys.speedyEscape, 60);
        }

        if (new Random().nextInt(1, 101) >= 90 && isUsed(player, EntityType.TURTLE)) {
            player.getWorld().dropItemNaturally(player.getLocation(), new ItemStack(Material.SCUTE));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerSleep(PlayerDeepSleepEvent event) {
        Player player = event.getPlayer();
        if (isUsed(player, EntityType.CAT)) {
            PlayerUtils.addOrDropItem(player, ItemStacks.SU_ECTOPLASM);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAngerPigman(PigZombieAngerEvent event) {
        Entity entity = event.getTarget();
        if (entity instanceof Player player && isUsed(player, EntityType.ZOMBIFIED_PIGLIN)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onConsumeItem(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (!isUsed(player, EntityType.ZOMBIE, EntityType.ZOMBIE_VILLAGER)) {
            return;
        }

        if (!(item.getItemMeta() instanceof PotionMeta potionMeta)) {
            return;
        }

        PotionData base = potionMeta.getBasePotionData();
        for (PotionEffect effect : potionMeta.getCustomEffects()) {
            if (effect.getType() == PotionEffectType.HARM) {
                effect = new PotionEffect(PotionEffectType.HEAL, effect.getDuration(), effect.getAmplifier(), effect.isAmbient());
                potionMeta.removeCustomEffect(PotionEffectType.HARM);
                potionMeta.addCustomEffect(effect, true);
            }
        }
        if (base.getType() == PotionType.INSTANT_DAMAGE) {
            base = new PotionData(PotionType.INSTANT_HEAL, base.isExtended(), base.isUpgraded());
        }
        potionMeta.setBasePotionData(base);
        item.setItemMeta(potionMeta);
        event.setItem(item);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHungerChange(FoodLevelChangeEvent event) {
        HumanEntity entity = event.getEntity();
        if (!(entity instanceof Player player)) {
            return;
        }

        if (new Random().nextInt(1,101) >= 70 && isUsed(player, EntityType.WOLF)) {
            event.setFoodLevel(event.getFoodLevel()*2);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSplashPotion(PotionSplashEvent event) {

        ThrownPotion potion = event.getPotion();
        for (LivingEntity entity : event.getAffectedEntities()) {
            if (! (entity instanceof Player player)) {
                return;
            }
            if (! isUsed(player, EntityType.ZOMBIE, EntityType.ZOMBIE_VILLAGER)) {
                return;
            }
            PotionMeta potionMeta = potion.getPotionMeta();
            PotionData base = potionMeta.getBasePotionData();
            for (PotionEffect effect : potionMeta.getCustomEffects()) {
                if (effect.getType() == PotionEffectType.HARM) {
                    effect = new PotionEffect(PotionEffectType.HEAL, effect.getDuration(), effect.getAmplifier(), effect.isAmbient());
                    potionMeta.removeCustomEffect(PotionEffectType.HARM);
                    potionMeta.addCustomEffect(effect, true);
                }
            }
            if (base.getType() == PotionType.INSTANT_DAMAGE) {
                base = new PotionData(PotionType.INSTANT_HEAL, base.isExtended(), base.isUpgraded());
            }
            potionMeta.setBasePotionData(base);
            potion.setPotionMeta(potionMeta);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerShear(PlayerShearEntityEvent event) {
        Entity entity = event.getEntity();
        Player player = event.getPlayer();
        if (entity instanceof Sheep) {
            if (! isUsed(player, EntityType.SHEEP)) {
                return;
            }
            if (new Random().nextInt(1,101) >= 25) {
                event.getItem().setAmount(event.getItem().getAmount()*2);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerEnter(EntityMountEvent event) {
        Entity rider = event.getEntity();
        Entity mount = event.getMount();
        if (!(rider instanceof Player player)) {
            return;
        }

        if (mount instanceof Pig pig) {
            if (! isUsed(player, EntityType.PIG)) {
                return;
            }
            pig.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 10, true));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLeave(EntityDismountEvent event) {
        Entity rider = event.getEntity();
        Entity dismount = event.getDismounted();
        if (!(rider instanceof Player player)) {
            return;
        }

        if (dismount instanceof Pig pig) {
            if (! isUsed(player, EntityType.PIG)) {
                return;
            }
            if (pig.hasPotionEffect(PotionEffectType.SPEED) && pig.getPotionEffect(PotionEffectType.SPEED).getAmplifier() == 10) {
                pig.removePotionEffect(PotionEffectType.SPEED);
            }
        }
    }
    private boolean isUsed(Player player, EntityType... types) {
        for (EntityType type : types) {
            if (SpiritUtils.useSpiritItem(player, type)) {
                return true;
            }
        }
        return false;
    }
    private boolean onCooldown(Player player, NamespacedKey key) {
        return PersistentDataAPI.getBoolean(player, key);
    }
    private void startCooldown(Player player, NamespacedKey key, int when) {
        PersistentDataAPI.setBoolean(player, key, true);
        Bukkit.getScheduler().runTaskLater(instance, () -> {
            PersistentDataAPI.setBoolean(player, key, false);
        }, when* 20L);
    }
}
