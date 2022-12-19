package me.justahuman.spiritsunchained;

import dev.sefiraat.sefilib.slimefun.itemgroup.MenuItem;
import me.justahuman.spiritsunchained.implementation.mobs.Spirit;
import me.justahuman.spiritsunchained.implementation.mobs.UnIdentifiedSpirit;
import me.justahuman.spiritsunchained.managers.SpiritEntityManager;
import me.justahuman.spiritsunchained.slimefun.Groups;
import me.justahuman.spiritsunchained.slimefun.ItemStacks;
import me.justahuman.spiritsunchained.slimefun.Items;
import me.justahuman.spiritsunchained.spirits.SpiritDefinition;
import me.justahuman.spiritsunchained.utils.ParticleUtils;
import me.justahuman.spiritsunchained.utils.SpiritUtils;

public class Setup {

    public static final Setup INSTANCE = new Setup();
    private boolean initialised;

    public void init() {
        if (initialised) {
            return;
        }

        initialised = true;

        final SpiritsUnchained plugin = SpiritsUnchained.getInstance();
        final SpiritEntityManager manager = SpiritsUnchained.getSpiritEntityManager();

        Groups.SU_MAIN_GROUP.register(plugin);
        Groups.SU_MAIN_GROUP.addMenuItem(new MenuItem(ItemStacks.SU_SPIRIT_GUIDEBOOK, (player1, slot, itemStack, clickAction) -> {
            player1.openBook(SpiritUtils.getFilledBook("GUIDE_BOOK"));
            return false;
        }));
        Groups.SU_MAIN_GROUP.addItemGroup(Groups.SU_RESOURCES);
        Groups.SU_MAIN_GROUP.addItemGroup(Groups.SU_TOOLS);
        Groups.SU_MAIN_GROUP.addItemGroup(Groups.SU_MACHINES);
        Groups.SU_MAIN_GROUP.addItemGroup(Groups.SU_SPIRITS_GROUP);
        Groups.SU_MAIN_GROUP.addItemGroup(Groups.SU_ALTAR_1);
        Groups.SU_MAIN_GROUP.addItemGroup(Groups.SU_ALTAR_2);
        Groups.SU_MAIN_GROUP.addItemGroup(Groups.SU_ALTAR_3);

        Items.setup(plugin);
        ParticleUtils.setup();

        UnIdentifiedSpirit unIdentifiedSpirit = new UnIdentifiedSpirit();
        unIdentifiedSpirit.register(manager);

        for (SpiritDefinition definition : SpiritsUnchained.getSpiritsManager().getSpiritMap().values()) {
            Spirit spiritEntity = new Spirit(definition.getType().name() + "_SPIRIT", "Spirit", definition.getType());
            spiritEntity.register(SpiritsUnchained.getSpiritEntityManager());
        }
    }
}
