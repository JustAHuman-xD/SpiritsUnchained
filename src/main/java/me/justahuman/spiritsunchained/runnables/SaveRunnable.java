package me.justahuman.spiritsunchained.runnables;

import me.justahuman.spiritsunchained.SpiritsUnchained;

import org.bukkit.scheduler.BukkitRunnable;

public class SaveRunnable extends BukkitRunnable {
    @Override
    public void run() {
        SpiritsUnchained.getConfigManager().save();
    }
}
