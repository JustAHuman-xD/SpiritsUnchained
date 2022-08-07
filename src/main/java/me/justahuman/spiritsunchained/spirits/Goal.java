package me.justahuman.spiritsunchained.spirits;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;
import lombok.Getter;
import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.justahuman.spiritsunchained.Utils.LogUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

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
        String loreEnd = amount + " " + ChatUtils.humanize(what) + "(s)";
        ItemStack kill = new CustomItemStack(
                Material.DIAMOND_SWORD,
                "&cKill Mob(s)",
                "",
                "&7Kill " + loreEnd
        );
        ItemStack item = new CustomItemStack(
                Material.STICK,
                "&aGive Item(s)",
                "",
                "&7Give " + loreEnd
        );
        if (type.equals("Item")) {
            try {
                item.setType(Material.valueOf(what));
            } catch(IllegalArgumentException ignored) {}
        }
        ItemStack slimefunItem = new CustomItemStack(
                Material.SLIME_BALL,
                "&aGive Slimefun Item(s)",
                "",
                "&7Give " + loreEnd
        );
        if (type.equals("SlimefunItem")) {
            try {
                item.setType(SpiritsUnchained.getSlimefunItem(what).getType());
            } catch(IllegalArgumentException ignored) {}
        }
        ItemStack breed = new CustomItemStack(
                Material.WHEAT,
                "&eBreed Mob(s)",
                "",
                "&7Breed " + loreEnd
        );
        return switch (type) {
            default -> kill;
            case "Item" -> item;
            case "SlimefunItem" -> slimefunItem;
            case "Breed" -> breed;
        };
    }
}
