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
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;

import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.justahuman.spiritsunchained.Utils.PlayerUtils;
import me.justahuman.spiritsunchained.slimefun.Groups;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.FireworkEffect;
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
    private static final int RELATIONS_SLOT = 33;
    private static final int TRAIT_SLOT = 41;

    private static final ItemStack notEnoughKnowledge = new CustomItemStack(
            Material.WRITABLE_BOOK,
            "&cNot Enough Knowledge!",
            "Get Knowledge by Either",
            "   - Using a Spirit Book (Levels 1-2)",
            "   - Getting the Spirit to the Friendly State! (Level 3)"
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

        spiritList.sort(Comparator.comparing(definition -> definition.getTier()));
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
        // Back Button
        menu.replaceExistingItem(GUIDE_BACK, ChestMenuUtils.getBackButton(player, Slimefun.getLocalization().getMessage("guide.back.guide")));
        menu.addMenuClickHandler(GUIDE_BACK, (player1, slot, itemStack, clickAction) -> {
            prepare(player1, profile, mode, menu, returnPage);
            return false;
        });

        //Spirit Type
        menu.replaceExistingItem(SPIRIT_SLOT, getSpiritMenuItem(definition));

        if (PlayerUtils.hasKnowledgeLevel(player, definition.getType(), 1)) {
            menu.replaceExistingItem(GOAL_SLOT, definition.getGoal().getType());
        } else {
            menu.replaceExistingItem(GOAL_SLOT, notEnoughKnowledge);
        }

        if (PlayerUtils.hasKnowledgeLevel(player, definition.getType(), 2)) {
            menu.replaceExistingItem(RELATIONS_SLOT, new CustomItemStack(
                    Material.OAK_SAPLING,
                    "&aRelations Tree",
                    "",
                    "&7Click to Open"
            ));
        } else {
            menu.replaceExistingItem(RELATIONS_SLOT, notEnoughKnowledge);
        }

        if (PlayerUtils.hasKnowledgeLevel(player, definition.getType(), 3)) {
            menu.replaceExistingItem(TRAIT_SLOT, new CustomItemStack(
                    Material.BARRIER,
                    "&aTrait",
                    "",
                    "&7Not Implemented Yet"
            ));
        } else {
            menu.replaceExistingItem(TRAIT_SLOT, notEnoughKnowledge);
        }


        clearDisplay(menu);

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
        ChatColor chatColor = tierColor(definition.getTier());
        String spiritType  = ChatUtils.humanize(definition.getType().getName());

        itemMeta.setDisplayName(chatColor + spiritType + " Spirit" );
        ((FireworkEffectMeta) itemMeta).setEffect(effectColor(definition.getType()));

        List<String> lore = new ArrayList<>();
        lore.add(ChatColors.color(ChatColor.WHITE + "The Captured Spirit of a " + spiritType));
        lore.add(ChatColors.color(ChatColor.WHITE + "Tier: " + chatColor + definition.getTier()));
        itemMeta.setLore(lore);
        PersistentDataAPI.setString(itemMeta, new NamespacedKey(SpiritsUnchained.getInstance(), "spirit_type"), spiritType);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ChatColor tierColor(int tier) {
        return switch (tier) {
            default -> ChatColor.YELLOW;
            case 2 -> ChatColor.AQUA;
            case 3 -> ChatColor.LIGHT_PURPLE;
            case 4 -> ChatColor.GOLD;
        };
    }

    private FireworkEffect effectColor(EntityType type) {
        return switch (type) {
            default -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(100, 100, 100)).build();
            case AXOLOTL -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(235, 181, 213)).build();
            case BAT -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(64, 53, 41)).build();
            case BEE, PUFFERFISH -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(228, 165, 1)).build();
            case BLAZE -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(231, 167, 1)).build();
            case CAT -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(221, 185, 131)).build();
            case CAVE_SPIDER -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(11, 61, 72)).build();
            case CHICKEN -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(149, 149, 149)).build();
            case COD -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(164, 142, 90)).build();
            case COW -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(63, 50, 35)).build();
            case CREEPER -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(12, 157, 10)).build();
            case DOLPHIN -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(32, 55, 72)).build();
            case DONKEY -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(78, 65, 53)).build();
            case DROWNED -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(132, 223, 199)).build();
            case ELDER_GUARDIAN -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(193, 191, 174)).build();
            case ENDERMAN, ENDERMITE, ENDER_DRAGON -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(20, 20, 20)).build();
            case EVOKER -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(138, 144, 144)).build();
            case FOX -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(197, 169, 147)).build();
            case FROG -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(195, 109, 64)).build();
            case GHAST -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(234, 234, 234)).build();
            case GLOW_SQUID -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(8, 80, 80)).build();
            case GOAT -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(155, 139, 116)).build();
            case GUARDIAN -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(83, 120, 106)).build();
            case HOGLIN -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(186, 103, 80)).build();
            case HORSE -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(183, 151, 119)).build();
            case HUSK -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(112, 104, 90)).build();
            case LLAMA -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(180, 148, 117)).build();
            case MAGMA_CUBE -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(49, 0, 0)).build();
            case MUSHROOM_COW -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(150, 14, 15)).build();
            case MULE -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(25, 2, 0)).build();
            case OCELOT -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(221, 206, 116)).build();
            case PANDA, SHEEP, TURTLE, IRON_GOLEM, SNOWMAN -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(217, 217, 217)).build();
            case PARROT -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(12, 157, 10)).build();
            case PHANTOM -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(63, 76, 129)).build();
            case PIG -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(225, 155, 152)).build();
            case PIGLIN -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(143, 89, 60)).build();
            case PIGLIN_BRUTE -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(83, 39, 15)).build();
            case PILLAGER -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(78, 44, 51)).build();
            case POLAR_BEAR -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(224, 224, 224)).build();
            case RABBIT -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(143, 89, 60)).build();
            case RAVAGER -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(108, 107, 104)).build();
            case SALMON -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(150, 14, 15)).build();
            case SHULKER -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(137, 95, 137)).build();
            case SILVERFISH -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(102, 102, 102)).build();
            case SKELETON -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(181, 181, 181)).build();
            case SKELETON_HORSE -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(97, 97, 74)).build();
            case SLIME -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(76, 150, 58)).build();
            case SPIDER -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(49, 42, 37)).build();
            case SQUID -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(32, 55, 72)).build();
            case STRAY -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(91, 111, 112)).build();
            case STRIDER -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(146, 49, 51)).build();
            case TADPOLE -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(102, 78, 57)).build();
            case TROPICAL_FISH -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(224, 98, 20)).build();
            case VEX -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(114, 135, 154)).build();
            case VILLAGER -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(81, 56, 48)).build();
            case VINDICATOR -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(140, 145, 145)).build();
            case WANDERING_TRADER -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(65, 92, 141)).build();
            case WARDEN -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(14, 66, 68)).build();
            case WITCH -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(49, 0, 0)).build();
            case WITHER_SKELETON, WITHER -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(19, 19, 19)).build();
            case WOLF -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(202, 198, 198)).build();
            case ZOGLIN -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(186, 103, 80)).build();
            case ZOMBIE -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(0, 164, 164)).build();
            case ZOMBIE_HORSE -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(46, 77, 49)).build();
            case ZOMBIE_VILLAGER -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(81, 56, 48)).build();
            case ZOMBIFIED_PIGLIN -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(219, 138, 138)).build();
        };
    }

}
