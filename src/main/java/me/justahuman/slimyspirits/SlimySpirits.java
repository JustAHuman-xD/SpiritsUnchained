package me.justahuman.slimyspirits;

import io.github.thebusybiscuit.slimefun4.libraries.dough.updater.GitHubBuildsUpdater;
import org.bukkit.plugin.java.JavaPlugin;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import org.checkerframework.checker.nullness.qual.NonNull;

public class SlimySpirits extends JavaPlugin implements SlimefunAddon {

    private static SlimySpirits instance;
    @Override
    public void onEnable() {
        instance = this;


        getLogger().info("========================================");
        getLogger().info("         SlimySpirts - By JustAHuman    ");
        getLogger().info("========================================");

        saveDefaultConfig();

        if (getConfig().getBoolean("options.auto-update")) {
            GitHubBuildsUpdater updater = new GitHubBuildsUpdater(this, this.getFile(), "JustAHuman-xD/SlimySpirits/master");
            updater.start();
        }
    }

    @NonNull
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Override
    public String getBugTrackerURL() {
        return "https://github.com/JustAHuman-xD/SlimySpirits/issues";
    }

    public static SlimySpirits getInstance() {
        return instance;
    }

    public void onDisable() {
        // Logic for disabling the plugin...
    }
}