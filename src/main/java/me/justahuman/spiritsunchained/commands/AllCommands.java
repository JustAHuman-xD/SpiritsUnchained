package me.justahuman.spiritsunchained.commands;

import io.github.thebusybiscuit.slimefun4.libraries.dough.common.ChatColors;
import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;
import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.justahuman.spiritsunchained.implementation.mobs.AbstractCustomMob;
import me.justahuman.spiritsunchained.utils.Keys;
import me.justahuman.spiritsunchained.utils.PlayerUtils;
import me.justahuman.spiritsunchained.utils.SpiritTraits;
import me.justahuman.spiritsunchained.utils.SpiritUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AllCommands implements TabExecutor {

    final Set<String> spiritTypes = SpiritsUnchained.getSpiritEntityManager().entityMap.keySet();
    final List<String> entityTypes = SpiritUtils.getTypes();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (! (sender instanceof Player player ) || args.length == 0) {
            return false;
        }
        if (useCommand("SummonSpirit", player, 0, 2, args)) {
            return summonSpirit(args[1], player, args.length >= 3 ? args[2] : "COW");
        }
        else if (useCommand("GiveSpirit", player, 0, 2, args)) {
            return giveSpirit(player, args[1], args.length >= 3 ? args[2] : "Friendly");
        }
        else if (useCommand("EditItem", player, 0, 2, args)) {
            return editItem(player, args[1], args.length >= 3 ? args[2] : "Blank");
        }
        else if (useCommand("ResetCooldowns", player, 0, 1, args)) {
            return resetCooldown(player, args.length >= 2 ? args[1] : player.getName());
        }
        else {
            return false;
        }
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, Command command, @NotNull String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("spirits")) {
            final List<String> l = new ArrayList<>();
            final Map<String, Integer> add = new HashMap<>();
            if (args.length == 1) {
                add.put("GiveSpirit", 0);
                add.put("SummonSpirit", 0);
                add.put("EditItem", 0);
                add.put("ResetCooldowns", 0);
            }

            else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("SummonSpirit")) {
                    for (String string : spiritTypes) {
                        add.put(string, 1);
                    }
                }
                else if (args[0].equalsIgnoreCase("GiveSpirit")) {
                    for (String type : entityTypes) {
                        add.put(type, 1);
                    }
                }
                else if (args[0].equalsIgnoreCase("EditItem")) {
                    add.put("State", 1);
                    add.put("Progress", 1);
                    add.put("Max", 1);
                }
                else if (args[0].equalsIgnoreCase("ResetCooldowns")) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        add.put(player.getName(), 1);
                    }
                }
            }

            else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("SummonSpirit") && args[1].equalsIgnoreCase("UNIDENTIFIED_SPIRIT")) {
                    for (EntityType type : SpiritsUnchained.getSpiritsManager().getSpiritMap().keySet()) {
                        add.put(String.valueOf(type), 2);
                    }
                }
                else if ((args[0].equalsIgnoreCase("EditItem") && args[1].equalsIgnoreCase("State")) || (args[0].equalsIgnoreCase("GiveSpirit") && entityTypes.contains(args[1]))) {
                    for (String state : SpiritUtils.getStates()) {
                        add.put(state, 2);
                    }
                }
                else if (args[0].equalsIgnoreCase("EditItem") && args[1].equalsIgnoreCase("Progress")) {
                    add.put("1", 2);
                    add.put("10", 2);
                    add.put("25", 2);
                    add.put("50", 2);
                    add.put("75", 2);
                    add.put("100", 2);
                }
            }

            //Change displays based on the current message
            for (Map.Entry<String, Integer> entry : add.entrySet()) {
                final String toAdd = entry.getKey();
                final Integer index = entry.getValue();
                if (toAdd.toLowerCase().contains(args[index].toLowerCase())) {
                    l.add(toAdd);
                }
            }

            return l;
        }
        return Collections.emptyList();
    }

    private boolean useCommand(String command, Player player, int index, int size, String[] args) {
        return args[index].equalsIgnoreCase(command) && args.length >= size && hasPerm(player);
    }

    private boolean hasPerm(Player player) {
        return player.isOp() || player.hasPermission("spiritsunchained.admin");
    }

    private boolean giveSpirit(Player player, String type, String state) {
        final EntityType spiritType;
        try {
            spiritType = EntityType.valueOf(type);
        } catch (IllegalArgumentException | NullPointerException e) {
            return sendError(player, "Not a Valid Spirit Type!");
        }
        PlayerUtils.addOrDropItem(player, SpiritUtils.spiritItem(state, SpiritsUnchained.getSpiritsManager().getSpiritMap().get(spiritType)));
        return true;
    }

    private boolean summonSpirit(String spiritId, Player player, String type) {
        final AbstractCustomMob<?> spirit = SpiritsUnchained.getSpiritEntityManager().getCustomClass(null, spiritId);
        if (spirit == null || ! SpiritUtils.canSpawn()) {
            return sendError(player, "Not a Valid Spirit Type!");
        }
        spirit.spawn(player.getLocation(), player.getWorld(), "Natural", type);
        return true;
    }

    private boolean editItem(Player player, String toChange, String changeValue) {
        final ItemStack item = player.getInventory().getItemInMainHand();
        if (!SpiritUtils.isSpiritItem(item)) {
            return sendError(player, "You are not holding a Spirit Item!");
        }
        final ItemMeta meta = item.getItemMeta();
        if (toChange.equalsIgnoreCase("state")) {
            if (!SpiritUtils.getStates().contains(changeValue)) {
                return sendError(player, "Not a valid Spirit State!");
            }
            PersistentDataAPI.setString(meta, Keys.spiritStateKey, changeValue);
        } else if (toChange.equalsIgnoreCase("progress")) {
            try {
                PersistentDataAPI.setDouble(meta, Keys.spiritProgressKey, Double.parseDouble(changeValue));
            } catch(NullPointerException | NumberFormatException e) {
                return sendError(player, "Not a proper Progress Value!");
            }
        } else if (toChange.equalsIgnoreCase("max")) {
            PersistentDataAPI.setString(meta, Keys.spiritStateKey, "Friendly");
            PersistentDataAPI.setDouble(meta, Keys.spiritProgressKey, 100.0);
        }
        item.setItemMeta(meta);
        SpiritUtils.updateSpiritItemProgress(item, 0);
        return true;
    }

    private boolean resetCooldown(Player sender, String playerName) {
        final Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            return sendError(sender, playerName + " not Online!");
        }
        SpiritTraits.resetCooldown(player);
        return true;
    }

    private boolean sendError(Player player, String message) {
        player.sendMessage(ChatColors.color(ChatColor.RED + message));
        return true;
    }
}
