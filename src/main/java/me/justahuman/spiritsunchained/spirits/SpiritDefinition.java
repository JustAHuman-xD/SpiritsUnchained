package me.justahuman.spiritsunchained.spirits;

import lombok.Getter;

import org.bukkit.entity.EntityType;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class SpiritDefinition {

    private final EntityType type;
    private final int tier;
    private final List<String> states;
    private final List<String> times;
    private final List<String> biomeGroup;
    private final Goal goal;
    private final String trait;
    private final String dimension;
    private final Map<String, List<EntityType>> relations;

    @ParametersAreNonnullByDefault
    public SpiritDefinition(EntityType type, int tier, List<String> states, Goal goal, Map<String, List<EntityType>> relations, String trait, String dimension, List<String> biomeGroup, List<String> times) {
        this.type = type;
        this.tier = tier;
        this.states = states;
        this.goal = goal;
        this.trait = trait;
        this.relations = relations;
        this.dimension = dimension;
        this.biomeGroup = biomeGroup;
        this.times = times;
    }
}
