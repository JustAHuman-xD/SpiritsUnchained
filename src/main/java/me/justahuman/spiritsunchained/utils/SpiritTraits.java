package me.justahuman.spiritsunchained.utils;

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import me.justahuman.spiritsunchained.SpiritsUnchained;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Rotation;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.GlowItemFrame;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.LlamaSpit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("unused")
public class SpiritTraits {

    static final Map<UUID, Map<String, Long>> Cooldown_Map = new HashMap<>();

    private static String translate(String path) {
        return SpiritUtils.getTranslation("messages.traits." + path);
    }

    public static String useTrait(Player player, Map<String, Object> traitInfo, ItemStack item) {
        final UUID uuid = player.getUniqueId();
        final String type = PersistentDataAPI.getString(item.getItemMeta(), Keys.spiritItemKey);
        final String name = (String) traitInfo.get("name");
        final String id = (String) traitInfo.get("id");
        final Method traitMethod;

        if (!Slimefun.getProtectionManager().hasPermission(player, player.getLocation(), Interaction.INTERACT_BLOCK) || !Slimefun.getProtectionManager().hasPermission(player, player.getLocation(), Interaction.PLACE_BLOCK) || !Slimefun.getProtectionManager().hasPermission(player, player.getLocation(), Interaction.BREAK_BLOCK) || !Slimefun.getProtectionManager().hasPermission(player, player.getLocation(), Interaction.ATTACK_ENTITY) || !Slimefun.getProtectionManager().hasPermission(player, player.getLocation(), Interaction.ATTACK_PLAYER) || !Slimefun.getProtectionManager().hasPermission(player, player.getLocation(), Interaction.INTERACT_ENTITY)) {
            return SpiritUtils.getTranslation("messages.general.no_permission");
        }

        if (traitInfo.get("type").equals("Passive")) {
            return translate("passive").replace("{trait_name}", name);
        }

        try {
           traitMethod = SpiritTraits.class.getMethod((String) traitInfo.get("id"), Player.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return translate("error").replace("{trait_name}", name);
        }

        //If the Trait is on Cooldown
        if (Cooldown_Map.containsKey(uuid) && Cooldown_Map.get(uuid).containsKey(id) && Cooldown_Map.get(uuid).get(id) > System.currentTimeMillis()) {
            final long cooldown = (Cooldown_Map.get(uuid).get(id) - System.currentTimeMillis()) / 1000;
            return translate("on_cooldown").replace("{trait_name}", name).replace("{cooldown}", String.valueOf(cooldown));
        }

        if (!SpiritUtils.useSpiritItem(player, EntityType.valueOf(type), item)) {
            return null;
        }

        try {
            traitMethod.invoke(SpiritTraits.class, player);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
            return translate("error").replace("{trait_name}", name);
        }

        //Add the cooldown to the Map
        final Map<String, Long> cooldownMap = Cooldown_Map.containsKey(uuid) ? Cooldown_Map.get(uuid) : new HashMap<>();
        cooldownMap.put((String) traitInfo.get("id"), System.currentTimeMillis() + (int) traitInfo.get("cooldown") * 1000);
        Cooldown_Map.put(uuid, cooldownMap);

        return translate("used").replace("{trait_name}", name);
    }

    public static void resetCooldown(Player player) {
        Cooldown_Map.put(player.getUniqueId(), new HashMap<>());
    }

    public static void Clear_Effects(Player player) {
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_DRINK, 2, 1);
        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(potionEffect.getType());
        }
    }
    public static void Eggpult(Player player) {
        SpiritUtils.spawnProjectile(player, Egg.class, "Eggpult");
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_CHICKEN_AMBIENT, 2, 1);
    }
    public static void Aquatic_Creature(Player player) {
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_CONVERTED_TO_DROWNED, 2, 1);
        player.addPotionEffect(new PotionEffect(PotionEffectType.CONDUIT_POWER, 60*20, 0, true));
    }
    public static void Ink_Spray(Player player) {
        final Location location = player.getLocation();
        ParticleUtils.spawnParticleRadius(location, Particle.SQUID_INK, 2, 20, "");
        for(Player nearbyPlayer : player.getWorld().getNearbyPlayers(location, 5, 5, 5)) {
            if (nearbyPlayer.getUniqueId() == player.getUniqueId()) {
                continue;
            }
            nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10*20, 0, true));
        }
        player.getWorld().playSound(location, Sound.ENTITY_SQUID_SQUIRT, 2, 1);
    }
    public static void Hops(Player player) {
        final Location location = player.getLocation();
        player.getWorld().playSound(location, Sound.ENTITY_RABBIT_JUMP, 3, 1);
        ParticleUtils.spawnParticleRadius(location.add(0, 0.5, 0), Particle.COMPOSTER, 3, 20, "");
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 30*20, 1, true));
    }
    public static void Bee_Buddy(Player player) {
        final World world = player.getWorld();
        final Location location = player.getLocation();
        final ItemStack item = (SpiritUtils.random(0.0, 1.0) <= 0.5) ? new ItemStack(Material.HONEYCOMB) : new ItemStack(Material.HONEY_BOTTLE);
        ParticleUtils.spawnParticleRadius(location.add(0, 0.5, 0), Particle.DRIPPING_HONEY, 3, 20, "");

        item.setAmount(SpiritUtils.random(1,6));
        world.playSound(location, Sound.BLOCK_BEEHIVE_SHEAR, 2, 1);
        PlayerUtils.addOrDropItem(player, item);
    }
    public static void Webber(Player player) {
        final Location location = player.getLocation();
        int count = 0;
        for(Player nearbyPlayer : player.getWorld().getNearbyPlayers(location, 5, 5, 5)) {
            if (nearbyPlayer.getUniqueId() == player.getUniqueId()) {
                continue;
            }

            final Block block = nearbyPlayer.getWorld().getBlockAt(nearbyPlayer.getLocation());
            if (block.getType().isAir() || ! block.getType().isSolid()) {
                if (count >= 3) {
                    break;
                }

                final Material restoreType = block.getType();
                final BlockData restoreData = block.getBlockData();
                block.setType(Material.COBWEB);

                Bukkit.getScheduler().runTaskLater(SpiritsUnchained.getInstance(), () -> {
                    block.setType(restoreType);
                    block.setBlockData(restoreData);
                }, 5 * 20L);

                count++;
            }
        }
    }
    public static void Explode(Player player) {
        final Location location = player.getLocation();
        final TNTPrimed tnt = (TNTPrimed) player.getWorld().spawnEntity(location, EntityType.PRIMED_TNT);
        tnt.setFuseTicks(1);
        PersistentDataAPI.setString(tnt, Keys.immuneKey, player.getUniqueId().toString());
        PersistentDataAPI.setString(tnt, Keys.entityKey, "DullExplosion");
    }
    public static void Infest(Player player) {
        final Block lookingAt = player.getTargetBlock(null, 5);
        try{
            final Material newMaterial = Material.valueOf("INFESTED_" + lookingAt.getType());
            lookingAt.setType(newMaterial);
            ParticleUtils.spawnParticleRadius(lookingAt.getLocation(), Particle.ASH, 1.5, 40, "");
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_SILVERFISH_AMBIENT, 1, 1);
        } catch (IllegalArgumentException | NullPointerException e) {
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_SILVERFISH_DEATH, 1, 1);
        }
    }
    public static void Echolocation(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 0, true));
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BAT_AMBIENT, 1, 1);
        for(LivingEntity entity: player.getWorld().getNearbyLivingEntities(player.getLocation(), 100, 100, 100)) {
            if (entity.getUniqueId() == player.getUniqueId()) {
                continue;
            }
            entity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 32*20, 0, true));
        }
    }
    public static void Stew_Maker(Player player) {
        fillItems(player, Material.BOWL, new ItemStack(Material.MUSHROOM_STEW), 5);
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
        final Location location = player.getLocation();
        final World world = player.getWorld();
        final Block block = world.getBlockAt(location);

        for (Block relative : SpiritUtils.getNearbyBlocks(block, 5)) {
            if (relative.getType().toString().contains("SIGN")) {
                final Sign sign = (Sign) relative.getState();
                sign.setGlowingText(true);
                sign.update();
                ParticleUtils.spawnParticleRadius(relative.getLocation(), Particle.GLOW_SQUID_INK, 1.5, 24, "");
                world.playSound(relative.getLocation(), Sound.ENTITY_GLOW_SQUID_SQUIRT, 1, 1);
            }
        }

        for (ItemFrame itemFrame : world.getNearbyEntitiesByType(ItemFrame.class, location, 5)) {
            final ItemStack item = itemFrame.getItem();
            final Rotation rotation = itemFrame.getRotation();
            final BlockFace face = itemFrame.getAttachedFace();
            final Location frameLocation = itemFrame.getLocation();
            itemFrame.remove();
            final GlowItemFrame frame = world.spawn(frameLocation, GlowItemFrame.class);
            frame.setFacingDirection(face.getOppositeFace());
            frame.setItem(item);
            frame.setRotation(rotation);
            ParticleUtils.spawnParticleRadius(itemFrame.getLocation(), Particle.GLOW_SQUID_INK, 1.5, 24, "");
            world.playSound(itemFrame.getLocation(), Sound.ENTITY_GLOW_SQUID_SQUIRT, 1, 1);
        }
    }
    public static void Light_It_Up(Player player) {
        final Location location = player.getLocation();
        final World world = player.getWorld();
        final Block block = world.getBlockAt(location);

        for (Block relative : SpiritUtils.getNearbyBlocks(block, 5)) {
            if (relative.getType() == Material.MAGMA_BLOCK) {
                final Material[] chooseFrom = new Material[] {
                        Material.OCHRE_FROGLIGHT,
                        Material.VERDANT_FROGLIGHT,
                        Material.PEARLESCENT_FROGLIGHT
                };
                final Material newMaterial = chooseFrom[SpiritUtils.random(0,3)];
                relative.setType(newMaterial);
                ParticleUtils.spawnParticleRadius(relative.getLocation(), Particle.END_ROD, 1.5, 24, "Freeze");
            }
        }

        player.playSound(location, Sound.ENTITY_FROG_AMBIENT, 1, 1);
    }
    public static void High_Jump(Player player) {
        player.setVelocity(new Vector(0, 1.5, 0));
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_HORSE_JUMP, 2, 1);
    }
    public static void Spitter(Player player) {
        final Projectile spit = (Projectile) SpiritUtils.spawnProjectile(player, LlamaSpit.class, "Spitter");
        spit.setShooter(player);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_LLAMA_SPIT, 2, 1);
    }
    public static void Goats_Instrument(Player player) {
        final Sound[] playFrom = new Sound[] {
                Sound.ITEM_GOAT_HORN_SOUND_0,
                Sound.ITEM_GOAT_HORN_SOUND_1,
                Sound.ITEM_GOAT_HORN_SOUND_2,
                Sound.ITEM_GOAT_HORN_SOUND_3,
                Sound.ITEM_GOAT_HORN_SOUND_4,
                Sound.ITEM_GOAT_HORN_SOUND_5,
                Sound.ITEM_GOAT_HORN_SOUND_6,
                Sound.ITEM_GOAT_HORN_SOUND_7
        };
        player.getWorld().playSound(player.getLocation(), playFrom[SpiritUtils.random(0,8)], 2, 1);
    }
    public static void Poison_Spray(Player player) {
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_LLAMA_SPIT, 2, 1);
        ParticleUtils.spawnParticleRadius(player.getLocation(), Particle.SCULK_CHARGE_POP, 5, 100, "Freeze");
        for (Player nearbyPlayer : player.getWorld().getNearbyPlayers(player.getLocation(), 5)) {
            if (nearbyPlayer.getUniqueId() == player.getUniqueId()) {
                continue;
            }
            nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 10*20, 0));
        }
    }
    public static void Crit_hit(Player player) {
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_VINDICATOR_AMBIENT, 2, 1);
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 30*20, 1, true));
    }
    public static void Mini_Teleport(Player player) {
        if (player.getVehicle() != null) {
            player.leaveVehicle();
        }

        final Location location = player.getLocation().clone();
        ParticleUtils.spawnParticleRadius(location, Particle.PORTAL, 1.5, 30, "");

        for (int i = 0; i < 16; i++) {
            final int x = location.clone().getBlockX() + SpiritUtils.random(1, 33);
            final int y = location.clone().getBlockY() + SpiritUtils.random(1, 33);
            final int z = location.clone().getBlockZ() + SpiritUtils.random(1, 33);
            final Location teleportTo = new Location(location.getWorld(), x, y ,z);
             if (player.teleport(teleportTo, PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT)) {
                 ParticleUtils.spawnParticleRadius(teleportTo, Particle.PORTAL, 1.5, 30, "");
                 break;
             }
        }

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
        final Inventory inventory = player.getInventory();
        if (inventory.contains(Material.GLASS_BOTTLE)) {
            final ItemStack potion1 = new ItemStack(Material.SPLASH_POTION);
            final PotionMeta meta1 = ((PotionMeta) potion1.getItemMeta());
            meta1.setBasePotionData(new PotionData(PotionType.REGEN, false, true));
            potion1.setItemMeta(meta1);

            final ItemStack potion2 = new ItemStack(Material.SPLASH_POTION);
            final PotionMeta meta2 = ((PotionMeta) potion2.getItemMeta());
            meta2.setBasePotionData(new PotionData(PotionType.STRENGTH, false, true));
            potion2.setItemMeta(meta2);

            final ItemStack potion3 = new ItemStack(Material.SPLASH_POTION);
            final PotionMeta meta3 = ((PotionMeta) potion3.getItemMeta());
            meta3.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL, false, true));
            potion3.setItemMeta(meta3);

            final ItemStack[] potions = new ItemStack[] {
                    potion1, potion2, potion3
            };
            final int amount = SpiritUtils.random(0, 5);
            for (int current = 0; current < amount; current++) {
                fillItems(player, Material.GLASS_BOTTLE, potions[SpiritUtils.random(0, 4)], 1);
            }
        }
    }
    public static void Heavy_Hit(Player player) {
        PersistentDataAPI.setBoolean(player, Keys.heavyHitKey, true);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_HOGLIN_ATTACK, 2, 1);
    }
    public static void Targeted_Teleport(Player player) {
        final RayTraceResult rs = player.getWorld().rayTraceBlocks(player.getEyeLocation(), player.getEyeLocation().getDirection(), 64);
        if (rs == null || rs.getHitBlock() == null) {
            return;
        }
        final Location location = player.getLocation();
        final float yaw = location.getYaw();
        final float pitch = location.getPitch();
        final double D = 1;
        final double x = -D*Math.sin(yaw*Math.PI/180);
        final double z = D*Math.cos(yaw*Math.PI/180);
        final Location targetLoc = rs.getHitBlock().getLocation().subtract(x, -1, z);
        targetLoc.setYaw(yaw);
        targetLoc.setPitch(pitch);
        ParticleUtils.spawnParticleRadius(location, Particle.PORTAL, 1.5, 30, "");
        player.teleport(targetLoc);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 2, 1);
        ParticleUtils.spawnParticleRadius(targetLoc, Particle.PORTAL, 1.5, 30, "");
    }
    public static void Tank(Player player) {
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_RAVAGER_ATTACK, 2, 1);
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 15*20, 1, true));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 15*20, 2, true));
    }
    public static void Bullet_Swarm(Player player) {
        final World world = player.getWorld();
        final Location location = player.getLocation();
        final List<LivingEntity> targets = new ArrayList<>();
        for (LivingEntity nearbyEntity : world.getNearbyLivingEntities(location, 20, 20, 20)) {
            if (nearbyEntity.getUniqueId() == player.getUniqueId() || nearbyEntity instanceof ArmorStand) {
                continue;
            }
            targets.add(nearbyEntity);
        }
        for (int spawned = 0; spawned < 5; spawned++) {
            final LivingEntity target = !targets.isEmpty() ? targets.get(SpiritUtils.random(0, targets.size())) : null;
            final int x = SpiritUtils.random(0, 5) * (SpiritUtils.random(0.0, 1.0) >= 0.5 ? -1 : 1);
            final int z = SpiritUtils.random(0, 5) * (SpiritUtils.random(0.0, 1.0) >= 0.5 ? -1 : 1);
            final ShulkerBullet bullet = (ShulkerBullet) world.spawnEntity(location.add(x,2,z), EntityType.SHULKER_BULLET);
            PersistentDataAPI.setString(bullet, Keys.immuneKey, player.getUniqueId().toString());
            bullet.setShooter(player);
            if (target != null) {
                bullet.setTarget(target);
                targets.remove(target);
            }
        }
    }
    public static void Skull_Fire(Player player) {
        SpiritUtils.spawnProjectile(player, WitherSkull.class, "Skull_Fire");
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 2, 1);
    }
    public static void Dragons_Breath(Player player) {
        fillItems(player, Material.GLASS_BOTTLE, new ItemStack(Material.DRAGON_BREATH), 9);
    }
    public static void Dark_Aura(Player player) {
        final World world = player.getWorld();
        final Location location = player.getLocation();
        for (Player nearbyPlayer : world.getNearbyPlayers(location, 30, 30, 30)) {
            if (nearbyPlayer.getUniqueId() == player.getUniqueId()) {
                continue;
            }
            final Location nearbyLocation = nearbyPlayer.getLocation();
            nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 30*20, 0, true));
            nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30*20, 3, true));
            world.playSound(nearbyLocation, Sound.ENTITY_WARDEN_HEARTBEAT, 3, 1);
            ParticleUtils.spawnParticleRadius(nearbyLocation, Particle.SCULK_SOUL, 2, 30, "");
        }
        world.playSound(location, Sound.ENTITY_WARDEN_ROAR, 1, 1);
    }
    public static void fillItems(Player player, Material fill, ItemStack fillWith, int howMany) {
        final Inventory inventory = player.getInventory();
        if (inventory.contains(fill)) {
            int fillAmount = 0;
            for(ItemStack item : inventory.getContents()) {
                if (item != null && item.getType() == fill) {
                    fillAmount = (item.getAmount() >= howMany ? SpiritUtils.random(1, howMany) : SpiritUtils.random(0, item.getAmount()));
                    item.setAmount(item.getAmount() - fillAmount);
                }
            }
            fillWith.setAmount(fillAmount);
            PlayerUtils.addOrDropItem(player, fillWith);
        }
    }
}
