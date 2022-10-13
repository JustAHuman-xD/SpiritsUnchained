package me.justahuman.spiritsunchained.managers;

import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.justahuman.spiritsunchained.utils.LogUtils;
import me.justahuman.spiritsunchained.spirits.Goal;
import me.justahuman.spiritsunchained.spirits.SpiritDefinition;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpiritsManager {

    @Getter
    private final Map<EntityType, SpiritDefinition> spiritMap = new HashMap<>();
    @Getter
    private final List<List<EntityType>> tierMaps = new ArrayList<>();

    public SpiritsManager() {
        tierMaps.add(new ArrayList<>());
        tierMaps.add(new ArrayList<>());
        tierMaps.add(new ArrayList<>());
        tierMaps.add(new ArrayList<>());
        fillSpiritMap();
    }

    private void fillSpiritMap() {
        final FileConfiguration spirits = SpiritsUnchained.getConfigManager().getSpirits();
        for (String key : spirits.getKeys(false)) {
            final EntityType type;
            try {
                type = EntityType.valueOf(key);
            } catch (IllegalArgumentException e) {
                LogUtils.logInfo("Not a Valid Entity Type: " + key);
                e.printStackTrace();
                continue;
            }
            final ConfigurationSection spirit = spirits.getConfigurationSection(key);

            if (spirit == null) {
                LogUtils.logInfo("Missing Spirit's Section: " + key);
                continue;
            }

            final int tier = spirit.getInt("Tier");
            final List<String> states = spirit.getStringList("States");
            final List<String> listGoal = spirit.getStringList("Pass On");
            final List<String> biomeGroup = spirit.getStringList("Biomes");
            final List<String> times = spirit.getStringList("Time");
            final String dimension = spirit.getString("Dimension");
            final String trait = spirit.getString("Trait");
            final Goal goal = new Goal(listGoal.get(0), listGoal.get(1), Integer.parseInt(listGoal.get(2)));
            final HashMap<String, List<EntityType>> relations = new HashMap<>();
            final List<EntityType> scare = new ArrayList<>();
            final List<EntityType> afraid = new ArrayList<>();

            final ConfigurationSection relationSection = spirit.getConfigurationSection("Relations");

            if (relationSection != null) {
                for (String relation : relationSection.getKeys(false)) {
                    for (String with : relationSection.getStringList(relation)) {
                        try {
                            if (relation.equals("Scare")) {scare.add(EntityType.valueOf(with));}
                            if (relation.equals("Afraid")) {afraid.add(EntityType.valueOf(with));}
                        } catch(IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            relations.put("Scare", scare);
            relations.put("Afraid", afraid);

            final SpiritDefinition spiritDefinition = new SpiritDefinition(
                    type,
                    tier,
                    states,
                    goal,
                    relations,
                    trait,
                    dimension,
                    biomeGroup,
                    times
            );
            spiritMap.put(type, spiritDefinition);
            tierMaps.get(tier-1).add(type);
        }
        LogUtils.logInfo("Loaded " + spiritMap.size() + " Spirits!");
    }
}
