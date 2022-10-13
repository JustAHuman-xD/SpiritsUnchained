package me.justahuman.spiritsunchained.listeners;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.justahuman.spiritsunchained.spirits.Goal;
import me.justahuman.spiritsunchained.spirits.SpiritDefinition;
import me.justahuman.spiritsunchained.utils.ParticleUtils;
import me.justahuman.spiritsunchained.utils.PlayerUtils;
import me.justahuman.spiritsunchained.utils.SpiritUtils;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class PassOnListeners implements Listener {

    @EventHandler
    public void onItemClick(InventoryClickEvent clickEvent) {
        final ItemStack clickingWith = clickEvent.getCursor();
        final ItemStack clicking = clickEvent.getCurrentItem();
        if (!(clickEvent.getWhoClicked() instanceof Player player) || !(clickEvent.getClick().isLeftClick() || clickEvent.getClick().isRightClick()) || clickingWith == null || clicking == null || ! SpiritUtils.isSpiritItem(clicking)) {
            return;
        }

        final SpiritDefinition definition = SpiritUtils.getSpiritDefinition(clicking);
        final EntityType type = definition.getType();
        final Goal goal = definition.getGoal();
        final ItemStack requirementStack = goal.getRequiredStack();
        final String goalType = goal.getGoalType();
        final int amount = goal.getAmount();
        final int tier = definition.getTier();

        final SlimefunItem clickingWithSF = SlimefunItem.getByItem(clickingWith);
        final SlimefunItem requirementStackSF = SlimefunItem.getByItem(requirementStack);

        if ((goalType.equals("Item") && clickingWithSF == null && clickingWith.getType() == requirementStack.getType()) || (goalType.equals("SlimefunItem") && clickingWithSF != null && clickingWithSF == requirementStackSF)) {
            clickingWith.subtract(amount);
            clicking.subtract();
            clickEvent.getInventory().close();
            ParticleUtils.passOnAnimation(player.getLocation());

            final ItemStack toDrop;
            final FileConfiguration rewards = SpiritsUnchained.getConfigManager().getRewards();
            final String overridePath = "overrides." + type.name();
            final String tierPath = "tiered." + tier;
            final boolean override = rewards.contains(overridePath);
            final List<String> drops = override ? rewards.getStringList(overridePath + ".rewards") : rewards.getStringList(tierPath + ".rewards");
            final String drop = drops.get(new Random().nextInt(drops.size()));
            final SlimefunItem sfDrop = SlimefunItem.getById(drop);
            final int min = override ? rewards.getInt(overridePath + ".min") : rewards.getInt(tierPath + ".min");
            final int max = override ? rewards.getInt(overridePath + ".max") : rewards.getInt(tierPath + ".max");
            toDrop = sfDrop == null ? new ItemStack(Material.valueOf(drop)) : sfDrop.getItem().clone();
            toDrop.setAmount(new Random().nextInt(min, max + 1));
            PlayerUtils.addOrDropItem(player, toDrop);
        }
    }
}
