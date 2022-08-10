package me.justahuman.spiritsunchained.spirits;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;

import me.justahuman.spiritsunchained.slimefun.Groups;
import me.justahuman.spiritsunchained.utils.MiscUtils;
import me.justahuman.spiritsunchained.utils.SpiritUtils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class SpiritItem {

    private String state;
    private int progress;
    private SpiritDefinition spiritDefinition;

    public SpiritItem(String state, SpiritDefinition definition) {
        this.state = state;
        this.spiritDefinition = definition;
        this.progress = 0;

        ItemStack itemStack = new ItemStack(Material.FIREWORK_STAR);
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataAPI.setString(itemMeta, MiscUtils.spiritItemKey, String.valueOf(spiritDefinition.getType()));
        ((FireworkEffectMeta) itemMeta).setEffect(SpiritUtils.effectColor(definition.getType()));
    }
}
