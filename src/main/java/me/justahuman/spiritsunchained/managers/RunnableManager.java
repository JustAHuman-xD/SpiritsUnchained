package me.justahuman.spiritsunchained.managers;

import lombok.Getter;

import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.justahuman.spiritsunchained.runnables.PassivesRunnable;
import me.justahuman.spiritsunchained.runnables.RelationsAndStateRunnable;
import me.justahuman.spiritsunchained.runnables.SaveRunnable;

public class RunnableManager {
    @Getter
    public final SaveRunnable saveRunnable;
    @Getter
    public final PassivesRunnable passivesRunnable;
    @Getter
    public final RelationsAndStateRunnable relationsAndStateRunnable;

    public RunnableManager() {
        final SpiritsUnchained spiritsUnchained = SpiritsUnchained.getInstance();

        this.saveRunnable = new SaveRunnable();
        this.saveRunnable.runTaskTimer(spiritsUnchained,1, 18000L);

        this.passivesRunnable = new PassivesRunnable();
        this.passivesRunnable.runTaskTimer(spiritsUnchained, 1, 60* 20L);

        this.relationsAndStateRunnable = new RelationsAndStateRunnable();
        this.relationsAndStateRunnable.runTaskTimer(spiritsUnchained, 1, 20L);
    }
}
