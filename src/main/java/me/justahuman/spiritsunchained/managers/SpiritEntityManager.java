package me.justahuman.spiritsunchained.managers;

import me.justahuman.spiritsunchained.SpiritsUnchained;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
public class SpiritEntityManager implements Listener {

    public SpiritEntityManager() {
        int tickRate = SpiritsUnchained.getInstance().getConfig().getInt("tick-rate", 2);
        if (tickRate > 20) {
            tickRate = 20;
        }
        //SpiritsUnchained.getPluginManager().registerEvents(this, SpiritsUnchained.getInstance());
        //Bukkit.getScheduler().runTaskTimer(SpiritsUnchained.getInstance(), this::tick, tickRate, Math.max(1, tickRate));
    }

    private void tick() {

    }
}
