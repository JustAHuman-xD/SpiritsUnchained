package me.justahuman.spiritsunchained.managers;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.common.ChatColors;
import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;
import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.justahuman.spiritsunchained.implementation.mobs.AbstractCustomMob;
import me.justahuman.spiritsunchained.slimefun.Items;
import me.justahuman.spiritsunchained.utils.Keys;
import me.justahuman.spiritsunchained.utils.ParticleUtils;
import me.justahuman.spiritsunchained.utils.PlayerUtils;
import me.justahuman.spiritsunchained.utils.SpiritTraits;
import me.justahuman.spiritsunchained.utils.SpiritUtils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class CommandManager implements TabExecutor {

    public final Set<String> spiritTypes = SpiritsUnchained.getSpiritEntityManager().entityMap.keySet();
    public final List<String> entityTypes = SpiritUtils.getTypes();
    public final Set<UUID> ghostBlocks = new HashSet<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (! (sender instanceof Player player ) || args.length == 0) {
            return false;
        }
        if (useCommand("TestParticles", player, 2, args)) {
            return testParticles(player, args[1]);
        }
        else if (useCommand("SummonSpirit", player, 2, args)) {
            return summonSpirit(args[1], player, args.length >= 3 ? args[2] : "COW");
        }
        else if (useCommand("GiveSpirit", player, 2, args)) {
            return giveSpirit(player, args[1], args.length >= 3 ? args[2] : "Friendly");
        }
        else if (useCommand("EditItem", player, 2, args)) {
            return editItem(player, args[1], args.length >= 3 ? args[2] : "Blank");
        }
        else if (useCommand("ResetCooldowns", player, 1, args)) {
            return resetCooldown(player, args.length >= 2 ? args[1] : player.getName());
        }
        else if (useCommand("Altar", player, 2, args)) {
            return visualizeAltar(player, args[1]);
        }
        else {
            return false;
        }
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (! (sender instanceof Player player ) || args.length == 0) {
            return new ArrayList<>();
        }
        if (command.getName().equalsIgnoreCase("spirits")) {
            final List<String> l = new ArrayList<>();
            final Map<String, Integer> add = new HashMap<>();
            if (args.length == 1) {
                add.put("Altar", 0);
                add.put("TestParticles", 0);
                add.put("GiveSpirit", 0);
                add.put("SummonSpirit", 0);
                add.put("EditItem", 0);
                add.put("ResetCooldowns", 0);
            }

            else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("TestParticles")) {
                    add.put("Catch", 1);
                    add.put("Bottle", 1);
                    add.put("PassOn", 1);
                }
                else if (args[0].equalsIgnoreCase("Altar")) {
                    add.put("1", 1);
                    add.put("2", 1);
                    add.put("3", 1);
                }
                else if (args[0].equalsIgnoreCase("SummonSpirit")) {
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
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        add.put(onlinePlayer.getName(), 1);
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

            //Change displays based on the current message & Permissions
            for (Map.Entry<String, Integer> entry : add.entrySet()) {
                final String toAdd = entry.getKey();
                final Integer index = entry.getValue();
                if (toAdd.toLowerCase().contains(args[index].toLowerCase()) && (index != 0 || hasPerm(player, toAdd))) {
                    l.add(toAdd);
                }
            }

            return l;
        }
        return Collections.emptyList();
    }

    private boolean useCommand(String command, Player player, int size, String[] args) {
        return args[0].equalsIgnoreCase(command) && args.length >= size && hasPerm(player, command);
    }

    private boolean hasPerm(Player player, String command) {
        return player.isOp() || player.hasPermission("spiritsunchained.*") || player.hasPermission("spiritsunchained." + command.toLowerCase());
    }

    private boolean visualizeAltar(Player player, String altar) {
        final Location location = player.getLocation();
        final World world = player.getWorld();

        if (!(altar.equals("1") || altar.equals("2") || altar.equals("3"))) {
            return sendError(player, "altar.error.altar_tier");
        }

        if (PersistentDataAPI.hasLong(player, Keys.visualizing) && PersistentDataAPI.getLong(player, Keys.visualizing) > System.currentTimeMillis()) {
            return sendError(player, "altar.error.altar_multiple");
        }

        PersistentDataAPI.setLong(player, Keys.visualizing, System.currentTimeMillis() + 30 * 1000L);

        final Map<Vector, SlimefunItemStack> altarMap = switch(altar) {
            case "3" -> Items.getAltar3();
            case "2" -> Items.getAltar2();
            default -> Items.getAltar1();
        };

        player.sendMessage(SpiritUtils.getTranslation("messages.commands.altar.use").replace("{altar_tier}", altar));
        player.sendMessage(SpiritUtils.getTranslation("messages.commands.altar.materials"));

        final Set<SlimefunItemStack> sent = new HashSet<>();
        for (SlimefunItemStack itemStack : altarMap.values()) {
            if (!sent.contains(itemStack)) {
                sent.add(itemStack);
                player.sendMessage(ChatColors.color("&6" + Collections.frequency(altarMap.values(), itemStack) + " &e" + itemStack.getDisplayName()));
            }
        }

        int entryIndex = 0;
        for (Map.Entry<Vector, SlimefunItemStack> entry : altarMap.entrySet()) {
            final int finalEntryIndex = entryIndex;
            Bukkit.getScheduler().runTaskLater(SpiritsUnchained.getInstance(), () -> {
                final Vector changes = entry.getKey();
                final Location relativeLocation = new Location(location.getWorld(), location.getBlockX() + 0.5, location.getBlockY(), location.getBlockZ() + 0.5).add(changes);
                final BlockData blockData = entry.getValue().getType().createBlockData();
                if (blockData instanceof Directional directional) {
                    final BlockFace face;
                    if ((Math.max(Math.abs(changes.getX()), Math.abs(changes.getZ()))) == Math.abs(changes.getX())) {
                        face = changes.getX() > 0 ? BlockFace.WEST : BlockFace.EAST;
                    } else {
                        face = changes.getZ() > 0 ? BlockFace.NORTH : BlockFace.SOUTH;
                    }
                    directional.setFacing(face);
                }
                final FallingBlock fallingBlock = world.spawnFallingBlock(relativeLocation, blockData);
                ghostBlocks.add(fallingBlock.getUniqueId());
                fallingBlock.setVelocity(new Vector(0, 0, 0));
                fallingBlock.setGravity(false);
                fallingBlock.setDropItem(false);
                fallingBlock.setPersistent(true);
                fallingBlock.setInvulnerable(true);
                PersistentDataAPI.setString(fallingBlock, Keys.entityKey, "altar");
                Bukkit.getScheduler().runTaskLater(SpiritsUnchained.getInstance(), () -> {
                    if (fallingBlock != null) {
                        ghostBlocks.remove(fallingBlock.getUniqueId());
                        fallingBlock.remove();
                    }
                }, (30 * 20) - (finalEntryIndex * 5L));
            }, entryIndex * 5L);
            entryIndex++;
        }

        return true;
    }

    private boolean testParticles(Player player, String test) {
        final Location location = player.getLocation();
        switch (test.toLowerCase()) {
            case "catch" -> ParticleUtils.catchAnimation(location);
            case "bottle" -> ParticleUtils.bottleAnimation(location);
            case "passon" -> ParticleUtils.passOnAnimation(location);
            default -> sendError(player, "particles.error");
        }
        return true;
    }

    private boolean giveSpirit(Player player, String type, String state) {
        final EntityType spiritType;
        try {
            spiritType = EntityType.valueOf(type);
        } catch (IllegalArgumentException | NullPointerException e) {
            return sendError(player, "give_spirit.error");
        }
        final ItemStack spirit = SpiritUtils.spiritItem(state, SpiritsUnchained.getSpiritsManager().getSpiritMap().get(spiritType));
        SpiritUtils.updateSpiritItemProgress(spirit, 100);
        PlayerUtils.addOrDropItem(player, SpiritUtils.spiritItem(state, SpiritsUnchained.getSpiritsManager().getSpiritMap().get(spiritType)));
        return true;
    }

    private boolean summonSpirit(String spiritId, Player player, String type) {
        final AbstractCustomMob<?> spirit = SpiritsUnchained.getSpiritEntityManager().getCustomClass(null, spiritId);
        if (spirit == null || ! SpiritUtils.canSpawn()) {
            return sendError(player, "summon_spirit.error");
        }
        spirit.spawn(player.getLocation(), player.getWorld(), "Natural", type);
        return true;
    }

    private boolean editItem(Player player, String toChange, String changeValue) {
        final ItemStack item = player.getInventory().getItemInMainHand();
        if (!SpiritUtils.isSpiritItem(item)) {
            return sendError(player, "edit_item.error.not_holding");
        }
        final ItemMeta meta = item.getItemMeta();
        if (toChange.equalsIgnoreCase("state")) {
            if (!SpiritUtils.getStates().contains(changeValue)) {
                return sendError(player, "edit_item.error.wrong_state");
            }
            PersistentDataAPI.setString(meta, Keys.spiritStateKey, changeValue);
        } else if (toChange.equalsIgnoreCase("progress")) {
            try {
                PersistentDataAPI.setDouble(meta, Keys.spiritProgressKey, Double.parseDouble(changeValue));
            } catch(NullPointerException | NumberFormatException e) {
                return sendError(player, "edit_item.error.wrong_progress");
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
            return sendError(sender, "reset_cooldown.error");
        }
        SpiritTraits.resetCooldown(player);
        return true;
    }

    private boolean sendError(Player player, String path) {
        player.sendMessage(SpiritUtils.getTranslation("messages.commands." + path));
        return true;
    }
}
