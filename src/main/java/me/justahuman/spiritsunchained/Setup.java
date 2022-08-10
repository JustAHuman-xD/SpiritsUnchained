package me.justahuman.spiritsunchained;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;

import me.justahuman.spiritsunchained.implementation.mobs.Spirit;
import me.justahuman.spiritsunchained.implementation.mobs.UnIdentifiedSpirit;
import me.justahuman.spiritsunchained.managers.SpiritEntityManager;
import me.justahuman.spiritsunchained.slimefun.Groups;
import me.justahuman.spiritsunchained.slimefun.Items;
import me.justahuman.spiritsunchained.spirits.SpiritDefinition;

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
        final SpiritEntityManager manager = SpiritsUnchained.getSpiritEntityManager();

        Groups.SU_MAIN_GROUP.register(plugin);
        Groups.SU_MAIN_GROUP.addItemGroup(Groups.SU_RESOURCES);
        Groups.SU_MAIN_GROUP.addItemGroup(Groups.SU_TOOLS);
        Groups.SU_MAIN_GROUP.addItemGroup(Groups.SU_MACHINES);
        Groups.SU_MAIN_GROUP.addItemGroup(Groups.SU_SPIRITS_GROUP);
        Groups.SU_MAIN_GROUP.addItemGroup(Groups.SU_ALTAR_1);
        Groups.SU_MAIN_GROUP.addItemGroup(Groups.SU_ALTAR_2);
        Groups.SU_MAIN_GROUP.addItemGroup(Groups.SU_ALTAR_3);

        Items.setup(plugin);

        UnIdentifiedSpirit unIdentifiedSpirit = new UnIdentifiedSpirit();
        unIdentifiedSpirit.register(manager);

        for (SpiritDefinition definition : SpiritsUnchained.getSpiritsManager().getSpiritMap().values()) {
            Spirit spiritEntity = new Spirit(definition.getType().name() + "_SPIRIT", "Spirit", definition.getType());
            spiritEntity.register(SpiritsUnchained.getSpiritEntityManager());
        }
    }
}
