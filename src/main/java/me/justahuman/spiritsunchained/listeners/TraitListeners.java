package me.justahuman.spiritsunchained.listeners;

import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;
import io.papermc.paper.event.entity.ElderGuardianAppearanceEvent;
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
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PigZombieAngerEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
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

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerShoot(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        if (new Random().nextInt(1, 101) >= 65 && isUsed(player, EntityType.PILLAGER)) {
            SpiritUtils.spawnProjectile(player, Arrow.class, "Multishoot", 30);
            SpiritUtils.spawnProjectile(player, Arrow.class, "Multishoot", -30);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        //What Fall
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL && isUsed(player, EntityType.SLIME)) {
            event.setDamage(event.getDamage() / 2);
        }
        //Sly Fox
        if (event.getCause() == EntityDamageEvent.DamageCause.CONTACT && isUsed(player, EntityType.FOX)) {
            event.setCancelled(true);
            return;
        }
        //Frosty
        if (event.getCause() == EntityDamageEvent.DamageCause.FREEZE && isUsed(player, EntityType.SNOWMAN)) {
            event.setCancelled(true);
            return;
        }
        //Wither Resistance
        if (event.getCause() == EntityDamageEvent.DamageCause.WITHER && isUsed(player, EntityType.WITHER_SKELETON)) {
            event.setCancelled(true);
            return;
        }
        Entity attacker = event instanceof EntityDamageByEntityEvent otherEvent ? otherEvent.getDamager() : null;
        double finalHealth = player.getHealth() - event.getFinalDamage();
        double finalHealthPercentage = finalHealth / player.getMaxHealth();

        //Explosion Traits
        if (attacker != null && PersistentDataAPI.getString(attacker, Keys.immuneKey).equals(player.getUniqueId().toString())) {
            event.setCancelled(true);
            return;
        }
        //Heavy hit
        if(attacker != null && PersistentDataAPI.getBoolean(attacker, Keys.heavyHitKey)) {
            player.setVelocity(new Vector(0, 15, 0));
            PersistentDataAPI.setBoolean(attacker, Keys.heavyHitKey, false);
        }
        //Poisonous Thorns
        if (attacker instanceof Player attackingPlayer) {
            if (event.getCause() == EntityDamageEvent.DamageCause.THORNS && new Random().nextInt(1,101) >= 75 && isUsed(attackingPlayer, EntityType.PUFFERFISH)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 10*20, 1, true));
            }
        }
        //Hunger Hit
        if (attacker instanceof Player attackingPlayer) {
            if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK && new Random().nextInt(1,101) >= 75 && isUsed(attackingPlayer, EntityType.HUSK)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 30*20, 1, true));
            }
        }
        //Slow Shot
        if (attacker instanceof Arrow arrow && arrow.getShooter() instanceof Player attackingPlayer) {
            if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE && new Random().nextInt(1,101) >= 75 && isUsed(attackingPlayer, EntityType.HUSK)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 30*20, 1, true));
            }
        } else if (attacker instanceof SpectralArrow arrow && arrow.getShooter() instanceof Player attackingPlayer) {
            if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE && new Random().nextInt(1,101) >= 75 && isUsed(attackingPlayer, EntityType.HUSK)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 30*20, 1, true));
            }
        }
        //Natural Thorns
        if (attacker != null && event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK && new Random().nextInt(1,101) >= 50 && isUsed(player, EntityType.GUARDIAN)) {
            EntityDamageByEntityEvent newEvent = new EntityDamageByEntityEvent(player, attacker, EntityDamageEvent.DamageCause.THORNS, new Random().nextInt(1,5));
            newEvent.callEvent();
        }
        //Blazing Thorns
        if (attacker != null && event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK && new Random().nextInt(1,101) >= 75 && isUsed(player, EntityType.GUARDIAN)) {
            attacker.setFireTicks(5*20);
        }
        //Strong Bones
        if(finalHealthPercentage <= 0.5 && !onCooldown(player, Keys.strongBones) && isUsed(player, EntityType.SKELETON)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 45*20, 1, true));
            startCooldown(player, Keys.strongBones, 90);
        }
        //Play Dead
        if(finalHealthPercentage <= 0.25 && !onCooldown(player, Keys.playDead) && isUsed(player, EntityType.AXOLOTL)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 15*20, 1, true));
            startCooldown(player, Keys.playDead, 60);
        }
        //Speedy Escape
        if(finalHealthPercentage <= 0.1 && !onCooldown(player, Keys.speedyEscape) && isUsed(player, EntityType.OCELOT)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 15*20, 5, true));
            startCooldown(player, Keys.speedyEscape, 60);
        }
        //Another Chance
        if(event.getFinalDamage() >= player.getHealth() && isUsed(player, EntityType.EVOKER)) {
            ItemStack offHand = player.getInventory().getItemInOffHand().clone();
            player.getInventory().setItemInOffHand(new ItemStack(Material.TOTEM_OF_UNDYING));
            Bukkit.getScheduler().runTaskLater(instance, () -> {
                player.getInventory().setItemInOffHand(offHand);
            }, 1);
        }
        //Scute Shedding
        if (new Random().nextInt(1, 101) >= 90 && isUsed(player, EntityType.TURTLE)) {
            player.getWorld().dropItemNaturally(player.getLocation(), new ItemStack(Material.SCUTE));
        }
    }
    //Morning Gift
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerSleep(PlayerDeepSleepEvent event) {
        Player player = event.getPlayer();
        if (isUsed(player, EntityType.CAT)) {
            PlayerUtils.addOrDropItem(player, ItemStacks.SU_ECTOPLASM);
        }
    }
    //Group Protection
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAngerPigman(PigZombieAngerEvent event) {
        Entity entity = event.getTarget();
        if (entity instanceof Player player && isUsed(player, EntityType.ZOMBIFIED_PIGLIN)) {
            event.setCancelled(true);
        }
    }
    //No Fatigue
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerGetFatigue(ElderGuardianAppearanceEvent event) {
        if (isUsed(event.getAffectedPlayer(), EntityType.ELDER_GUARDIAN)) {
            event.setCancelled(true);
        }
    }
    //Undead Protection
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
    //Better Foods
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
    //Undead Protection
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
    //Better Shears
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
    //Pig Rancher
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
    //Pig Rancher
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
