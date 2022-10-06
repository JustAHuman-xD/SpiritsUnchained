package me.justahuman.spiritsunchained.utils;

import me.justahuman.spiritsunchained.SpiritsUnchained;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashSet;
import java.util.Set;

public class PlayerUtils {

    //There are 3 levels of Knowledge about a Spirit (4 including Level: 0)
    //At Level 1 you learn what it needs to Pass On, Unlocked with a Spirit Book
    //At Level 2 you learn what its relations with other Spirits Area, Unlocked with a Spirit Book
    //At Level 3 you learn what Effect it will have on You when in your Inventory, Unlocked by bringing its Friendship Level to the Max, Only After Being Level 2

    //Sets the Players Knowledge Level
    @ParametersAreNonnullByDefault
    public static void setKnowledgeLevel(Player player, EntityType type, int setLevel) {
        FileConfiguration playerData = SpiritsUnchained.getConfigManager().getPlayerData();
        int currentLevel = playerData.getInt(player.getUniqueId()+"."+type);
        playerData.set(player.getUniqueId()+"."+type, (currentLevel == (setLevel -1)) ? setLevel : currentLevel);
    }


    //Checks if the current players Knowledge Level is higher or equal to the Provided
    @ParametersAreNonnullByDefault
    public static boolean hasKnowledgeLevel(Player player, EntityType type, int checkLevel) {
        return SpiritsUnchained.getConfigManager().getPlayerData().get(player.getUniqueId() + "." + type) instanceof Integer currentLevel && currentLevel >= checkLevel;
    }
}
