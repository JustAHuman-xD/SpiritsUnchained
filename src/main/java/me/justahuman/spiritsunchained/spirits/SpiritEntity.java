package me.justahuman.spiritsunchained.spirits;

import io.github.thebusybiscuit.slimefun4.libraries.dough.common.ChatColors;
import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;

import lombok.Getter;

import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.justahuman.spiritsunchained.managers.SpiritEntityManager;

import net.kyori.adventure.text.Component;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Allay;
import org.bukkit.entity.Entity;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpellCastEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.Random;

@Getter
public class SpiritEntity implements Listener {

    private final NamespacedKey key = new NamespacedKey(SpiritsUnchained.getInstance(), "spirit");

    private final SpiritEntityManager spiritEntityManager = SpiritsUnchained.getSpiritEntityManager();
    private final SpiritDefinition definition;
    private final String id;
    private final String name;
    private final String currentState;

    @ParametersAreNonnullByDefault
    public SpiritEntity(String id, String name, SpiritDefinition definition) {
        this.id = id;
        this.name = ChatColors.color(name);
        this.definition = definition;
        this.currentState = definition.getStates().get(new Random().nextInt(definition.getStates().size()));
    }

    public void reveal(@Nonnull Entity mob) {
        mob.setCustomNameVisible(true);
    }
}
