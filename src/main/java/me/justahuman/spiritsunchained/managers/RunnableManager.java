package me.justahuman.spiritsunchained.managers;

import lombok.Getter;

import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.justahuman.spiritsunchained.runnables.SaveRunnable;

public class RunnableManager {
    @Getter
    public final SaveRunnable saveRunnable;

    public RunnableManager() {
        SpiritsUnchained spiritsUnchained = SpiritsUnchained.getInstance();

        this.saveRunnable = new SaveRunnable();
        this.saveRunnable.runTaskTimer(spiritsUnchained,1, 18000);
    }


}
