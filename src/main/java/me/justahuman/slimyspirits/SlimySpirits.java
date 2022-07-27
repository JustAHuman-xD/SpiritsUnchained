package me.justahuman.slimyspirits;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.libraries.dough.updater.GitHubBuildsUpdater;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.groups.NestedItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.groups.SubItemGroup;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.skins.PlayerHead;
import io.github.thebusybiscuit.slimefun4.libraries.dough.skins.PlayerSkin;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.NamespacedKey;

import org.checkerframework.checker.nullness.qual.NonNull;

public class SlimySpirits extends JavaPlugin implements SlimefunAddon {

    private static SlimySpirits instance;

    private NestedItemGroup nestedItemGroup;
    private ItemGroup mainItemGroup;
    private ItemGroup toolItemGroup;
    private ItemGroup machineItemGroup;
    @Override
    public void onEnable() {
        instance = this;


        getLogger().info("========================================");
        getLogger().info("         SlimySpirts - By JustAHuman    ");
        getLogger().info("========================================");

        saveDefaultConfig();

        nestedItemGroup = new NestedItemGroup(new NamespacedKey(this, "parent_category"), new CustomItemStack(PlayerHead.getItemStack(PlayerSkin.fromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGZhMTMwMzJmYTkzOWYxODRkYWE2YTRlMTFlNmYzYTkxM2U0OGYyNTA0OTgxNjVjNTY2NWNjZjQ5YzcyYTE0MCJ9fX0=")), "&aSlimy Spirits"));
        mainItemGroup = new SubItemGroup(new NamespacedKey(this, "resources"), nestedItemGroup, new CustomItemStack(Material.PHANTOM_MEMBRANE, "&aCrafting Materials"));
        toolItemGroup = new SubItemGroup(new NamespacedKey(this, "tools"), nestedItemGroup, new CustomItemStack(Material.COBWEB, "&aTools"));
        machineItemGroup = new SubItemGroup(new NamespacedKey(this, "machines"), nestedItemGroup, new CustomItemStack(Material.FURNACE, "&aMachines"));

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