package me.justahuman.spiritsunchained.listeners;

import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.justahuman.spiritsunchained.implementation.mobs.AbstractCustomMob;
import me.justahuman.spiritsunchained.spirits.SpiritDefinition;
import me.justahuman.spiritsunchained.utils.MiscUtils;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

public class PlayerReleaseSpiritListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerReleaseSpirit(EntityDeathEvent e) {
        LivingEntity killedEntity = e.getEntity();
        Player player = killedEntity.getKiller();

        if (player == null) {return;}

        ItemStack helmetItem = player.getInventory().getHelmet();

        if (helmetItem == null) {return;}

        EntityType type = killedEntity.getType();
        AbstractCustomMob<?> spirit = SpiritsUnchained.getSpiritEntityManager().getCustomClass(null, type + "_SPIRIT");
        if ( spirit == null) {return;}
        SpiritDefinition definition = SpiritsUnchained.getSpiritsManager().getSpiritMap().get(type);
        int chance = ThreadLocalRandom.current().nextInt(1, 100);
        if (MiscUtils.imbuedCheck(helmetItem) && chance <= 10/definition.getTier()) {
            spirit.spawn(killedEntity.getLocation(), killedEntity.getWorld(), "Hostile", null);
        }
    }
}
