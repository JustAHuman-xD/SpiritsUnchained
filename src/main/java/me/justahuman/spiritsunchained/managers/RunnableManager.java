package me.justahuman.spiritsunchained.managers;

import lombok.Getter;

import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.justahuman.spiritsunchained.runnables.PassivesRunnable;
import me.justahuman.spiritsunchained.runnables.SaveRunnable;

public class RunnableManager {
    @Getter
    public final SaveRunnable saveRunnable;

    @Getter
    public final PassivesRunnable passivesRunnable;

    public RunnableManager() {
        SpiritsUnchained spiritsUnchained = SpiritsUnchained.getInstance();

        this.saveRunnable = new SaveRunnable();
        this.saveRunnable.runTaskTimer(spiritsUnchained,1, 18000);

        this.passivesRunnable = new PassivesRunnable();
        this.passivesRunnable.runTaskTimer(spiritsUnchained, 1, 10*20);
    }


}
