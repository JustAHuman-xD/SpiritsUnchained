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

    //There are 3 pieces of Knowledge you can learn about a Spirit
    //1: What it needs to Pass On, Unlocked by Catching the Spirit
    //2: What its relations with other Spirits Area, Unlocked with a Spirit Book
    //3: What Effect it will have on You when in your Inventory, Unlocked by bringing its Friendship Level to the Friendly

    //Adds a Piece of Knowledge
    @ParametersAreNonnullByDefault
    public static void learnKnowledgePiece(Player player, EntityType type, int knowledgeType) {
        knowledgeType = knowledgeType - 1;
        final FileConfiguration playerData = configManager.getPlayerData();
        final String currentKnowledge = playerData.getString(player.getUniqueId()+"."+type, "ABC");
        final String newKnowledge = currentKnowledge.replace(currentKnowledge.charAt(knowledgeType), 'Y');
        playerData.set(player.getUniqueId()+"."+type, newKnowledge);
        if (!currentKnowledge.equals(newKnowledge)) {
            player.sendMessage("You have gained new knowledge about your " + ChatUtils.humanize(type.name()) + " Spirit!");
        }
    }


    //Checks if the current players Knowledge Level is higher or equal to the Provided
    @ParametersAreNonnullByDefault
    public static boolean hasKnowledgePiece(Player player, EntityType type, int knowledgeType) {
        final FileConfiguration playerData = configManager.getPlayerData();
        final String currentKnowledge = playerData.getString(player.getUniqueId()+"."+type, "NNN");
        return currentKnowledge.charAt(knowledgeType) == 'Y';
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
