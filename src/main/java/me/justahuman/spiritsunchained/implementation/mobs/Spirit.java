package me.justahuman.spiritsunchained.implementation.mobs;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;
import lombok.Getter;
import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.justahuman.spiritsunchained.slimefun.ItemStacks;
import me.justahuman.spiritsunchained.spirits.SpiritDefinition;
import me.justahuman.spiritsunchained.utils.Keys;
import me.justahuman.spiritsunchained.utils.ParticleUtils;
import me.justahuman.spiritsunchained.utils.PlayerUtils;
import me.justahuman.spiritsunchained.utils.SpiritUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Allay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.ParametersAreNonnullByDefault;

public class Spirit extends AbstractCustomMob<Allay> {

    @Getter
    private static final int particleCount = 4;
    @Getter
    private final SpiritDefinition definition;

    public Spirit(String id, EntityType entityType) {
        super(Allay.class, id);
        this.definition = SpiritsUnchained.getSpiritsManager().getSpiritMap().get(entityType);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onSpawn(Allay allay, String state, String unused) {
        PersistentDataAPI.setLong(allay, Keys.despawnKey, System.currentTimeMillis() + SpiritUtils.random((int) (this.definition.getTier() * 60 * 0.75), this.definition.getTier() * 60 * SpiritUtils.random(1, 3)) * 1000L);
        if (state.equals("Natural")) {
            state = this.definition.getStates().get(SpiritUtils.random(0, this.definition.getStates().size()));
        }
        PersistentDataAPI.setString(allay, Keys.spiritStateKey, state);
        
        for (Player player : allay.getWorld().getPlayers()) {
            if (player.canSee(allay)) {
                player.hideEntity(SpiritsUnchained.getInstance(), allay);
            }
        }
        
        allay.setCollidable(false);
        allay.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000000 * 20, 1, true, false));
        allay.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1000000 * 20, 1, true, false));
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onTick(Allay allay) {
        final Location location = allay.getLocation();
        final String state = PersistentDataAPI.getString(allay, Keys.spiritStateKey);
        ParticleUtils.spawnParticleRadius(location, Particle.SPELL_INSTANT, 0.1, particleCount, "Spirit");
        SpiritUtils.spawnStateParticle(state, location);
        
        if (PersistentDataAPI.hasLong(allay, Keys.despawnKey) && System.currentTimeMillis() >= PersistentDataAPI.getLong(allay, Keys.despawnKey)) {
            ParticleUtils.passOnAnimation(location);
            allay.remove();
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onHit(EntityDamageByEntityEvent event) {
        event.setCancelled(true);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onInteract(PlayerInteractEntityEvent event) {
        final Player player = event.getPlayer();
        final EquipmentSlot hand = event.getHand();
        final Entity entity = event.getRightClicked();
        final EntityType type = this.definition.getType();
        final ItemStack item = player.getInventory().getItem(hand);
        final int tier = this.definition.getTier();

        if (item.getType() == Material.AIR) {
            return;
        }
        
        final SlimefunItem slimefunItem = SlimefunItem.getByItem(item);
        if (slimefunItem != null && slimefunItem.getId().equals(ItemStacks.SU_SPIRIT_NET.getItemId())) {
            if (SpiritUtils.chance(SpiritUtils.getTierChance(tier))) {
                entity.remove();
                ParticleUtils.catchAnimation(entity.getLocation());
                PlayerUtils.addOrDropItem(player, SpiritUtils.spiritItem(PersistentDataAPI.getString(entity, Keys.spiritStateKey), this.definition));
                PlayerUtils.learnKnowledgePiece(player, type, 1);
            } else {
                player.sendMessage(SpiritUtils.getTranslation("messages.spirits.escape"));
                ParticleUtils.breakParticles(entity.getLocation(), item.clone());
            }
            item.subtract();
        } else if (slimefunItem != null && slimefunItem.getId().equals(ItemStacks.SU_SPIRIT_BOOK.getItemId())) {
            if (SpiritUtils.chance(SpiritUtils.getTierChance(tier))) {
                if (!PlayerUtils.hasKnowledgePiece(player, type, 2)) {
                    PlayerUtils.addOrDropItem(player, SpiritUtils.getFilledBook(this.definition));
                    PlayerUtils.learnKnowledgePiece(player, type, 2);
                } else {
                    player.sendMessage(SpiritUtils.getTranslation("messages.spirits.max_knowledge"));
                    return;
                }
            } else {
                player.sendMessage(SpiritUtils.getTranslation("messages.spirits.rip_book"));
                ParticleUtils.breakParticles(entity.getLocation(), item.clone());
            }
            item.subtract();
        } else if (item.getType() == Material.GLASS_BOTTLE && item.getItemMeta().getPersistentDataContainer().isEmpty()) {
            item.subtract();
            entity.remove();
            ParticleUtils.bottleAnimation(entity.getLocation());
            PlayerUtils.addOrDropItem(player, ItemStacks.SU_SPIRIT_BOTTLE);
        }
    }
}
