package me.justahuman.spiritsunchained.utils;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.libraries.dough.common.ChatColors;
import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;

import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.justahuman.spiritsunchained.implementation.tools.SpiritLenses;
import me.justahuman.spiritsunchained.spirits.SpiritDefinition;

import net.kyori.adventure.text.Component;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Allay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class SpiritUtils {

    public static Map<Integer, Entity> SpiritIdMap = new HashMap<>();

    public static List<String> getStates() {
        List<String> states = new ArrayList<>();
        states.add("Hostile");
        states.add("Aggressive");
        states.add("Passive");
        states.add("Gentle");
        states.add("Friendly");
        return states;
    }

    public static List<String> getTypes() {
        List<String> types = new ArrayList<>();
        for (EntityType type : SpiritsUnchained.getSpiritsManager().getSpiritMap().keySet()) {
            types.add(type.name());
        }
        return types;
    }

    public static ChatColor tierColor(int tier) {
        return switch (tier) {
            default -> ChatColor.YELLOW;
            case 2 -> ChatColor.AQUA;
            case 3 -> ChatColor.LIGHT_PURPLE;
            case 4 -> ChatColor.GOLD;
        };
    }

    public static ChatColor stateColor(String state) {
        return switch (state) {
            default -> ChatColor.YELLOW;
            case "Hostile" -> ChatColor.DARK_RED;
            case "Aggressive" -> ChatColor.RED;
            case "Gentle" -> ChatColor.GREEN;
            case "Friendly" -> ChatColor.DARK_GREEN;
        };
    }

    @ParametersAreNonnullByDefault
    public static FireworkEffect effectColor(EntityType type) {
        return switch (type) {
            default -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(100, 100, 100)).build();
            case AXOLOTL -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(235, 181, 213)).build();
            case BAT -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(64, 53, 41)).build();
            case BEE, PUFFERFISH -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(228, 165, 1)).build();
            case BLAZE -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(231, 167, 1)).build();
            case CAT -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(221, 185, 131)).build();
            case CAVE_SPIDER -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(11, 61, 72)).build();
            case CHICKEN -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(149, 149, 149)).build();
            case COD -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(164, 142, 90)).build();
            case COW -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(63, 50, 35)).build();
            case CREEPER -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(12, 157, 10)).build();
            case DOLPHIN -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(32, 55, 72)).build();
            case DONKEY -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(78, 65, 53)).build();
            case DROWNED -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(132, 223, 199)).build();
            case ELDER_GUARDIAN -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(193, 191, 174)).build();
            case ENDERMAN, ENDERMITE, ENDER_DRAGON -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(20, 20, 20)).build();
            case EVOKER -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(138, 144, 144)).build();
            case FOX -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(197, 169, 147)).build();
            case FROG -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(195, 109, 64)).build();
            case GHAST -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(234, 234, 234)).build();
            case GLOW_SQUID -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(8, 80, 80)).build();
            case GOAT -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(155, 139, 116)).build();
            case GUARDIAN -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(83, 120, 106)).build();
            case HOGLIN -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(186, 103, 80)).build();
            case HORSE -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(183, 151, 119)).build();
            case HUSK -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(112, 104, 90)).build();
            case LLAMA -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(180, 148, 117)).build();
            case MAGMA_CUBE -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(49, 0, 0)).build();
            case MUSHROOM_COW -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(150, 14, 15)).build();
            case MULE -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(25, 2, 0)).build();
            case OCELOT -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(221, 206, 116)).build();
            case PANDA, SHEEP, TURTLE, IRON_GOLEM, SNOWMAN -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(217, 217, 217)).build();
            case PARROT -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(12, 157, 10)).build();
            case PHANTOM -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(63, 76, 129)).build();
            case PIG -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(225, 155, 152)).build();
            case PIGLIN -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(143, 89, 60)).build();
            case PIGLIN_BRUTE -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(83, 39, 15)).build();
            case PILLAGER -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(78, 44, 51)).build();
            case POLAR_BEAR -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(224, 224, 224)).build();
            case RABBIT -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(143, 89, 60)).build();
            case RAVAGER -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(108, 107, 104)).build();
            case SALMON -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(150, 14, 15)).build();
            case SHULKER -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(137, 95, 137)).build();
            case SILVERFISH -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(102, 102, 102)).build();
            case SKELETON -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(181, 181, 181)).build();
            case SKELETON_HORSE -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(97, 97, 74)).build();
            case SLIME -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(76, 150, 58)).build();
            case SPIDER -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(49, 42, 37)).build();
            case SQUID -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(32, 55, 72)).build();
            case STRAY -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(91, 111, 112)).build();
            case STRIDER -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(146, 49, 51)).build();
            case TADPOLE -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(102, 78, 57)).build();
            case TROPICAL_FISH -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(224, 98, 20)).build();
            case VEX -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(114, 135, 154)).build();
            case VILLAGER -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(81, 56, 48)).build();
            case VINDICATOR -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(140, 145, 145)).build();
            case WANDERING_TRADER -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(65, 92, 141)).build();
            case WARDEN -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(14, 66, 68)).build();
            case WITCH -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(49, 0, 0)).build();
            case WITHER_SKELETON, WITHER -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(19, 19, 19)).build();
            case WOLF -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(202, 198, 198)).build();
            case ZOGLIN -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(186, 103, 80)).build();
            case ZOMBIE -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(0, 164, 164)).build();
            case ZOMBIE_HORSE -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(46, 77, 49)).build();
            case ZOMBIE_VILLAGER -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(81, 56, 48)).build();
            case ZOMBIFIED_PIGLIN -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(219, 138, 138)).build();
        };
    }
    @ParametersAreNonnullByDefault
    public static Map<String, Object> getTraitInfo(String traitId) {
        FileConfiguration traits = SpiritsUnchained.getConfigManager().getTraits();
        ConfigurationSection trait = traits.getConfigurationSection(traitId);
        Map<String, Object> toReturn = new HashMap<>();
        if (trait == null) {return toReturn;}
        toReturn.put("id", traitId);
        toReturn.put("name", trait.getString("name"));
        toReturn.put("cooldown", trait.getInt("cooldown", 0));
        toReturn.put("type", trait.getString("type"));
        toReturn.put("lore", trait.getStringList("lore"));
        return toReturn;
    }

    @ParametersAreNonnullByDefault
    public static void spawnStateParticle(String state, Location location) {
        Particle.DustOptions dustOptions;
        switch(state) {
            default -> dustOptions = new Particle.DustOptions(Color.fromRGB(80,80,80), 1);
            case "Hostile" -> dustOptions = new Particle.DustOptions(Color.fromRGB(180,0,0), 1);
            case "Aggressive" -> dustOptions = new Particle.DustOptions(Color.fromRGB(200,20,20), 1);
            case "Gentle" -> dustOptions = new Particle.DustOptions(Color.fromRGB(20,200,20), 1);
            case "Friendly" -> dustOptions = new Particle.DustOptions(Color.fromRGB(0,180,20), 1);
        }
        Collection<Player> collection = getNearImbued(location);
        for (Player player : collection) {
            player.spawnParticle(Particle.REDSTONE, location.clone().add(0,0.5,0), 1, 0, 0, 0, dustOptions);
        }
    }

    public static boolean isSpiritItem(ItemStack itemStack) {
        return PersistentDataAPI.hasString(itemStack.getItemMeta(), Keys.spiritItemKey);
    }

    public static ItemStack getSpiritItem(Player player, EntityType type) {
        Inventory inventory = player.getInventory();
        if (inventory.contains(Material.FIREWORK_STAR)) {
            for (ItemStack item : inventory.getContents()) {
                if (item != null && item.getType() == Material.FIREWORK_STAR && isSpiritItem(item) && PersistentDataAPI.getString(item.getItemMeta(), Keys.spiritItemKey).equals(String.valueOf(type)) ) {
                    return item;
                }
            }
        }
        return null;
    }

    public static double getTraitUsage(String trait) {
        return switch(trait) {
            default -> 1;
            case "Multishoot", "Hunger_Hit", "Slow_Shot" -> 2;
            case "Iron_Defense" -> 3;
            case "Clear_Effects", "Pig_Rancher", "Aquatic_Creature", "Ink_Spray", "Hops", "Undead_Protection", "Group_Protection", "Infest", "High_Jump", "Morning_Gift", "Spitter", "Natural_Fisher", "Goats_Instrument", "Strange_Secrets", "Mini_Teleport", "Heavy_Hit" -> 5;
            case "Speedy_Escape", "Bee_Buddy", "Strong_Bones", "Stew_Maker", "Villager_Friend", "Glow_Up", "Light_It_Up", "Poison_Spray", "Sleep_No_More" -> 10;
            case "Better_Brewer", "Targeted_Teleport", "Skull_Fire", "Dragons_Breath", "Dark_Aura" -> 15;
            case "Eggpult", "Webber", "Explode", "Lava_Walker", "Play_Dead", "Crit_hit", "Magma_Trap", "Tank", "Bullet_Swarm" -> 25;
            case "Another_Chance" -> 30;
        };
    }

    public static boolean useSpiritItem(Player player, EntityType type) {
        if (player == null) {
            return false;
        }
        ItemStack spiritItem = getSpiritItem(player, type);
        if (spiritItem != null) {
            ItemMeta meta = spiritItem.getItemMeta();
            String state = PersistentDataAPI.getString(meta, Keys.spiritStateKey);
            SpiritDefinition definition = SpiritsUnchained.getSpiritsManager().getSpiritMap().get(type);
            String traitType = (String) getTraitInfo(definition.getTrait()).get("type");
            if (getStates().indexOf(state) > 2) {
                updateSpiritItemProgress(spiritItem, - getTraitUsage(definition.getTrait()));
                return true;
            }
        }
        return false;
    }

    public static boolean updateSpiritItemProgress(ItemStack item, double updateWith) {
        ItemMeta meta = item.getItemMeta();
        List<Component> lore = meta.lore();
        String state = PersistentDataAPI.getString(meta, Keys.spiritStateKey);
        double progress = PersistentDataAPI.getDouble(meta, Keys.spiritProgressKey);
        double newProgress = progress + updateWith;
        boolean toReturn = false;
        if (newProgress >= 100) {
            int index = getStates().indexOf(state);
            boolean canIncrease = ! getStates().get(getStates().size()-1).equals(state);
            state = canIncrease ? getStates().get(index + 1) : state;
            newProgress = canIncrease ? newProgress - 100.0 : 100.0;
            toReturn = true;
        } else if (newProgress <= 0) {
            int index = getStates().indexOf(state);
            boolean canDecrease = !(index == 0);
            state = canDecrease ? getStates().get(index - 1) : state;
            newProgress = canDecrease ? newProgress + 100.0 : 0.0;
            toReturn = true;
        }
        newProgress = new BigDecimal(newProgress).setScale(2, RoundingMode.HALF_UP).doubleValue();
        PersistentDataAPI.setDouble(meta, Keys.spiritProgressKey, newProgress);
        PersistentDataAPI.setString(meta, Keys.spiritStateKey, state);
        lore.set(2, Component.text(ChatColors.color("&fCurrent State: " + stateColor(state) + state)));
        lore.set(5, Component.text(ChatColors.color("&fProgress: " + getProgress(newProgress))));
        meta.lore(lore);
        item.setItemMeta(meta);
        return toReturn;
    }
    @ParametersAreNonnullByDefault
    public static Collection<Entity> getNearbySpirits(Location location) {
        Collection<Entity> nearbyEntities = location.getNearbyEntities(48, 48, 48);
        Collection<Entity> returnList = new ArrayList<>();
        if (nearbyEntities.size() < 1) {return returnList;}
        for (Entity entity : nearbyEntities) {
            if (! (SpiritsUnchained.getSpiritEntityManager().getCustomClass(entity, null) == null)) {
                returnList.add(entity);
            }
        }
        return returnList;
    }

    @Nullable
    public static String getSpawnMob(Location location) {
        List<List<EntityType>> tierMap = SpiritsUnchained.getSpiritsManager().getTierMaps();
        World world = location.getWorld();
        Biome biome = location.getBlock().getBiome();
        World.Environment dimension = world.getEnvironment();
        boolean isDay = world.isDayTime();
        int chance = ThreadLocalRandom.current().nextInt(1, 100);
        String spirit = null;
        int tier;
        if (chance > 40) {
            tier = 0;
        } else if (chance > 10) {
            tier = 1;
        } else if (chance > 1) {
            tier = 2;
        } else if (chance > 0) {
            tier = 3;
        } else {
            tier = 0;
        }
        List<EntityType> getFrom = tierMap.get(tier);
        Collections.shuffle(getFrom);
        for (EntityType entityType : getFrom) {
            SpiritDefinition definition = SpiritsUnchained.getSpiritsManager().getSpiritMap().get(entityType);
            if (dimension != World.Environment.valueOf(definition.getDimension())) {continue;} // Check if Dimensions Match Up
            if (definition.getTimes().size() > 0) { //Check if the Spirit has a Time Requirement & if it does then is it Met
                boolean timeRight = false;
                for (String time : definition.getTimes()) {
                    if (time.equals("Day") && isDay) {timeRight = true;}
                    if (time.equals("Night") && !isDay) {timeRight = true;}
                }
                if (!timeRight) {continue;}
            }
            if (definition.getBiome_group().size() > 0) {
                boolean inBiome = false;
                for (String biomeId : definition.getBiome_group()) {
                    for (String CurrentBiome : SpiritsUnchained.getConfigManager().getBiomeMap().get(biomeId)) {
                        if (biome == Biome.valueOf(CurrentBiome.toUpperCase())) {
                            inBiome = true;
                        }
                    }
                }
                if (!inBiome) {continue;}
            }
            spirit = entityType.name();
        }
        return spirit;
    }

    public static boolean canSpawn() {
        return SpiritIdMap.size() < SpiritsUnchained.getInstance().getConfig().getInt("max-spirits", 40);
    }

    public static boolean imbuedCheck(ItemStack helmetItem) {
        return SlimefunItem.getByItem(helmetItem) instanceof SpiritLenses || PersistentDataAPI.hasByte(helmetItem.getItemMeta(), Keys.imbuedKey) && PersistentDataAPI.getByte(helmetItem.getItemMeta(), Keys.imbuedKey) == 2;
    }

    public static Collection<Player> getNearImbued(Location location) {
        Collection<Entity> collection = location.getWorld().getNearbyEntities(location, 48, 48, 48);
        Collection<Player> toReturn = new ArrayList<>();
        for (Entity entity : collection) {
            if (entity instanceof Player player) {
                ItemStack helmetItem = player.getInventory().getHelmet();
                if (helmetItem == null) {continue;}
                if (imbuedCheck(helmetItem)) {
                    toReturn.add(player);
                }
            }
        }
        return toReturn;
    }

    public static int getPlayerCap() {
        return SpiritsUnchained.getInstance().getConfig().getInt("player-spirit-cap", 4);
    }

    public static ItemStack SpiritItem(String state, SpiritDefinition definition) {
        ItemStack itemStack = new ItemStack(Material.FIREWORK_STAR);
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<Component> itemLore = new ArrayList<>();

        ChatColor tierColor = tierColor(definition.getTier());
        ChatColor stateColor = stateColor(state);
        String spiritType  = ChatUtils.humanize(definition.getType().name());
        Map<String, Object> traitInfo = getTraitInfo(definition.getTrait());

        ((FireworkEffectMeta) itemMeta).setEffect(SpiritUtils.effectColor(definition.getType()));

        PersistentDataAPI.setString(itemMeta, Keys.spiritItemKey, definition.getType().toString());
        PersistentDataAPI.setString(itemMeta, Keys.spiritStateKey, state);
        PersistentDataAPI.setDouble(itemMeta, Keys.spiritProgressKey, 0);

        itemMeta.displayName(Component.text(tierColor + spiritType + " Spirit"));

        itemLore.add(Component.text(""));
        itemLore.add(Component.text(ChatColors.color("&fTier: " + tierColor + definition.getTier())));
        itemLore.add(Component.text(ChatColors.color("&fCurrent State: " + stateColor + state)));
        itemLore.add(Component.text(ChatColors.color("&fUse " + traitInfo.get("name") + "&f: " + traitInfo.get("type"))));
        itemLore.add(Component.text(""));
        itemLore.add(Component.text(ChatColors.color("&fProgress: " + getProgress(0))));

        itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);

        itemMeta.lore(itemLore);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public static String getProgress(double Progress) {
        String base = "¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦";
        int divideAt = (int) (Progress/5);
        return Progress + "% " +  ChatColors.color(ChatColor.GREEN + base.substring(0, divideAt) + ChatColor.GRAY + base.substring(divideAt));
    }

    public static int getTierChance(int Tier) {
        return switch(Tier) {
            default -> 75;
            case 2,3 -> 50;
            case 4 -> 25;
        };
    }

    public static Block getSpawnBlock(Location location) {
        World world = location.getWorld();
        World.Environment dimension = world.getEnvironment();
        int x = new Random().nextInt(17) * (new Random().nextBoolean() ? 1 : -1) + location.getBlockX();
        int z = new Random().nextInt(17) * (new Random().nextBoolean() ? 1 : -1) + location.getBlockZ();
        int y = location.getBlockY();
        if (! (world.getBlockAt(x,y,z).getType() == Material.AIR)) {
            boolean foundAir = false;
            while (!foundAir) {
                y++;
                if (world.getBlockAt(x,y,z).getType() == Material.AIR) {
                    foundAir = true;
                }
            }
        }
        return world.getBlockAt(x,y,z);
    }

    public static List<Entity> getLookingList(Player player){
        List<Entity> entities = new ArrayList<>();
        for(Entity e : player.getNearbyEntities(10, 10, 10)){
            if(e instanceof Allay){
                if(getLookingAt(player, (LivingEntity) e)){
                    entities.add(e);
                }
            }
        }

        return entities;
    }

    public static boolean getLookingAt(Player player, LivingEntity livingEntity){
        Location eye = player.getEyeLocation();
        Vector toEntity = livingEntity.getLocation().toVector().subtract(eye.toVector());
        double dot = toEntity.normalize().dot(eye.getDirection());
        return dot > 0.99D;
    }

    public static Entity spawnProjectile(Player player, Class<? extends Entity> entity, String reason) {
        Location location = player.getLocation();
        float yaw = location.getYaw();
        double D = 1;
        double x = -D*Math.sin(yaw*Math.PI/180);
        double z = D*Math.cos(yaw*Math.PI/180);
        Entity projectile = player.getWorld().spawn(location.add(x, 1.162, z), entity);
        projectile.setVelocity(location.getDirection().multiply(2));
        PersistentDataAPI.setString(projectile, Keys.entityKey, reason);
        PersistentDataAPI.setString(projectile, Keys.ownerKey, player.getUniqueId().toString());
        return projectile;
    }

    public static List<Block> getNearbyBlocks(Block block, int radius) {
        List<Block> toReturn = new ArrayList<>();
        for (int x = -radius; x < radius+1; x++) {
            for (int y = -radius; y < radius+1; y++) {
                for (int z = -radius; z < radius+1; z++) {
                    Block relative = block.getRelative(x,y,z);
                    toReturn.add(relative);
                }
            }
        }
        return toReturn;
    }

    public static ItemStack getFilledSpiritBook(SpiritDefinition definition, int knowledgeLevel) {
        ItemStack filledBook = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) filledBook.getItemMeta();
        ChatColor tierColor = tierColor(definition.getTier());
        String spiritName = ChatUtils.humanize(definition.getType().name()) + " Spirit";

        bookMeta.addPages(Component.text("Example Book Test (Left for Polishing Stage)"));
        bookMeta.setAuthor(ChatColors.color(tierColor + spiritName));
        bookMeta.setTitle(spiritName);
        filledBook.setItemMeta(bookMeta);
        return filledBook;
    }
}
