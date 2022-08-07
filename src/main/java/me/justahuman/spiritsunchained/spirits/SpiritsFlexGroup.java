package me.justahuman.spiritsunchained.spirits;

import io.github.thebusybiscuit.slimefun4.api.items.groups.FlexItemGroup;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuide;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideMode;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.common.ChatColors;
import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.github.bakedlibs.dough.items.CustomItemStack;
import me.justahuman.spiritsunchained.Utils.SpiritUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;

import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.justahuman.spiritsunchained.Utils.PlayerUtils;
import me.justahuman.spiritsunchained.slimefun.Groups;

import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SpiritsFlexGroup extends FlexItemGroup {

    private static final int GUIDE_BACK = 1;
    private static final int PAGE_PREVIOUS = 46;
    private static final int PAGE_NEXT = 52;
    private static final int PAGE_SIZE = 36;

    private static final int[] HEADER = new int[]{
            0, 1, 2, 3, 4, 5, 6, 7, 8
    };
    private static final int[] FOOTER = new int[]{
            45, 46, 47, 48, 49, 50, 51, 52, 53
    };

    private static final int SPIRIT_SLOT = 22;
    private static final int GOAL_SLOT = 30;
    private static final int RELATIONS_SLOT = 32;
    private static final int TRAIT_SLOT = 40;

    private static final ItemStack notEnoughKnowledge = new CustomItemStack(
            Material.WRITABLE_BOOK,
            "&cNot Enough Knowledge!",
            "&7Get &bKnowledge &7by:",
            "&7   - Using a Spirit Book (Levels 1-2)",
            "&7   - Getting the Spirit to the Friendly State! (Level 3)"
    );

    @ParametersAreNonnullByDefault
    public SpiritsFlexGroup(NamespacedKey key, ItemStack item) {
        super(key, item);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean isVisible(Player player, PlayerProfile playerProfile, SlimefunGuideMode guideMode) {
        return true;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void open(Player player, PlayerProfile profile, SlimefunGuideMode mode) {
        final ChestMenu menu = new ChestMenu("&aSpirits Unchained");

        for (int slot : HEADER) { menu.addItem(slot, ChestMenuUtils.getBackground(), (player1, i1, itemStack, clickAction) -> false);}
        for (int slot : FOOTER) { menu.addItem(slot, ChestMenuUtils.getBackground(), (player1, i1, itemStack, clickAction) -> false);}

        menu.setEmptySlotsClickable(false);
        prepare(player, profile, mode, menu, 1);
        menu.open(player);
    }

    @ParametersAreNonnullByDefault
    private void prepare(Player player, PlayerProfile profile, SlimefunGuideMode mode, ChestMenu menu, int page) {
        final List<SpiritDefinition> spiritList = new ArrayList<>(SpiritsUnchained.getSpiritsManager().getSpiritMap().values());
        final int spiritCount = SpiritsUnchained.getSpiritsManager().getSpiritMap().size();
        final int totalPages = (int) Math.ceil(spiritCount / (double) PAGE_SIZE);
        final int start = (page - 1) * PAGE_SIZE;
        final int end = Math.min(start + PAGE_SIZE, spiritCount);

        spiritList.sort(Comparator.comparing(definition -> definition.getType().name()));
        spiritList.sort(Comparator.comparing(SpiritDefinition::getTier));
        final List<SpiritDefinition> spiritSubList = spiritList.subList(start, end);

        pageControls(player, profile, mode, menu, page, totalPages);

        menu.replaceExistingItem(GUIDE_BACK, ChestMenuUtils.getBackButton(player, Slimefun.getLocalization().getMessage("guide.back.guide")));
        menu.addMenuClickHandler(GUIDE_BACK, (player1, slot, itemStack, clickAction) -> {
            SlimefunGuide.openItemGroup(profile, Groups.SU_MAIN_GROUP, mode, 1);
            return false;
        });

        for (int i = 0; i < 36; i++) {
            final int slot = i + 9;

            if (i + 1 <= spiritSubList.size()) {
                final SpiritDefinition definition = spiritSubList.get(i);
                menu.replaceExistingItem(slot, getSpiritMenuItem(definition));
                menu.addMenuClickHandler(slot, (player1, i1, itemStack1, clickAction) -> {
                    displayDefinition(player1, profile, mode, menu, page, definition);
                    return false;
                });
            } else {
                menu.replaceExistingItem(slot, null);
                menu.addMenuClickHandler(slot, (player1, i1, itemStack1, clickAction) -> false);
            }
        }
    }

    private void pageControls(Player player, PlayerProfile profile, SlimefunGuideMode mode, ChestMenu menu, int page, int totalPages) {
        for (int slot : FOOTER) {
            menu.replaceExistingItem(slot, ChestMenuUtils.getBackground());
            menu.addMenuClickHandler(slot, ((player1, i, itemStack, clickAction) -> false));
        }

        menu.replaceExistingItem(PAGE_PREVIOUS, ChestMenuUtils.getPreviousButton(player, page, totalPages));
        menu.addMenuClickHandler(PAGE_PREVIOUS, (player1, slot, itemStack, clickAction) -> {
            final int previousPage = page - 1;
            if (previousPage >= 1) {
                prepare(player1, profile, mode, menu, previousPage);
            }
            return false;
        });

        menu.replaceExistingItem(PAGE_NEXT, ChestMenuUtils.getNextButton(player, page, totalPages));
        menu.addMenuClickHandler(PAGE_NEXT, (player1, slot, itemStack, clickAction) -> {
            final int nextPage = page + 1;
            if (nextPage <= totalPages) {
                prepare(player1, profile, mode, menu, nextPage);
            }
            return false;
        });
    }

    @ParametersAreNonnullByDefault
    private void displayDefinition(Player player, PlayerProfile profile, SlimefunGuideMode mode, ChestMenu menu, int returnPage, SpiritDefinition definition) {
        EntityType entityType = definition.getType();

        // Back Button
        menu.replaceExistingItem(GUIDE_BACK, ChestMenuUtils.getBackButton(player, Slimefun.getLocalization().getMessage("guide.back.guide")));
        menu.addMenuClickHandler(GUIDE_BACK, (player1, slot, itemStack, clickAction) -> {
            prepare(player1, profile, mode, menu, returnPage);
            return false;
        });

        clearDisplay(menu);

        //Spirit Type
        menu.replaceExistingItem(SPIRIT_SLOT, getSpiritMenuItem(definition));

        //Pass On Task
        if (PlayerUtils.hasKnowledgeLevel(player, entityType, 1) || mode == SlimefunGuideMode.CHEAT_MODE) {
            menu.replaceExistingItem(GOAL_SLOT, definition.getGoal().getType());
        } else {
            menu.replaceExistingItem(GOAL_SLOT, notEnoughKnowledge);
        }

        //Relations
        if (PlayerUtils.hasKnowledgeLevel(player, entityType, 2) || mode == SlimefunGuideMode.CHEAT_MODE) {
            menu.replaceExistingItem(RELATIONS_SLOT, new CustomItemStack(
                    Material.WRITTEN_BOOK,
                    "&aRelations",
                    "",
                    "&7Click to Open"
            ));
        } else {
            menu.replaceExistingItem(RELATIONS_SLOT, notEnoughKnowledge);
        }

        //Trait
        if (PlayerUtils.hasKnowledgeLevel(player, entityType, 3) || mode == SlimefunGuideMode.CHEAT_MODE) {
            menu.replaceExistingItem(TRAIT_SLOT, new CustomItemStack(
                    Material.BARRIER,
                    "&aTrait",
                    "",
                    "&7Not Implemented Yet"
            ));
        } else {
            menu.replaceExistingItem(TRAIT_SLOT, notEnoughKnowledge);
        }
    }

    @ParametersAreNonnullByDefault
    private void displayRelationsTree(Player player, PlayerProfile profile, SlimefunGuideMode mode, ChestMenu menu, int returnPage, SpiritDefinition definition) {
        // Back Button
        menu.replaceExistingItem(GUIDE_BACK, ChestMenuUtils.getBackButton(player, Slimefun.getLocalization().getMessage("guide.back.guide")));
        menu.addMenuClickHandler(GUIDE_BACK, (player1, slot, itemStack, clickAction) -> {
            displayDefinition(player1, profile, mode, menu, returnPage, definition);
            return false;
        });

    }

    @ParametersAreNonnullByDefault
    private void clearDisplay(ChestMenu menu) {
        for (int i = 0; i < 45; i++) {
            final int slot = i + 9;
            menu.replaceExistingItem(slot, null);
            menu.addMenuClickHandler(slot, (player1, i1, itemStack1, clickAction) -> false);
        }
    }

    @ParametersAreNonnullByDefault
    private ItemStack getSpiritMenuItem(SpiritDefinition definition) {
        ItemStack itemStack = new ItemStack(Material.FIREWORK_STAR);
        ItemMeta itemMeta = itemStack.getItemMeta();
        ChatColor chatColor = SpiritUtils.tierColor(definition.getTier());
        String spiritType  = ChatUtils.humanize(definition.getType().name());

        itemMeta.displayName(Component.text(chatColor + spiritType + " Spirit" ));
        ((FireworkEffectMeta) itemMeta).setEffect(SpiritUtils.effectColor(definition.getType()));

        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(ChatColors.color(ChatColor.WHITE + "The Captured Spirit of a " + spiritType)));
        lore.add(Component.text(ChatColors.color(ChatColor.WHITE + "Tier: " + chatColor + definition.getTier())));
        itemMeta.lore(lore);
        PersistentDataAPI.setString(itemMeta, new NamespacedKey(SpiritsUnchained.getInstance(), "spirit_type"), spiritType);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DYE, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS);

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
