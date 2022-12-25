package me.justahuman.spiritsunchained.listeners;

import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;
import io.papermc.paper.event.entity.ElderGuardianAppearanceEvent;
import io.papermc.paper.event.player.PlayerDeepSleepEvent;
import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.justahuman.spiritsunchained.slimefun.ItemStacks;
import me.justahuman.spiritsunchained.utils.Keys;
import me.justahuman.spiritsunchained.utils.PlayerUtils;
import me.justahuman.spiritsunchained.utils.SpiritUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Sheep;
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

public class TraitListeners implements Listener {
    SpiritsUnchained instance = SpiritsUnchained.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onProjectileHit(ProjectileHitEvent event) {
        final Projectile projectile = event.getEntity();
        final String key = PersistentDataAPI.hasString(projectile, Keys.entityKey) ? PersistentDataAPI.getString(projectile, Keys.entityKey) : "";

        if (key.equalsIgnoreCase("Eggpult")) {
            final TNTPrimed tnt = (TNTPrimed) projectile.getWorld().spawnEntity(projectile.getLocation(), EntityType.PRIMED_TNT);
            tnt.setFuseTicks(1);
            PersistentDataAPI.setString(tnt, Keys.entityKey, "DullExplosion");
            PersistentDataAPI.setString(tnt, Keys.immuneKey, PersistentDataAPI.getString(projectile, Keys.ownerKey));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onTntExplode(EntityExplodeEvent event) {
        final Entity exploding = event.getEntity();
        if (!PersistentDataAPI.hasString(exploding, Keys.entityKey)) {
            return;
        }

        if (PersistentDataAPI.getString(exploding, Keys.entityKey).equals("DullExplosion") || PersistentDataAPI.getString(exploding, Keys.entityKey).equals("Skull_Fire")) {
            event.blockList().clear();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerShoot(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player player && event.getProjectile() instanceof Arrow arrow1)) {
            return;
        }
        if (SpiritUtils.chance(35) && isUsed(player, EntityType.PILLAGER)) {
            final Arrow arrow2 = (Arrow) SpiritUtils.spawnProjectile(player, Arrow.class, "Multishoot");
            final Arrow arrow3 = (Arrow) SpiritUtils.spawnProjectile(player, Arrow.class, "Multishoot");
            arrow2.setPickupStatus(AbstractArrow.PickupStatus.CREATIVE_ONLY);
            arrow3.setPickupStatus(AbstractArrow.PickupStatus.CREATIVE_ONLY);
            arrow2.setShooter(player);
            arrow3.setShooter(player);
            arrow2.setColor(arrow1.getColor());
            arrow3.setColor(arrow1.getColor());
            arrow2.setVelocity(arrow1.getVelocity().rotateAroundY(Math.toRadians(10)));
            arrow3.setVelocity(arrow1.getVelocity().rotateAroundY(Math.toRadians(-10)));
            arrow2.setDamage(arrow1.getDamage());
            arrow3.setDamage(arrow1.getDamage());
            arrow2.setCritical(arrow1.isCritical());
            arrow3.setCritical(arrow1.isCritical());
            for (PotionEffect effect : arrow1.getCustomEffects()) {
                arrow2.addCustomEffect(effect, true);
                arrow3.addCustomEffect(effect, true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof LivingEntity entity)) {
            return;
        }
        final Player player = entity instanceof Player maybe ? maybe : null;
        final EntityDamageEvent.DamageCause cause = event.getCause();
        //What Fall
        if (cause == EntityDamageEvent.DamageCause.FALL && isUsed(player, EntityType.SLIME)) {
            event.setDamage(event.getDamage() / 2);
        }
        //Sly Fox
        if (cause == EntityDamageEvent.DamageCause.CONTACT && isUsed(player, EntityType.FOX)) {
            event.setDamage(0);
            return;
        }
        //Frosty
        if (cause == EntityDamageEvent.DamageCause.FREEZE && isUsed(player, EntityType.SNOWMAN)) {
            event.setCancelled(true);
            return;
        }
        //Wither Resistance
        if (cause == EntityDamageEvent.DamageCause.WITHER && isUsed(player, EntityType.WITHER_SKELETON)) {
            event.setCancelled(true);
            return;
        }

        final Entity attacker = event instanceof EntityDamageByEntityEvent otherEvent ? otherEvent.getDamager() : null;
        final AttributeInstance attributeInstance = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        final double finalHealth = entity.getHealth() - event.getFinalDamage();
        final double maxHealth = attributeInstance != null ? attributeInstance.getValue() : 1;
        final double finalHealthPercentage = finalHealth / maxHealth;

        //Explosion Traits
        if (attacker != null && PersistentDataAPI.hasString(attacker, Keys.immuneKey) && PersistentDataAPI.getString(attacker, Keys.immuneKey).equals(entity.getUniqueId().toString())) {
            event.setCancelled(true);
            return;
        }
        //Heavy hit
        if(attacker != null && PersistentDataAPI.hasBoolean(attacker, Keys.heavyHitKey) && PersistentDataAPI.getBoolean(attacker, Keys.heavyHitKey)) {
            Bukkit.getScheduler().runTaskLater(instance, () -> entity.setVelocity(new Vector(0, 1, 0)), 2);
            PersistentDataAPI.setBoolean(attacker, Keys.heavyHitKey, false);
        }
        //Undead Protection
        if (attacker instanceof ThrownPotion && isUsed(player, EntityType.ZOMBIE, EntityType.ZOMBIE_VILLAGER)) {
            event.setCancelled(true);
            player.setHealth(Math.min(player.getHealth() + event.getFinalDamage(), maxHealth));
            player.sendHealthUpdate();
        }
        //Poisonous Thorns
        if (attacker instanceof Player attackingPlayer && event.getCause() == EntityDamageEvent.DamageCause.THORNS && SpiritUtils.chance(25) && isUsed(attackingPlayer, EntityType.PUFFERFISH)) {
            entity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 10*20, 1, true));
        }
        //Hunger Hit
        if (attacker instanceof Player attackingPlayer && event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK && SpiritUtils.chance(25) && isUsed(attackingPlayer, EntityType.HUSK)) {
            entity.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 30*20, 1, true));
        }
        //Slow Shot
        if (attacker instanceof AbstractArrow arrow && arrow.getShooter() instanceof Player attackingPlayer && event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE && SpiritUtils.chance(25) && isUsed(attackingPlayer, EntityType.STRAY)) {
            entity.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 30*20, 1, true));
        }
        //Natural Thorns
        if (attacker != null && event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK && SpiritUtils.chance(50) && isUsed(player, EntityType.GUARDIAN)) {
            final EntityDamageByEntityEvent newEvent = new EntityDamageByEntityEvent(entity, attacker, EntityDamageEvent.DamageCause.THORNS, SpiritUtils.random(1,5));
            newEvent.callEvent();
        }
        //Blazing Thorns
        if (attacker != null && event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK && SpiritUtils.chance(25) && isUsed(player, EntityType.BLAZE)) {
            attacker.setFireTicks(5*20);
        }
        //Poisonous Thorns
        if (attacker instanceof LivingEntity livingEntity && event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK && SpiritUtils.chance(25) && isUsed(player, EntityType.PUFFERFISH)) {
            livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 10*20, 1, true));
        }

        //Strong Bones
        if(finalHealthPercentage <= 0.5 && notOnCooldown(entity, Keys.strongBones) && isUsed(player, EntityType.SKELETON)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 45*20, 1, true));
            startCooldown(player, Keys.strongBones, 90, "messages.traits.cooldown.strong_bones");
        }
        //Play Dead
        if(finalHealthPercentage <= 0.25 && notOnCooldown(entity, Keys.playDead) && isUsed(player, EntityType.AXOLOTL)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 15*20, 1, true));
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 15*20, 1, true));
            startCooldown(player, Keys.playDead, 60, "messages.traits.cooldown.play_dead");
        }
        //Speedy Escape
        if(finalHealthPercentage <= 0.1 && notOnCooldown(entity, Keys.speedyEscape) && isUsed(player, EntityType.OCELOT)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 15*20, 5, true));
            startCooldown(player, Keys.speedyEscape, 60, "messages.traits.cooldown.speed_escape");
        }
        //Another Chance
        if(event.getFinalDamage() >= entity.getHealth() && isUsed(player, EntityType.EVOKER)) {
            final ItemStack offHand = player.getInventory().getItemInOffHand().clone();
            player.getInventory().setItemInOffHand(new ItemStack(Material.TOTEM_OF_UNDYING));
            Bukkit.getScheduler().runTaskLater(instance, () -> player.getInventory().setItemInOffHand(offHand), 1);
        }
        //Scute Shedding
        if (SpiritUtils.chance(10) && isUsed(player, EntityType.TURTLE)) {
            player.getWorld().dropItemNaturally(player.getLocation(), new ItemStack(Material.SCUTE));
        }
    }
    //Morning Gift
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerSleep(PlayerDeepSleepEvent event) {
        final Player player = event.getPlayer();
        if (notOnCooldown(player, Keys.morningGift) && isUsed(player, EntityType.CAT)) {
            PlayerUtils.addOrDropItem(player, ItemStacks.SU_ECTOPLASM);
            startCooldown(player, Keys.morningGift, 20 * 60, "messages.traits.cooldown.morning_gift");
        }
    }
    //Group Protection
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onAngerPigZombie(PigZombieAngerEvent event) {
        final Entity entity = event.getTarget();
        if (entity instanceof Player player && isUsed(player, EntityType.ZOMBIFIED_PIGLIN)) {
            event.setCancelled(true);
        }
    }
    //No Fatigue
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerGetFatigue(ElderGuardianAppearanceEvent event) {
        if (isUsed(event.getAffectedPlayer(), EntityType.ELDER_GUARDIAN)) {
            event.setCancelled(true);
        }
    }
    //Undead Protection
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onConsumeItem(PlayerItemConsumeEvent event) {
        final Player player = event.getPlayer();
        final ItemStack item = event.getItem();

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
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onHungerChange(FoodLevelChangeEvent event) {
        final HumanEntity entity = event.getEntity();
        if (!(entity instanceof Player player)) {
            return;
        }

        if (SpiritUtils.chance(30) && isUsed(player, EntityType.WOLF)) {
            event.setFoodLevel(event.getFoodLevel()*2);
        }
    }
    //Better Shears
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerShear(PlayerShearEntityEvent event) {
        final Entity entity = event.getEntity();
        final Player player = event.getPlayer();
        if (entity instanceof Sheep && SpiritUtils.chance(75) && isUsed(player, EntityType.SHEEP)) {
            event.getItem().setAmount(event.getItem().getAmount()*2);
        }
    }
    //Pig Rancher
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerEnter(EntityMountEvent event) {
        final Entity rider = event.getEntity();
        final Entity mount = event.getMount();
        if (!(rider instanceof Player player)) {
            return;
        }

        if (mount instanceof Pig pig) {
            if (! isUsed(player, EntityType.PIG)) {
                return;
            }
            pig.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 9, true));
            PersistentDataAPI.setBoolean(pig, Keys.entityKey, true);
        }
    }
    //Pig Rancher
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerLeave(EntityDismountEvent event) {
        final Entity rider = event.getEntity();
        final Entity dismount = event.getDismounted();
        if (!(rider instanceof Player player)) {
            return;
        }

        if (dismount instanceof Pig pig) {
            if (! isUsed(player, EntityType.PIG)) {
                return;
            }
            if (PersistentDataAPI.hasBoolean(pig, Keys.entityKey) && PersistentDataAPI.getBoolean(pig, Keys.entityKey)) {
                pig.removePotionEffect(PotionEffectType.SPEED);
            }
        }
    }

    private boolean isUsed(Player player, EntityType... types) {
        if (player == null) {
            return false;
        }
        
        for (EntityType type : types) {
            if (SpiritUtils.useSpiritItem(player, type, null)) {
                return true;
            }
        }
        
        return false;
    }
    private boolean notOnCooldown(LivingEntity entity, NamespacedKey key) {
        return ! PersistentDataAPI.hasLong(entity, key) || PersistentDataAPI.getLong(entity, key) <= System.currentTimeMillis();
    }
    private void startCooldown(Player player, NamespacedKey key, int when, String path) {
        player.sendActionBar(Component.text(SpiritUtils.getTranslation(path)));
        PersistentDataAPI.setLong(player, key, System.currentTimeMillis() + when * 1000L);
    }
}
