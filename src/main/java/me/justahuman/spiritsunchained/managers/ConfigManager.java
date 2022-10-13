package me.justahuman.spiritsunchained.managers;

import lombok.Getter;

import me.justahuman.spiritsunchained.SpiritsUnchained;
import me.justahuman.spiritsunchained.utils.LogUtils;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class ConfigManager {
    private final FileConfiguration spirits;
    private final FileConfiguration traits;
    private final FileConfiguration playerData;
    private final FileConfiguration biomeGroups;
    private final Map<String, List<String>> biomeMap;

    public ConfigManager() {
        this.spirits = loadConfig("spirits.yml", true);
        this.traits = loadConfig("traits.yml", true);
        this.biomeGroups = loadConfig("biome-groups.yml", true);
        this.playerData = loadConfig("player-data.yml", false);

        biomeMap = fillBiomeMap();
    }

    @Nonnull
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private FileConfiguration loadConfig(@Nonnull String name, boolean override) {
        final SpiritsUnchained plugin = SpiritsUnchained.getInstance();
        final File file = new File(plugin.getDataFolder(), name);
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        final FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        try {
            configuration.load(file);
            if (override) {
                overrideConfiguration(configuration, file, name);
            }
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    @ParametersAreNonnullByDefault
    private void overrideConfiguration(FileConfiguration config, File file, String fileName) throws IOException {
        final InputStream inputStream = SpiritsUnchained.getInstance().getResource(fileName);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        final YamlConfiguration defaults = YamlConfiguration.loadConfiguration(reader);
        config.addDefaults(defaults);
        config.options().copyDefaults(true);
        config.save(file);
    }

    public void save() {
        LogUtils.logInfo("Saving Player Data");
        final File file = new File(SpiritsUnchained.getInstance().getDataFolder(), "player-data.yml");
        try {
            playerData.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private Map<String, List<String>> fillBiomeMap() {
        final Map<String, List<String>> finalMap = new HashMap<>();
        for (String group : biomeGroups.getKeys(false)) {
            finalMap.put(group, biomeGroups.getStringList(group));
        }
        return finalMap;
    }
}
