package me.justahuman.spiritsunchained.managers;

import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.justahuman.spiritsunchained.listeners.IdentifyingGlassListener;
import me.justahuman.spiritsunchained.listeners.PlayerReleaseSpiritListener;
import me.justahuman.spiritsunchained.listeners.PlayerToggleImbuedActiveListener;

public class ListenerManager {
    public ListenerManager() {
        SpiritsUnchained.getPluginManager().registerEvents(new IdentifyingGlassListener(), SpiritsUnchained.getInstance());
        SpiritsUnchained.getPluginManager().registerEvents(new PlayerToggleImbuedActiveListener(), SpiritsUnchained.getInstance());
        SpiritsUnchained.getPluginManager().registerEvents(new PlayerReleaseSpiritListener(), SpiritsUnchained.getInstance());
    }
}
