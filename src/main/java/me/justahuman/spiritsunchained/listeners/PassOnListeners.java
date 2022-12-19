package me.justahuman.spiritsunchained.listeners;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;

import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.justahuman.spiritsunchained.spirits.Goal;
import me.justahuman.spiritsunchained.spirits.SpiritDefinition;
import me.justahuman.spiritsunchained.utils.Keys;
import me.justahuman.spiritsunchained.utils.ParticleUtils;
import me.justahuman.spiritsunchained.utils.PlayerUtils;
import me.justahuman.spiritsunchained.utils.SpiritUtils;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class PassOnListeners implements Listener {

    @EventHandler
    public void onItemClick(InventoryClickEvent clickEvent) {
        final ItemStack clickingWith = clickEvent.getCursor();
        final ItemStack clicking = clickEvent.getCurrentItem();
        if (! (clickEvent.getWhoClicked() instanceof Player player) || ! (clickEvent.getClick().isLeftClick() || clickEvent.getClick().isRightClick()) || clickingWith == null || ! SpiritUtils.isSpiritItem(clicking) || SpiritUtils.isLocked(clicking)) {
            return;
        }
        final ItemMeta clickingMeta = clicking.getItemMeta();
        final SpiritDefinition definition = SpiritUtils.getSpiritDefinition(clicking);
        final Goal goal = definition.getGoal();
        final ItemStack requirementStack = goal.getRequiredStack();
        final String goalType = goal.getGoalType();
        final int amount = goal.getAmount() - PersistentDataAPI.getInt(clickingMeta, Keys.spiritPassOnKey);


        final SlimefunItem clickingWithSF = SlimefunItem.getByItem(clickingWith);
        final SlimefunItem requirementStackSF = SlimefunItem.getByItem(requirementStack);

        if ((goalType.equals("Item") && clickingWithSF == null && clickingWith.getType() == requirementStack.getType()) || (goalType.equals("SlimefunItem") && clickingWithSF != null && clickingWithSF == requirementStackSF)) {
            final int clickingWithAmount = clickingWith.getAmount();
            clickingWith.setAmount(clickingWithAmount - amount);
            clickEvent.setCancelled(true);
            if (amount > clickingWithAmount) {
                PersistentDataAPI.setInt(clickingMeta, Keys.spiritPassOnKey, clickingWithAmount + PersistentDataAPI.getInt(clickingMeta, Keys.spiritPassOnKey));
                clicking.setItemMeta(clickingMeta);
                SpiritUtils.updateSpiritItemProgress(clicking, 0);
            } else {
                clicking.subtract();
                clickEvent.getInventory().close();
                passOn(player, definition);
            }
        }
    }

    @EventHandler
    public void onBreedEntity(EntityBreedEvent event) {
        final LivingEntity bredEntity = event.getEntity();
        if (!(event.getBreeder() instanceof Player player) || !SpiritsUnchained.getSpiritsManager().getGoalRequirements().containsKey(bredEntity.getType())) {
            return;
        }

        onSpecialInteract(player, bredEntity, "Breed");
    }

    @EventHandler
    public void onEntityKill(EntityDeathEvent event) {
        final LivingEntity killedEntity = event.getEntity();
        final Player player = killedEntity.getKiller();
        if (player == null || !SpiritsUnchained.getSpiritsManager().getGoalRequirements().containsKey(killedEntity.getType())) {
            return;
        }

        onSpecialInteract(player, killedEntity, "Kill");
    }

    private void onSpecialInteract(Player player, LivingEntity entity, String type) {
        for (EntityType goalFor : SpiritsUnchained.getSpiritsManager().getGoalRequirements().get(entity.getType())) {
            final Set<ItemStack> spiritItems = SpiritUtils.getSpiritItems(player, goalFor);
            for (ItemStack spiritItem : spiritItems) {
                if (spiritItem != null && !SpiritUtils.isLocked(spiritItem)) {
                    SpiritDefinition definition = SpiritUtils.getSpiritDefinition(spiritItem);
                    if (!definition.getGoal().getGoalType().equals(type)) {
                        return;
                    }
                    final ItemMeta meta = spiritItem.getItemMeta();
                    final int amount = definition.getGoal().getAmount();
                    final int currentAmount = PersistentDataAPI.getInt(meta, Keys.spiritPassOnKey) + 1;
                    if (amount > currentAmount) {
                        PersistentDataAPI.setInt(meta, Keys.spiritPassOnKey, currentAmount);
                        spiritItem.setItemMeta(meta);
                        SpiritUtils.updateSpiritItemProgress(spiritItem, 0);
                    } else {
                        spiritItem.subtract();
                        passOn(player, definition);
                    }
                    return;
                }
            }
        }
    }

    private void passOn(Player player, SpiritDefinition definition) {
        final int tier = definition.getTier();
        final EntityType type = definition.getType();
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
        ParticleUtils.passOnAnimation(player.getLocation());
        player.sendMessage(SpiritUtils.getTranslation("messages.spirits.pass_on").replace("{tier_color}", String.valueOf(SpiritUtils.tierColor(tier))).replace("{spirit_name}", ChatUtils.humanize(type.name())));
    }
}
