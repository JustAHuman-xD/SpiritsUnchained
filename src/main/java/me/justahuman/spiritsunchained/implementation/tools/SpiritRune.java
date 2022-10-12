package me.justahuman.spiritsunchained.implementation.tools;


import io.github.bakedlibs.dough.data.persistent.PersistentDataAPI;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemDropHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;

import me.justahuman.spiritsunchained.utils.Keys;

import net.kyori.adventure.text.Component;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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

        final Location l = rune.getLocation();
        final Collection<Entity> entities = l.getWorld().getNearbyEntities(l, RANGE, RANGE, RANGE, this::findCompatibleItem);
        final Optional<Entity> optional = entities.stream().findFirst();

        if (optional.isPresent()) {
            final Item item = (Item) optional.get();
            final ItemStack itemStack = item.getItemStack();
            final ItemStack runeStack = rune.getItemStack();
            if (!itemStack.getType().name().endsWith("HELMET")) {
                p.sendMessage(ChatColor.LIGHT_PURPLE + "The thrown Item is not a Helmet!");
                return;
            }

            if (itemStack.getAmount() == 1 && runeStack.getAmount() == 1) {
                // This lightning is just an effect, it deals no damage.
                l.getWorld().strikeLightningEffect(l);

                Slimefun.runSync(() -> {
                    // Being sure entities are still valid and not picked up or whatsoever.
                    if (rune.isValid() && runeStack.getAmount() == 1 && item.isValid() && itemStack.getAmount() == 1 && !PersistentDataAPI.hasByte(itemStack.getItemMeta(), Keys.imbuedKey)) {

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
        if (item != null && item.getType() != Material.AIR && !PersistentDataAPI.hasByte(item.getItemMeta(), Keys.imbuedKey)) {
            final ItemMeta meta = item.getItemMeta();
            PersistentDataAPI.setByte(meta, Keys.imbuedKey, (byte) 2);
            final List<Component> lore = meta.hasLore() ? meta.lore() : new ArrayList<>();
            lore.add(Component.text(IMBUED_LORE));
            meta.lore(lore);
            item.setItemMeta(meta);
        }
    }

    private boolean findCompatibleItem(Entity entity) {
        if (entity instanceof Item item) {
            return !PersistentDataAPI.hasByte(item.getItemStack().getItemMeta(), Keys.imbuedKey);
        }
        return false;
    }
}
