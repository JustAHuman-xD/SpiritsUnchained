package me.justahuman.spiritsunchained.managers;

import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.justahuman.spiritsunchained.listeners.IdentifyingGlassListener;

public class ListenerManager {
    public ListenerManager() {
        SpiritsUnchained.getPluginManager().registerEvents(new IdentifyingGlassListener(), SpiritsUnchained.getInstance());
    }
}
