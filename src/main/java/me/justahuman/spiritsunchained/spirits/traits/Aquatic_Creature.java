package me.justahuman.spiritsunchained.spirits.traits;

import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import me.justahuman.spiritsunchained.spirits.bases.Trait;
import me.justahuman.spiritsunchained.utils.MiscUtils;
import me.justahuman.spiritsunchained.utils.PlayerUtils;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class Aquatic_Creature {
    public Aquatic_Creature(Player player) {
        super();
    }

    protected void Use(Player player) {
        MiscUtils.spawnParticleRadius(player.getLocation().clone().add(0,1,0), Particle.NAUTILUS, 4, 30, false);
        boolean result = PlayerUtils.givePotionEffect(player, PotionEffectType.CONDUIT_POWER, 30*20, 2);
        if (!result) {
            player.sendMessage("Could not give Potion Effect!");
        }
    }
}
