package me.justahuman.spiritsunchained.slimefun;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import me.justahuman.spiritsunchained.utils.Keys;
import me.justahuman.spiritsunchained.utils.SpiritUtils;

public class Researches {

    public static void init() {
        addResearch("materials", "materials", 0, 15,
                ItemStacks.SU_INFUSED_MEMBRANE,
                ItemStacks.SU_INFUSED_FEATHER,
                ItemStacks.SU_SOUL_STAINED_GLASS,
                ItemStacks.SU_SPIRIT_BOTTLE,
                ItemStacks.SU_UNIDENTIFIED_SPIRIT,
                ItemStacks.SU_ECTOPLASM
        );
        addResearch("tools", "tools", 1, 20,
                ItemStacks.SU_SPIRIT_LENSES,
                ItemStacks.SU_IDENTIFYING_GLASS,
                ItemStacks.SU_SPIRIT_BOOK,
                ItemStacks.SU_SPIRIT_NET,
                ItemStacks.SU_SPIRIT_RUNE
        );
        addResearch("machines", "machines", 2, 30,
                ItemStacks.SU_ELECTRIC_SPIRIT_CATCHER,
                ItemStacks.SU_ELECTRIC_SPIRIT_WRITER,
                ItemStacks.SU_ELECTRIC_SPIRIT_BOTTLER
        );
        addResearch("altar_1", "altar_1", 3, 10,
                ItemStacks.SU_CHARGED_QUARTZ_I,
                ItemStacks.SU_CHARGED_STAIRS_I,
                ItemStacks.SU_CHARGED_CORE_I
        );
        addResearch("altar_2", "altar_2", 4, 20,
                ItemStacks.SU_CHARGED_QUARTZ_II,
                ItemStacks.SU_CHARGED_STAIRS_II,
                ItemStacks.SU_CHARGED_PILLAR_II,
                ItemStacks.SU_CHARGED_CORE_II
        );
        addResearch("altar_3", "altar_3", 5, 30,
                ItemStacks.SU_CHARGED_QUARTZ_III,
                ItemStacks.SU_SMOOTH_CHARGED_QUARTZ_III,
                ItemStacks.SU_CHARGED_STAIRS_III,
                ItemStacks.SU_SMOOTH_CHARGED_STAIRS_III,
                ItemStacks.SU_CHARGED_PILLAR_III,
                ItemStacks.SU_CHARGED_CORE_III
        );
    }

    public static void addResearch(String key, String path, int id, int cost, SlimefunItemStack... items) {
        new Research(Keys.newKey(key), 123456 + id, SpiritUtils.getTranslation("names.researches." + path), cost).addItems(items).register();
    }

}
