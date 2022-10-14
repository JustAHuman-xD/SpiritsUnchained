package me.justahuman.spiritsunchained.spirits;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.libraries.dough.common.ChatColors;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;

import lombok.Getter;

import me.justahuman.spiritsunchained.SpiritsUnchained;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Goal {

    private final ItemStack displayStack;
    private final String goalType;
    private final String requiredType;
    private final ItemStack requiredStack;
    private final int amount;

    public Goal(String goalType, String requiredType, int amount) {
        this.goalType = goalType;
        this.requiredType = requiredType;
        this.amount = amount;
        this.requiredStack = createRequirementStack();
        this.displayStack = createDisplayStack();
    }

    private ItemStack createDisplayStack() {
        final String addition = amount > 1 ? "s" : "";
        final String loreEnd = amount + " " + ChatUtils.humanize(requiredType);
        final String name = "&bPass on Task:";
        final ItemStack kill = new CustomItemStack(
                Material.DIAMOND_SWORD,
                name,
                "",
                "&bType: &7Kill Mob" + addition,
                "&bTask: &7Kill " + loreEnd + addition
        );
        final ItemStack item = new CustomItemStack(
                Material.STICK,
                name,
                "",
                "&bType: &7Give Item" + addition,
                "&bTask: &7Give " + loreEnd
        );
        if (goalType.equals("Item")) {
            try {
                item.setType(Material.valueOf(requiredType));
            } catch(IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        ItemStack slimefunItem = new CustomItemStack(
                Material.SLIME_BALL,
                name,
                "",
                "&bType: &7Give Slimefun Item" + addition,
                "&bTask: &7Give " + loreEnd
        );
        if (goalType.equals("SlimefunItem")) {
            try {
                final ItemStack properSlimefunItem = SpiritsUnchained.getSlimefunItem(requiredType).clone();
                final List<Component> newLore = slimefunItem.getItemMeta().hasLore() ? slimefunItem.lore() : new ArrayList<>();
                final ItemMeta newMeta = properSlimefunItem.getItemMeta();
                newLore.set(2, Component.text(ChatColors.color(ChatColor.AQUA + "Task: " + ChatColor.GRAY + "Give " + properSlimefunItem.getItemMeta().displayName())));
                newMeta.displayName(Component.text(ChatColors.color(ChatColor.AQUA + "Pass On Task:")));
                properSlimefunItem.setItemMeta(newMeta);
                properSlimefunItem.lore(newLore);
                slimefunItem = properSlimefunItem;
            } catch(IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        final ItemStack breed = new CustomItemStack(
                Material.WHEAT,
                "&eBreed Mobs",
                "",
                "&7Breed " + loreEnd + addition
        );
        return switch (goalType) {
            case "Item" -> item;
            case "SlimefunItem" -> slimefunItem;
            case "Breed" -> breed;
            default -> kill;
        };
    }

    private ItemStack createRequirementStack() {
        ItemStack toReturn = new ItemStack(Material.AIR);
        if (goalType.equals("Item")) {
            toReturn = new ItemStack(Material.valueOf(requiredType));
        } else if (goalType.equals("SlimefunItem")) {
            toReturn = SlimefunItem.getById(requiredType).getItem().clone();
        }
        return toReturn;
    }
}
