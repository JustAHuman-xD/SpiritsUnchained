package me.justahuman.spiritsunchained.spirits;

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

import java.util.List;

@Getter
public class Goal {

    private final ItemStack type;
    private final String what;
    private final int amount;

    public Goal(String type, String what, int amount) {
        this.type = getTypeStack(type, what, amount);
        this.what = what;
        this.amount = amount;
    }

    private ItemStack getTypeStack(String type, String what, int amount) {
        String addition = "";
        if (amount > 1) {
            addition = "s";
        }
        String loreEnd = amount + " " + ChatUtils.humanize(what);
        ItemStack kill = new CustomItemStack(
                Material.DIAMOND_SWORD,
                "&bPass On Task:",
                "",
                "&bType: &7Kill Mob" + addition,
                "&bTask: &7Kill " + loreEnd + addition
        );
        ItemStack item = new CustomItemStack(
                Material.STICK,
                "&bPass On Task:",
                "",
                "&bType: &7Give Item" + addition,
                "&bTask: &7Give " + loreEnd
        );
        if (type.equals("Item")) {
            try {
                item.setType(Material.valueOf(what));
            } catch(IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        ItemStack slimefunItem = new CustomItemStack(
                Material.SLIME_BALL,
                "&bPass On Task:",
                "",
                "&bType: &7Give Slimefun Item" + addition,
                "&bTask: &7Give " + loreEnd
        );
        if (type.equals("SlimefunItem")) {
            try {
                ItemStack properSlimefunItem = SpiritsUnchained.getSlimefunItem(what).clone();
                List<Component> newLore = slimefunItem.lore();
                ItemMeta newMeta = properSlimefunItem.getItemMeta();
                newLore.set(2, Component.text(ChatColors.color(ChatColor.AQUA + "Task: " + ChatColor.GRAY + "Give " + properSlimefunItem.getItemMeta().displayName())));
                newMeta.displayName(Component.text(ChatColors.color(ChatColor.AQUA + "Pass On Task:")));
                properSlimefunItem.setItemMeta(newMeta);
                properSlimefunItem.lore(newLore);
                slimefunItem = properSlimefunItem;
            } catch(IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        ItemStack breed = new CustomItemStack(
                Material.WHEAT,
                "&eBreed Mobs",
                "",
                "&7Breed " + loreEnd + addition
        );
        return switch (type) {
            default -> kill;
            case "Item" -> item;
            case "SlimefunItem" -> slimefunItem;
            case "Breed" -> breed;
        };
    }
}
