package me.justahuman.spiritsunchained;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;
import io.github.thebusybiscuit.slimefun4.libraries.dough.updater.GitHubBuildsUpdater;
import lombok.Getter;
import me.justahuman.spiritsunchained.managers.CommandManager;
import me.justahuman.spiritsunchained.slimefun.Researches;
import me.justahuman.spiritsunchained.utils.Keys;
import me.justahuman.spiritsunchained.utils.LogUtils;
import me.justahuman.spiritsunchained.managers.ConfigManager;
import me.justahuman.spiritsunchained.managers.ListenerManager;
import me.justahuman.spiritsunchained.managers.RunnableManager;
import me.justahuman.spiritsunchained.managers.SpiritEntityManager;
import me.justahuman.spiritsunchained.managers.SpiritsManager;
import me.justahuman.spiritsunchained.slimefun.ItemStacks;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.FallingBlock;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class SpiritsUnchained extends JavaPlugin implements SlimefunAddon {

    @Getter
    private static SpiritsUnchained instance;
    @Getter
    private static ListenerManager listenerManager;
    @Getter
    private static SpiritsManager spiritsManager;
    @Getter
    private static SpiritEntityManager spiritEntityManager;
    @Getter
    private static ConfigManager configManager;
    @Getter
    private static RunnableManager runnableManager;

    @Override
    public void onEnable() {
        instance = this;


        getLogger().info("========================================");
        getLogger().info("    SpiritsUnchained - By JustAHuman    ");
        getLogger().info("========================================");

        saveDefaultConfig();

        configManager = new ConfigManager();
        runnableManager = new RunnableManager();
        listenerManager = new ListenerManager();
        spiritsManager = new SpiritsManager();
        spiritEntityManager = new SpiritEntityManager();

        Setup.INSTANCE.init();

        if (getConfig().getBoolean("options.auto-update")) {
            GitHubBuildsUpdater updater = new GitHubBuildsUpdater(this, this.getFile(), "JustAHuman-xD/SpiritsUnchained/master");
            updater.start();
        }

        if (getConfig().getBoolean("options.enable-researches")) {
            Researches.init();
        }

        this.getCommand("spirits").setExecutor(new CommandManager());
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

    @Nonnull
    @ParametersAreNonnullByDefault
    public static SlimefunItemStack getSlimefunItem(String id) {
        try {
            return (SlimefunItemStack) Slimefun.getRegistry().getSlimefunItemIds().get(id).getItem();
        } catch(NullPointerException | ClassCastException e) {
            e.printStackTrace();
            LogUtils.logInfo(id);
            return ItemStacks.SU_ECTOPLASM;
        }
    }

    @Override
    public void onDisable() {
        configManager.save();
        for (World world : Bukkit.getWorlds()) {
            for (FallingBlock fallingBlock : world.getEntitiesByClass(FallingBlock.class)) {
                if (PersistentDataAPI.hasString(fallingBlock, Keys.entityKey)) {
                    fallingBlock.remove();
                }
            }
        }
    }
}