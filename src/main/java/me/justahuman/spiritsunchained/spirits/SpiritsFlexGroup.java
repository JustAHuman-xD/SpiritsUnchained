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
import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;

import me.justahuman.spiritsunchained.slimefun.Groups;

import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

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

    private void pageControls(Player p, PlayerProfile profile, SlimefunGuideMode mode, ChestMenu menu, int page, int totalPages) {
        for (int slot : FOOTER) {
            menu.replaceExistingItem(slot, ChestMenuUtils.getBackground());
            menu.addMenuClickHandler(slot, ((player, i, itemStack, clickAction) -> false));
        }

        menu.replaceExistingItem(PAGE_PREVIOUS, ChestMenuUtils.getPreviousButton(p, page, totalPages));
        menu.addMenuClickHandler(PAGE_PREVIOUS, (player1, slot, itemStack, clickAction) -> {
            final int previousPage = page - 1;
            if (previousPage >= 1) {
                prepare(player1, profile, mode, menu, previousPage);
            }
            return false;
        });

        menu.replaceExistingItem(PAGE_NEXT, ChestMenuUtils.getNextButton(p, page, totalPages));
        menu.addMenuClickHandler(PAGE_NEXT, (player1, slot, itemStack, clickAction) -> {
            final int nextPage = page + 1;
            if (nextPage <= totalPages) {
                prepare(player1, profile, mode, menu, nextPage);
            }
            return false;
        });
    }

    @ParametersAreNonnullByDefault
    private void displayDefinition(Player p, PlayerProfile profile, SlimefunGuideMode mode, ChestMenu menu, int returnPage, SpiritDefinition definition) {
        // Back Button
        menu.replaceExistingItem(GUIDE_BACK, ChestMenuUtils.getBackButton(p, Slimefun.getLocalization().getMessage("guide.back.guide")));
        menu.addMenuClickHandler(GUIDE_BACK, (player1, slot, itemStack, clickAction) -> {
            prepare(player1, profile, mode, menu, returnPage);
            return false;
        });

        clearDisplay(menu);

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
        ((FireworkEffectMeta) itemMeta).setEffect(effectColor(definition.getTier()));

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
            case OCELOT -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(228, 165, 1)).build();
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
            case ENDERMAN, ENDERMITE -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(20, 20, 20)).build();
            case EVOKER -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(138, 144, 144)).build();
            case FOX -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(197, 169, 147)).build();
            case FROG -> FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(195, 109, 64)).build();
            case POLAR_BEAR -> 
        };
    }

}
