package me.justahuman.spiritsunchained.implementation.tools;


import me.justahuman.spiritsunchained.SpiritsUnchained;

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemDropHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;


import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.Sound;
import org.bukkit.Material;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class SpiritRune extends SimpleSlimefunItem<ItemDropHandler> {

    private static final double RANGE = 1.5;
    private static final NamespacedKey IMBUED_KEY = new NamespacedKey(SpiritsUnchained.getInstance(), "imbued");
    private static final String IMBUED_LORE = ChatColor.LIGHT_PURPLE + "Imbued";

    public SpiritRune(ItemGroup category, SlimefunItemStack item, RecipeType type, ItemStack[] recipe) {
        super(category, item, type, recipe);
    }

    @Nonnull
    @Override
    public ItemDropHandler getItemHandler() {
        return (e, p, item) -> {
            if (isItem(item.getItemStack())) {
                if (canUse(p, true)) {
                    Slimefun.runSync(() -> {
                        try {
                            addSpiritTag(p, item);
                        } catch (Exception x) {
                            error("An Exception occurred while trying to apply an Spirit Rune", x);
                        }
                    }, 20L);
                }

                return true;
            }

            return false;
        };
    }


    private void addSpiritTag(@Nonnull Player p, @Nonnull Item rune) {
        // Being sure the entity is still valid and not picked up or whatsoever.
        if (!rune.isValid()) {
            return;
        }

        Location l = rune.getLocation();
        Collection<Entity> entities = l.getWorld().getNearbyEntities(l, RANGE, RANGE, RANGE, this::findCompatibleItem);
        Optional<Entity> optional = entities.stream().findFirst();

        if (optional.isPresent()) {
            Item item = (Item) optional.get();
            ItemStack itemStack = item.getItemStack();
            ItemStack runeStack = rune.getItemStack();
            if (!itemStack.getType().name().endsWith("HELMET")) {
                p.sendMessage(ChatColor.LIGHT_PURPLE + "The thrown Item is not a Helmet!");
                return;
            }

            if (itemStack.getAmount() == 1 && runeStack.getAmount() == 1) {
                // This lightning is just an effect, it deals no damage.
                l.getWorld().strikeLightningEffect(l);

                Slimefun.runSync(() -> {
                    // Being sure entities are still valid and not picked up or whatsoever.
                    if (rune.isValid() && runeStack.getAmount() == 1 && item.isValid() && itemStack.getAmount() == 1 && !isImbued(itemStack)) {

                        l.getWorld().spawnParticle(Particle.CRIT_MAGIC, l, 1);
                        l.getWorld().playSound(l, Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 1F, 1F);

                        item.remove();
                        rune.remove();

                        setImbued(itemStack,p);
                        l.getWorld().dropItemNaturally(l, itemStack);

                        p.sendMessage(ChatColor.LIGHT_PURPLE + "Your Helmet is now Imbued with the Ability to See Spirits!");
                    }
                }, 10L);
            } else if (itemStack.getAmount() != 1) {
                p.sendMessage(ChatColor.LIGHT_PURPLE + "Your Helmet could not be Imbued!");
            } else if (runeStack.getAmount() != 1) {
                p.sendMessage(ChatColor.LIGHT_PURPLE + "Ony use 1 Rune!");
            }
        }
    }

    public static void setImbued(@Nullable ItemStack item, Player p) {
        if (item != null && item.getType() != Material.AIR) {
            boolean imbuedCheck = isImbued(item);
            ItemMeta meta = item.getItemMeta();
            PersistentDataContainer container = meta.getPersistentDataContainer();
            if (!imbuedCheck) {
                container.set(IMBUED_KEY, PersistentDataType.BYTE, (byte) 1);
                List<Component> lore = meta.hasLore() ? meta.lore() : new ArrayList<>();
                lore.add(Component.text(IMBUED_LORE));
                meta.lore(lore);
                item.setItemMeta(meta);
            }
        }
    }

    public static boolean isImbued(@Nullable ItemStack item) {
        if (item != null && item.getType() != Material.AIR) {
            ItemMeta meta = item.hasItemMeta() ? item.getItemMeta() : null;
            return hasImbuedFlag(meta);
        } else {
            return false;
        }
    }

    private static boolean hasImbuedFlag(@Nullable ItemMeta meta) {
        if (meta != null) {
            PersistentDataContainer container = meta.getPersistentDataContainer();
            return container.has(IMBUED_KEY, PersistentDataType.BYTE);
        }
        return false;
    }

    private boolean findCompatibleItem(Entity entity) {
        if (entity instanceof Item item) {
            return !isImbued(item.getItemStack()) && !isItem(item.getItemStack());
        }
        return false;
    }
}
