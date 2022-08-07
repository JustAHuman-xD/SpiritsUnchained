package me.justahuman.spiritsunchained;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import me.justahuman.spiritsunchained.managers.ConfigManager;
import me.justahuman.spiritsunchained.managers.ListenerManager;
import me.justahuman.spiritsunchained.managers.RunnableManager;
import me.justahuman.spiritsunchained.managers.SpiritsManager;
import me.justahuman.spiritsunchained.slimefun.ItemStacks;
import me.justahuman.spiritsunchained.slimefun.Setup;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;


public class SpiritsUnchained extends JavaPlugin implements SlimefunAddon {

    private static SpiritsUnchained instance;
    private ListenerManager listenerManager;
    private SpiritsManager spiritsManager;
    private ConfigManager configManager;

    private RunnableManager runnableManager;

    public static PluginManager getPluginManager() {
        return instance.getServer().getPluginManager();
    }

    @Override
    public void onEnable() {
        instance = this;


        getLogger().info("========================================");
        getLogger().info("    SpiritsUnchained - By JustAHuman    ");
        getLogger().info("========================================");

        this.configManager = new ConfigManager();
        this.runnableManager = new RunnableManager();
        this.listenerManager = new ListenerManager();
        this.spiritsManager = new SpiritsManager();

        Setup.INSTANCE.init();

        saveDefaultConfig();

        if (getConfig().getBoolean("options.auto-update")) {
            //GitHubBuildsUpdater updater = new GitHubBuildsUpdater(this, this.getFile(), "JustAHuman-xD/SlimySpirits/master");
            //updater.start();
        }
    }

    @Nonnull
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
    public static RunnableManager getRunnableManager() {
        return instance.runnableManager;
    }
    public static ListenerManager getListenerManager() {
        return instance.listenerManager;
    }
    public static SpiritsManager getSpiritsManager() {
        return instance.spiritsManager;
    }
    public static ConfigManager getConfigManager() {
        return instance.configManager;
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    public static SlimefunItemStack getSlimefunItem(String id) {
        try {
            return (SlimefunItemStack) Slimefun.getRegistry().getSlimefunItemIds().get(id).getItem();
        } catch(NullPointerException | ClassCastException e) {
            return ItemStacks.SU_ECTOPLASM;
        }
    }

    public void onDisable() {
        configManager.save();
    }
}