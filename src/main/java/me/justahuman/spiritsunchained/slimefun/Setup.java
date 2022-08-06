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

        final SpiritsUnchained plugin = SpiritsUnchained.getInstance();

        Groups.SU_MAIN_GROUP.register(plugin);
        Groups.SU_MAIN_GROUP.addItemGroup(Groups.SU_RESOURCES);
        Groups.SU_MAIN_GROUP.addItemGroup(Groups.SU_TOOLS);
        Groups.SU_MAIN_GROUP.addItemGroup(Groups.SU_MACHINES);
        Groups.SU_MAIN_GROUP.addItemGroup(Groups.SU_SPIRITS_GROUP);
        Groups.SU_MAIN_GROUP.addItemGroup(Groups.SU_ALTAR_1);
        Groups.SU_MAIN_GROUP.addItemGroup(Groups.SU_ALTAR_2);
        Groups.SU_MAIN_GROUP.addItemGroup(Groups.SU_ALTAR_3);

        Items.setup(plugin);
    }
}
