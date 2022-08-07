package me.justahuman.spiritsunchained.spirits;

import lombok.Getter;
import org.bukkit.entity.EntityType;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.List;

@Getter
public class SpiritDefinition {

    private final EntityType type;
    private final int tier;
    private final List<String> states;
    private final Goal goal;
    private final HashMap<String, EntityType> relations;

    @ParametersAreNonnullByDefault
    public SpiritDefinition(EntityType type, int tier, List<String> states, Goal goal, HashMap<String, EntityType> relations) {
        this.type = type;
        this.tier = tier;
        this.states = states;
        this.goal = goal;
        this.relations = relations;
    }
}
