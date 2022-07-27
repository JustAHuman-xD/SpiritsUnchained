package me.justahuman.spiritsunchained.slimefun;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import me.justahuman.spiritsunchained.SpiritsUnchained;

public class Setup {

    public static final Setup INSTANCE = new Setup();
    private final SlimefunAddon plugin = SpiritsUnchained.getInstance();
    private boolean initialised;

    public void init() {
        if (initialised) {
            return;
        }

        initialised = true;

        Items.setup(plugin);
    }
}
