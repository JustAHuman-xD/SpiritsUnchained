package me.justahuman.spiritsunchained.utils;

import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;
import it.unimi.dsi.fastutil.longs.Long2BooleanArrayMap;
import me.justahuman.spiritsunchained.SpiritsUnchained;

import net.md_5.bungee.api.ChatMessageType;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.LlamaSpit;
import org.bukkit.entity.Player;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.WitherSkull;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class SpiritTraits {

    static Map<UUID, Map<String, Long>> Cooldown_Map = new HashMap<>();

    public static String useTrait(Player player, Map<String, Object> traitInfo, String state) {
        UUID uuid = player.getUniqueId();
        String name = (String) traitInfo.get("name");
        Method traitMethod = null;

        if (! (SpiritUtils.getStates().indexOf(state) >= 2)) {
            return name + " needs to be to the Gentle State or Higher!";
        }

        if (traitInfo.get("type").equals("Passive")) {
            return name + " is a Passive Trait!";
        }

        try {
           traitMethod = SpiritTraits.class.getMethod((String) traitInfo.get("id"), Player.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        if (traitMethod == null) {
            return "Error";
        }

        //If the Trait is on Cooldown
        if (Cooldown_Map.containsKey(uuid) && Cooldown_Map.get(uuid).containsKey((String) traitInfo.get("id")) && Cooldown_Map.get(uuid).get((String) traitInfo.get("id")) > System.currentTimeMillis()) {
            long cooldown = (Cooldown_Map.get(uuid).get((String) traitInfo.get("id")) - System.currentTimeMillis()) / 1000;
            return name + " on Cooldown! (" + cooldown + "s)";
        }

        try {
            traitMethod.invoke(SpiritTraits.class, player);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            return "Error";
        }

        //Add the cooldown to the Map
        Map<String, Long> cooldowns = Cooldown_Map.containsKey(uuid) ? Cooldown_Map.get(uuid) : new HashMap<>();
        cooldowns.put((String) traitInfo.get("id"), System.currentTimeMillis() + (int) traitInfo.get("cooldown") * 1000);
        Cooldown_Map.put(uuid, cooldowns);

        return name + " Used!";
    }

    public static void Clear_Effects(Player player) {
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_DRINK, 2, 1);
        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(potionEffect.getType());
        }
    }
    public static void Eggpult(Player player) {
        SpiritUtils.spawnProjectile(player, Egg.class, "Eggpult", 0);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_CHICKEN_AMBIENT, 2, 1);
    }
    public static void Aquatic_Creature(Player player) {
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_CONVERTED_TO_DROWNED, 2, 1);
        player.addPotionEffect(new PotionEffect(PotionEffectType.CONDUIT_POWER, 60*20, 0, true));
    }
    public static void Ink_Spray(Player player) {
        Location location = player.getLocation();
        ParticleUtils.spawnParticleRadius(location, Particle.SQUID_INK, 2, 20, true, false);
        for(Player nearbyPlayer : player.getWorld().getNearbyPlayers(location, 5, 5, 5)) {
            if (nearbyPlayer.getUniqueId() == player.getUniqueId()) {
                continue;
            }
            nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10*20, 1, true));
        }
        player.getWorld().playSound(location, Sound.ENTITY_SQUID_SQUIRT, 2, 1);
    }
    public static void Hops(Player player) {
        Location location = player.getLocation();
        player.getWorld().playSound(location, Sound.ENTITY_RABBIT_JUMP, 3, 1);
        ParticleUtils.spawnParticleRadius(location.add(0, 0.5, 0), Particle.COMPOSTER, 3, 20, true, false);
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 30*20, 1, true));
    }
    public static void Bee_Buddy(Player player) {
        World world = player.getWorld();
        Location location = player.getLocation();
        ItemStack item = (Math.random() <= 0.5) ? new ItemStack(Material.HONEYCOMB) : new ItemStack(Material.HONEY_BOTTLE);
        ParticleUtils.spawnParticleRadius(location.add(0, 0.5, 0), Particle.DRIPPING_HONEY, 3, 20, true, false);

        item.setAmount(new Random().nextInt(1,6));
        world.playSound(location, Sound.BLOCK_BEEHIVE_SHEAR, 2, 1);
        PlayerUtils.addOrDropItem(player, item);
    }
    public static void Webber(Player player) {
        Location location = player.getLocation();
        int count = 0;
        for(Player nearbyPlayer : player.getWorld().getNearbyPlayers(location, 5, 5, 5)) {
            if (nearbyPlayer.getUniqueId() == player.getUniqueId()) {
                continue;
            }

            Block block = nearbyPlayer.getWorld().getBlockAt(nearbyPlayer.getLocation());
            if (block.getType().isAir() || ! block.getType().isSolid()) {
                if (count >= 3) {
                    break;
                }

                Material restoreType = block.getType();
                BlockData restoreData = block.getBlockData();
                block.setType(Material.COBWEB);

                Bukkit.getScheduler().runTaskLater(SpiritsUnchained.getInstance(), () -> {
                    block.setType(restoreType);
                    block.setBlockData(restoreData);
                }, 30*20);

                count++;
            }
        }
    }
    public static void Explode(Player player) {
        Location location = player.getLocation();
        TNTPrimed tnt = (TNTPrimed) player.getWorld().spawnEntity(location, EntityType.PRIMED_TNT);
        tnt.setFuseTicks(1);
        PersistentDataAPI.setString(tnt, Keys.immuneKey, player.getUniqueId().toString());
        PersistentDataAPI.setString(tnt, Keys.entityKey, "DullExplosion");
    }
    public static void Infest(Player player) {
        Block lookingAt = player.getTargetBlock(null, 5);
        try{
            Material newMatieral = Material.valueOf("infested_" + lookingAt.getType().toString());
            lookingAt.setType(newMatieral);
            ParticleUtils.spawnParticleRadius(lookingAt.getLocation(), Particle.ASH, 1.5, 40, false, false);
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_SILVERFISH_AMBIENT, 1, 1);
        } catch (IllegalArgumentException | NullPointerException e) {
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_SILVERFISH_DEATH, 1, 1);
        }
    }
    public static void Echolocation(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1*20, 0, true));
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BAT_AMBIENT, 1, 1);
        for(LivingEntity entity: player.getWorld().getNearbyLivingEntities(player.getLocation(), 15, 15, 15)) {
            if (entity.getUniqueId() == player.getUniqueId()) {
                continue;
            }
            entity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 32*20, 1, true));
        }
    }
    public static void Stew_Maker(Player player) {
        Inventory inventory = player.getInventory();
        if (inventory.contains(Material.BOWL)) {
            int stewAmount = 0;
            for(ItemStack item : inventory.getContents()) {
                if (item.getType() == Material.BOWL) {
                    stewAmount = (item.getAmount() >= 3 ? new Random().nextInt(0,4) : new Random().nextInt(0,item.getAmount()));
                    item.setAmount(item.getAmount() - stewAmount);
                }
            }

            PlayerUtils.addOrDropItem(player, new ItemStack(Material.MUSHROOM_STEW, stewAmount));
        }
    }
    public static void Lava_Walker(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 60*20, 0, true));
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_STRIDER_HAPPY, 1, 1);
    }
    public static void Villager_Friend(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE, 60*20, 4, true));
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, 1, 1);
    }
    public static void Glow_Up(Player player) {
        Location location = player.getLocation();
        World world = player.getWorld();
        Block block = world.getBlockAt(location);

        for (Block relative : SpiritUtils.getNearbyBlocks(block, 5)) {
            if (relative.getType().toString().contains("SIGN")) {
                Sign sign = (Sign) relative.getState();
                sign.setGlowingText(true);
                ParticleUtils.spawnParticleRadius(relative.getLocation(), Particle.GLOW_SQUID_INK, 1.5, 24, false, false);
                world.playSound(relative.getLocation(), Sound.ENTITY_GLOW_SQUID_SQUIRT, 1, 1);
            }
        }

        for (ItemFrame itemFrame : world.getNearbyEntitiesByType(ItemFrame.class, location, 5)) {
            itemFrame.setGlowing(true);
            ParticleUtils.spawnParticleRadius(itemFrame.getLocation(), Particle.GLOW_SQUID_INK, 1.5, 24, false, false);
            world.playSound(itemFrame.getLocation(), Sound.ENTITY_GLOW_SQUID_SQUIRT, 1, 1);
        }
    }
    public static void Light_It_Up(Player player) {
        Location location = player.getLocation();
        World world = player.getWorld();
        Block block = world.getBlockAt(location);

        for (Block relative : SpiritUtils.getNearbyBlocks(block, 5)) {
            if (relative.getType() == Material.MAGMA_BLOCK) {
                Material[] chooseFrom = new Material[] {
                        Material.OCHRE_FROGLIGHT,
                        Material.VERDANT_FROGLIGHT,
                        Material.PEARLESCENT_FROGLIGHT
                };
                Material newMaterial = chooseFrom[new Random().nextInt(3)];
                relative.setType(newMaterial);
                ParticleUtils.spawnParticleRadius(relative.getLocation(), Particle.END_ROD, 1.5, 24, true, false);
            }
        }

        player.playSound(location, Sound.ENTITY_FROG_AMBIENT, 1, 1);
    }
    public static void High_Jump(Player player) {
        player.setVelocity(new Vector(0, 15, 0));
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_HORSE_JUMP, 2, 1);
    }
    public static void Spitter(Player player) {
        SpiritUtils.spawnProjectile(player, LlamaSpit.class, "Spitter", 0);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_LLAMA_SPIT, 2, 1);
    }
    public static void Goats_Instrument(Player player) {
        player.getWorld().playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_PLAY, 2, 1);
    }
    public static void Poison_Spray(Player player) {
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_LLAMA_SPIT, 2, 1);
        ParticleUtils.spawnParticleRadius(player.getLocation(), Particle.SCULK_CHARGE_POP, 5, 30, true, false);
        for (Player nearbyPlayer : player.getWorld().getNearbyPlayers(player.getLocation(), 5)) {
            if (nearbyPlayer.getUniqueId() == player.getUniqueId()) {
                continue;
            }
            nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 10*20, 1));
        }
    }
    public static void Crit_hit(Player player) {
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_VINDICATOR_AMBIENT, 2, 1);
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 30*20, 2, true));
    }
    public static void Mini_Teleport(Player player) {
        if (player.getVehicle() != null) {
            player.leaveVehicle();
        }
        World world = player.getWorld();
        Location location = player.getLocation();

        double x = location.getX() + (new Random().nextDouble() - 0.5D) * 16.0D;
        double y = Math.max(location.getY() + (double) (new Random().nextInt(16) - 8), Math.min(world.getMinHeight(), (double) (world.getMinHeight() + world.getLogicalHeight() - 1)));
        double z = location.getZ() + (new Random().nextDouble() - 0.5D) * 16.0D;

        player.teleport(player.getLocation().add(x, y, z));
        player.getWorld().playSound(player.getLocation(), Sound.ITEM_CHORUS_FRUIT_TELEPORT, 2, 1);
    }
    public static void Sleep_No_More(Player player) {
        player.setStatistic(Statistic.TIME_SINCE_REST, 0);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PHANTOM_AMBIENT, 2, 1);
    }
    public static void Magma_Trap(Player player) {
        for (Player nearbyPlayer : player.getWorld().getNearbyPlayers(player.getLocation(), 5)) {
            if (nearbyPlayer.getUniqueId() == player.getUniqueId()) {
                continue;
            }
            nearbyPlayer.setFireTicks(10*20);
        }
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_MAGMA_CUBE_SQUISH, 2, 1);
    }
    public static void Better_Brewer(Player player) {
        Inventory inventory = player.getInventory();
        if (inventory.contains(Material.GLASS_BOTTLE)) {
            int potionAmount = 0;
            for(ItemStack item : inventory.getContents()) {
                if (item.getType() == Material.GLASS_BOTTLE) {
                    potionAmount = (item.getAmount() >= 3 ? new Random().nextInt(0,4) : new Random().nextInt(0,item.getAmount()));
                    item.setAmount(item.getAmount() - potionAmount);
                }
            }
            ItemStack potion1 = new ItemStack(Material.SPLASH_POTION);
            PotionMeta meta1 = ((PotionMeta) potion1.getItemMeta());
            meta1.addCustomEffect(new PotionEffect(PotionEffectType.LEVITATION, 40, 1), true); //<-- 40 ticks whereas you had 10 ticks
            potion1.setItemMeta(meta1);

            ItemStack potion2 = new ItemStack(Material.POTION);
            PotionMeta meta2 = ((PotionMeta) potion2.getItemMeta());
            meta2.addCustomEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60*20, 1), true); //<-- 40 ticks whereas you had 10 ticks
            potion2.setItemMeta(meta2);

            ItemStack potion3 = new ItemStack(Material.POTION);
            PotionMeta meta3 = ((PotionMeta) potion3.getItemMeta());
            meta2.addCustomEffect(new PotionEffect(PotionEffectType.HEAL, 60*20, 1), true); //<-- 40 ticks whereas you had 10 ticks
            potion3.setItemMeta(meta3);

            ItemStack[] potions = new ItemStack[] {
                    potion1, potion2, potion3
            };
            for (int current = 0; current != potionAmount; potionAmount++) {
                PlayerUtils.addOrDropItem(player, potions[new Random().nextInt(3)]);
            }
        }
    }
    public static void Heavy_Hit(Player player) {
        PersistentDataAPI.setBoolean(player, Keys.heavyHitKey, true);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_HOGLIN_ATTACK, 2, 1);
    }
    public static void Targeted_Teleport(Player player) {
        RayTraceResult rs = player.getWorld().rayTraceBlocks(player.getEyeLocation(), player.getEyeLocation().getDirection(), 64);
        if (rs == null) {
            return;
        }
        float yaw = player.getLocation().getYaw();
        double D = 1;
        double x = -D*Math.sin(yaw*Math.PI/180);
        double z = D*Math.cos(yaw*Math.PI/180);
        Location targetLoc = rs.getHitBlock().getLocation().subtract(x, -1, z);
        player.teleport(targetLoc);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 2, 1);
    }
    public static void Tank(Player player) {
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_RAVAGER_ATTACK, 2, 1);
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 30*20, 2, true));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 30*20, 2, true));
    }
    public static void Bullet_Swarm(Player player) {
        World world = player.getWorld();
        Location location = player.getLocation();
        List<Player> targets = new ArrayList<>();
        for (Player nearbyPlayer : world.getNearbyPlayers(location, 20, 20, 20)) {
            if (nearbyPlayer.getUniqueId() == player.getUniqueId()) {
                continue;
            }
            for (Player target : targets) {
                if (location.distance(nearbyPlayer.getLocation()) > location.distance(target.getLocation())) {
                    targets.remove(nearbyPlayer);
                    targets.add(targets.indexOf(target), nearbyPlayer);
                }
            }
        }
        for (int spawned = 0; spawned < 5; spawned++) {
            ShulkerBullet bullet = (ShulkerBullet) world.spawnEntity(location.add(0,2,0), EntityType.SHULKER_BULLET);
            bullet.setShooter(player);
            bullet.setTarget(targets.get(spawned));
        }
    }
    public static void Skull_Fire(Player player) {
        SpiritUtils.spawnProjectile(player, WitherSkull.class, "Skull_Fire", 0);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 2, 1);
    }
    public static void Dark_Aura(Player player) {
        World world = player.getWorld();
        Location location = player.getLocation();
        for (Player nearbyPlayer : world.getNearbyPlayers(location, 30, 30, 30)) {
            if (nearbyPlayer.getUniqueId() == player.getUniqueId()) {
                continue;
            }
            Location nearbyLocation = nearbyPlayer.getLocation();
            nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 30*20, 4, true));
            nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30*20, 4, true));
            world.playSound(nearbyLocation, Sound.ENTITY_WARDEN_HEARTBEAT, 3, 1);
            ParticleUtils.spawnParticleRadius(nearbyLocation, Particle.SCULK_SOUL, 2, 30, false, false);
        }
        world.playSound(location, Sound.ENTITY_WARDEN_ROAR, 1, 1);
    }
}
