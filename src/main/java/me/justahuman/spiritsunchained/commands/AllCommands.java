package me.justahuman.spiritsunchained.commands;

import io.github.thebusybiscuit.slimefun4.libraries.dough.common.ChatColors;
import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;
import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.justahuman.spiritsunchained.implementation.mobs.AbstractCustomMob;
import me.justahuman.spiritsunchained.utils.Keys;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AllCommands implements TabExecutor {

    Set<String> spiritTypes = SpiritsUnchained.getSpiritEntityManager().EntityMap.keySet();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (! (sender instanceof Player player ) || args.length == 0) {
            return false;
        }
        if (useCommand("SummonSpirit", player, 0, 2, args)) {
            String type = "COW";
            if (args.length >= 3) {
                type = args[2];
            }
            return summonSpirit(args[1], player, type);
        }
        else if (useCommand("EditItem", player, 0, 3, args)) {
            return editItem(player, args[1], args[2]);
        }
        else if (useCommand("ResetCooldowns", player, 0, 1, args)) {
            return resetCooldown(player, args.length >= 2 ? args[1] : player.getName());
        }
        else {
            return false;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("spirits")) {
            List<String> l = new ArrayList<String>();
            Map<String, Integer> add = new HashMap<>();
            if (args.length == 1) {
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
                else if (args[0].equalsIgnoreCase("EditItem")) {
                    add.put("State", 1);
                    add.put("Progress", 1);
                }
                else if (args[0].equalsIgnoreCase("ResetCooldowns")) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        add.put(player.getName(), 1);
                    }
                }
            }

            else if (args.length == 3) {
                if (args[1].equalsIgnoreCase("UNIDENTIFIED_SPIRIT")) {
                    for (EntityType type : SpiritsUnchained.getSpiritsManager().getSpiritMap().keySet()) {
                        add.put(String.valueOf(type), 2);
                    }
                }
                else if (args[1].equalsIgnoreCase("State")) {
                    for (String state : SpiritUtils.getStates()) {
                        add.put(state, 2);
                    }
                }
                else if (args[1].equalsIgnoreCase("Progress")) {
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
                String toAdd = entry.getKey();
                Integer index = entry.getValue();
                if (toAdd.toLowerCase().contains(args[index])) {
                    l.add(toAdd);
                }
            }

            return l;
        }
        return null;
    }

    private boolean useCommand(String command, Player player, int index, int size, String[] args) {
        return args[index].equalsIgnoreCase(command) && args.length >= size && hasPerm(player);
    }

    private boolean hasPerm(Player player) {
        return player.isOp() || player.hasPermission("spiritsunchained.admin");
    }

    private boolean summonSpirit(String soulId, Player player, String type) {
        AbstractCustomMob<?> spirit = SpiritsUnchained.getSpiritEntityManager().getCustomClass(null, soulId);
        if (spirit == null || ! SpiritUtils.canSpawn()) {
            return sendError(player, "Not a Valid Spirit Type!");
        }
        spirit.spawn(player.getLocation(), player.getWorld(), "Natural", type);
        return true;
    }

    private boolean editItem(Player player, String toChange, String changeValue) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!SpiritUtils.isSpiritItem(item)) {
            return sendError(player, "You are not holding a Spirit Item!");
        }
        ItemMeta meta = item.getItemMeta();
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

        }
        item.setItemMeta(meta);
        SpiritUtils.updateSpiritItemProgress(item, 0);
        return true;
    }

    private boolean resetCooldown(Player sender, String playerName) {
        Player player = Bukkit.getPlayer(playerName);
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
