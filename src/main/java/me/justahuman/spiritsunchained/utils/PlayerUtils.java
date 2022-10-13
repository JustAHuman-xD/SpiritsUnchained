package me.justahuman.spiritsunchained.utils;

import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;

import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.justahuman.spiritsunchained.managers.ConfigManager;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;

public class PlayerUtils {

    static final ConfigManager configManager = SpiritsUnchained.getConfigManager();

    //There are 3 levels of Knowledge about a Spirit (4 including Level: 0)
    //At Level 1 you learn what it needs to Pass On, Unlocked with a Spirit Book
    //At Level 2 you learn what its relations with other Spirits Area, Unlocked with a Spirit Book
    //At Level 3 you learn what Effect it will have on You when in your Inventory, Unlocked by bringing its Friendship Level to the Max, Only After Being Level 2

    @ParametersAreNonnullByDefault
    public static int getKnowledgeLevel(Player player, EntityType type) {
        return configManager.getPlayerData().get(player.getUniqueId() + "." + type) instanceof Integer currentLevel ? currentLevel : 0;
    }

    //Sets the Players Knowledge Level
    @ParametersAreNonnullByDefault
    public static void setKnowledgeLevel(Player player, EntityType type, int setLevel) {
        final FileConfiguration playerData = configManager.getPlayerData();
        final int currentLevel = playerData.getInt(player.getUniqueId()+"."+type);
        playerData.set(player.getUniqueId()+"."+type, (currentLevel == (setLevel -1)) ? setLevel : currentLevel);
        if (getKnowledgeLevel(player, type) > currentLevel) {
            player.sendMessage("You have gained new knowledge about your " + ChatUtils.humanize(type.name()) + " Spirit!");
        }
    }


    //Checks if the current players Knowledge Level is higher or equal to the Provided
    @ParametersAreNonnullByDefault
    public static boolean hasKnowledgeLevel(Player player, EntityType type, int checkLevel) {
        return getKnowledgeLevel(player, type) >= checkLevel;
    }

    @ParametersAreNonnullByDefault
    public static void addOrDropItem(Player player, ItemStack... itemStacks) {
        final HashMap<Integer, ItemStack> remaining = player.getInventory().addItem(itemStacks);
        if (remaining.size() > 0) {
            for (ItemStack itemStack : remaining.values()) {
                player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
            }
        }
    }
}
