package me.justahuman.spiritsunchained.commands;

import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.justahuman.spiritsunchained.implementation.mobs.AbstractCustomMob;
import me.justahuman.spiritsunchained.utils.SpiritUtils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AllCommands implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (! (sender instanceof Player player ) || args.length == 0) {
            return false;
        }
        if (args[0].equalsIgnoreCase("SummonSpirit") && hasPerm(player) && args.length >= 2) {
            String type = "COW";
            if (args.length >= 3) {
                type = args[2];
            }
            return summonSpirit(args[1], player, type);
        } else {
            return false;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("spirits")) {
            List<String> l = new ArrayList<String>();

            if (args.length == 1) {
                l.add("SummonSpirit");
            }

            else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("SummonSpirit")) {
                    l.addAll(SpiritsUnchained.getSpiritEntityManager().EntityMap.keySet());
                }

            }

            else if (args.length == 3) {
                if (args[1].equalsIgnoreCase("UNIDENTIFIED_SPIRIT")) {
                    for (EntityType type : SpiritsUnchained.getSpiritsManager().getSpiritMap().keySet()) {
                        l.add(String.valueOf(type));
                    }
                }
            }

            return l;
        }
        return null;
    }

    private boolean hasPerm(Player player) {
        return player.isOp() || player.hasPermission("spiritsunchained.admin");
    }

    private boolean summonSpirit(String soulId, Player player, String type) {
        AbstractCustomMob<?> spirit = SpiritsUnchained.getSpiritEntityManager().getCustomClass(null, soulId);
        if (spirit == null || ! SpiritUtils.canSpawn()) {
            return false;
        }
        spirit.spawn(player.getLocation(), player.getWorld(), "Natural", type);
        return true;
    }
}
