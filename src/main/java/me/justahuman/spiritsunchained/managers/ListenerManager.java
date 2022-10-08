package me.justahuman.spiritsunchained.managers;

import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.justahuman.spiritsunchained.listeners.IdentifyingGlassListener;
import me.justahuman.spiritsunchained.listeners.PlayerReleaseSpiritListener;
import me.justahuman.spiritsunchained.listeners.PlayerClickListener;
import me.justahuman.spiritsunchained.listeners.SpiritItemListeners;
import me.justahuman.spiritsunchained.listeners.TraitListeners;
import me.justahuman.spiritsunchained.utils.SpiritUtils;
import org.bukkit.plugin.PluginManager;

public class ListenerManager {
    public ListenerManager() {
        PluginManager manager = SpiritsUnchained.getPluginManager();
        SpiritsUnchained instance = SpiritsUnchained.getInstance();
        manager.registerEvents(new IdentifyingGlassListener(), instance);
        manager.registerEvents(new PlayerClickListener(), instance);
        manager.registerEvents(new PlayerReleaseSpiritListener(), instance);
        manager.registerEvents(new SpiritItemListeners(), instance);
        manager.registerEvents(new TraitListeners(), instance);
    }
}
