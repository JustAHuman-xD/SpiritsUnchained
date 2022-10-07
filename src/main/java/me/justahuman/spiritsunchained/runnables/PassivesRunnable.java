package me.justahuman.spiritsunchained.runnables;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import me.justahuman.spiritsunchained.utils.PlayerUtils;
import me.justahuman.spiritsunchained.utils.SpiritUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class PassivesRunnable extends BukkitRunnable {
    @Override
    public void run() {
        for (World world : Bukkit.getWorlds()) {
            for (Player player : world.getPlayers()) {
                checkPassives(player);
            }
        }
    }

    private void checkPassives(Player player) {
        if (SpiritUtils.useSpiritItem(player, EntityType.PARROT)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 11*20, 2, true));
        }
        if (SpiritUtils.useSpiritItem(player, EntityType.IRON_GOLEM)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 11*20, 2, true));
        }
        Block headBlock = player.getLocation().getBlock().getRelative(BlockFace.UP);
        if (headBlock.isLiquid() && player.isSwimming() && SpiritUtils.useSpiritItem(player, EntityType.DOLPHIN)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 11*20, 2, true));
        }
        if (headBlock.isLiquid() && player.isSwimming() && new Random().nextInt(1,101) >= 80 && SpiritUtils.useSpiritItem(player, EntityType.POLAR_BEAR)) {
            PlayerUtils.addOrDropItem(player, new ItemStack(Material.COD));
        }
        if (player.getInventory().contains(Material.GOLD_INGOT) && SpiritUtils.useSpiritItem(player, EntityType.PIGLIN)) {
            Inventory inventory = player.getInventory();
            for (ItemStack item : inventory.getContents()) {
                if (item.getType() == Material.GOLD_INGOT) {
                    item.subtract();
                    PlayerUtils.addOrDropItem(player, SlimefunItem.getById("STRANGE_NETHER_GOO").getItem().clone());
                }
            }
        }
    }
}
