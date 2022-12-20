package me.justahuman.spiritsunchained.spirits;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;

import lombok.Getter;

import me.justahuman.spiritsunchained.SpiritsUnchained;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

@Getter
public class Goal {

    private final ItemStack displayStack;
    private final String goalType;
    private final String requiredType;
    private final ItemStack requiredStack;
    private final int amount;
    private static final ItemStack INVALID_ITEM = new CustomItemStack(
            Material.BARRIER,
            "&aInvalid Item! Check Config"
    );

    public Goal(String goalType, String requiredType, int amount) {
        this.goalType = goalType;
        this.requiredType = requiredType;
        this.amount = amount;
        this.requiredStack = createRequirementStack();
        this.displayStack = createDisplayStack();
    }

    private static String translate(String path) {
        return SpiritsUnchained.getConfigManager().getTranslation("names.spirit_group.goal." + path);
    }

    private ItemStack createDisplayStack() {
        final String addition = amount > 1 ? "s" : "";
        final String loreEnd = amount + " " + ChatUtils.humanize(requiredType);
        final String name = translate("name");
        final ItemStack kill = new CustomItemStack(
                Material.DIAMOND_SWORD,
                name,
                "",
                translate("kill.label") + addition,
                translate("kill.value").replace("{amount_and_mob}", loreEnd)
        );
        final ItemStack item = new CustomItemStack(
                Material.STICK,
                name,
                "",
                translate("item.label") + addition,
                translate("item.value").replace("{amount_and_item}", loreEnd)
        );
        if (goalType.equals("Item")) {
            try {
                item.setType(Material.valueOf(requiredType));
            } catch(IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        ItemStack slimefunItem = new CustomItemStack(
                Material.SLIME_BALL,
                name,
                "",
                translate("slimefun_item.label") + addition,
                translate("slimefun_item.value").replace("{amount_and_item}", loreEnd)
        );
        if (goalType.equals("SlimefunItem")) {
            try {
                final ItemStack properSlimefunItem = SpiritsUnchained.getSlimefunItem(requiredType).clone();
                final List<Component> newLore = slimefunItem.lore();
                final ItemMeta newMeta = properSlimefunItem.getItemMeta();
                newLore.set(2, Component.text(translate("slimefun_item.value").replace("{amount_and_item}", amount + " " + properSlimefunItem.getItemMeta().getDisplayName())));
                newMeta.displayName(Component.text(name));
                properSlimefunItem.setItemMeta(newMeta);
                properSlimefunItem.lore(newLore);
                slimefunItem = properSlimefunItem;
            } catch(IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        final ItemStack breed = new CustomItemStack(
                Material.WHEAT,
                name,
                "",
                translate("breed.label"),
                translate("breed.value").replace("{amount_and_mob}", loreEnd)
        );
        return switch (goalType) {
            case "Item" -> item;
            case "SlimefunItem" -> slimefunItem;
            case "Breed" -> breed;
            default -> kill;
        };
    }

    private ItemStack createRequirementStack() {
        ItemStack toReturn = new ItemStack(Material.AIR);
        if (goalType.equals("Item")) {
            toReturn = new ItemStack(Material.valueOf(requiredType));
        } else if (goalType.equals("SlimefunItem")) {
            toReturn = SlimefunItem.getById(requiredType) != null ? SlimefunItem.getById(requiredType).getItem().clone() : INVALID_ITEM;
        }
        return toReturn;
    }
}
