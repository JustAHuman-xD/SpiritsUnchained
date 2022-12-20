package me.justahuman.spiritsunchained.runnables;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
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
        //Winged Creature
        if (SpiritUtils.useSpiritItem(player, EntityType.PARROT, null)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 61*20, 0, true));
        }
        //Iron Defense
        if (SpiritUtils.useSpiritItem(player, EntityType.IRON_GOLEM, null)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 61*20, 0, true));
        }
        //Dolphin Swimmer
        final Block headBlock = player.getLocation().getBlock().getRelative(BlockFace.UP);
        if (headBlock.isLiquid() && player.isSwimming() && SpiritUtils.useSpiritItem(player, EntityType.DOLPHIN, null)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 61*20, 2, true));
        }
        //Natural Fisher
        if (headBlock.isLiquid() && player.isSwimming() && SpiritUtils.useSpiritItem(player, EntityType.POLAR_BEAR, null)) {
            PlayerUtils.addOrDropItem(player, new ItemStack(Material.COD));
        }
        //Strange Secrets
        if (player.getInventory().contains(Material.GOLD_INGOT) && SpiritUtils.useSpiritItem(player, EntityType.PIGLIN, null)) {
            final Inventory inventory = player.getInventory();
            for (ItemStack item : inventory.getContents()) {
                if (item != null && item.getType() == Material.GOLD_INGOT) {
                    item.subtract();
                    PlayerUtils.addOrDropItem(player, SlimefunItems.STRANGE_NETHER_GOO.clone());
                }
            }
        }
    }
}
