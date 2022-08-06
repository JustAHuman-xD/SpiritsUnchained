package me.justahuman.spiritsunchained.managers;

import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.justahuman.spiritsunchained.spirits.SpiritDefinition;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpiritsManager {

    @Getter
    private final Map<EntityType, SpiritDefinition> spiritMap = new HashMap<>();
    public SpiritsManager() {
        fillSpiritMap();
    }

    private void fillSpiritMap() {
        final FileConfiguration spirits = SpiritsUnchained.getConfigManager().getSpirits();
        for (String key : spirits.getKeys(false)) {
            final EntityType type;
            try {
                type = EntityType.valueOf(key);
            } catch (IllegalArgumentException e) {
                SpiritsUnchained.getInstance().getLogger().info("Not a Valid Entity Type: " + key);
                e.printStackTrace();
                continue;
            }
            final ConfigurationSection spirit = spirits.getConfigurationSection(key);

            if (spirit == null) {
                SpiritsUnchained.getInstance().getLogger().info("Missing Spirit's Section: " + key);
                continue;
            }

            final int tier = spirit.getInt("Tier");
            final List<String> states = spirit.getStringList("States");
            final List<String> goal = spirit.getStringList("Pass On");
            final HashMap<String, EntityType> relations = new HashMap<>();

            final ConfigurationSection relationSection = spirit.getConfigurationSection("Relations");

            if (relationSection != null) {
                for (String relation : relationSection.getKeys(false)) {
                    for (String with : relationSection.getStringList(relation)) {
                        try { //
                            relations.put(relation, EntityType.valueOf(with));
                        } catch(IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            final SpiritDefinition spiritDefinition = new SpiritDefinition(
                    type,
                    tier,
                    states,
                    goal,
                    relations
            );
            spiritMap.put(type, spiritDefinition);
        }
        SpiritsUnchained.getInstance().getLogger().info("Loaded " + spiritMap.size() + " Spirits!");
    }
}
