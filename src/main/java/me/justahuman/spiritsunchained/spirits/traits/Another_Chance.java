package me.justahuman.spiritsunchained.spirits.traits;

import me.justahuman.spiritsunchained.SpiritsUnchained;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Another_Chance {
    public Another_Chance(Player player) {
        ItemStack offhandItem = player.getInventory().getItemInOffHand().clone();
        player.getInventory().setItemInOffHand(new ItemStack(Material.TOTEM_OF_UNDYING));
        Bukkit.getScheduler().runTaskLater(SpiritsUnchained.getInstance(), () -> player.getInventory().setItemInOffHand(offhandItem), 1);
    }
}
