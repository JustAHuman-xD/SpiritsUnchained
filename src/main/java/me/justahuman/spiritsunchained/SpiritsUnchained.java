package me.justahuman.spiritsunchained;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;

import org.bukkit.plugin.java.JavaPlugin;

import org.checkerframework.checker.nullness.qual.NonNull;

public class SpiritsUnchained extends JavaPlugin implements SlimefunAddon {

    private static SpiritsUnchained instance;

    @Override
    public void onEnable() {
        instance = this;


        getLogger().info("========================================");
        getLogger().info("         SpiritsUnchained - By JustAHuman    ");
        getLogger().info("========================================");

        saveDefaultConfig();

        if (getConfig().getBoolean("options.auto-update")) {
            //GitHubBuildsUpdater updater = new GitHubBuildsUpdater(this, this.getFile(), "JustAHuman-xD/SlimySpirits/master");
            //updater.start();
        }
    }

    @NonNull
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Override
    public String getBugTrackerURL() {
        return "https://github.com/JustAHuman-xD/SpiritsUnchained/issues";
    }

    public static SpiritsUnchained getInstance() {
        return instance;
    }

    public void onDisable() {
        // Logic for disabling the plugin...
    }
}