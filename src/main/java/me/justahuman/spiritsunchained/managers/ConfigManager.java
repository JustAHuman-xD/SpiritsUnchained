package me.justahuman.spiritsunchained.managers;

import io.github.thebusybiscuit.slimefun4.libraries.dough.common.ChatColors;
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
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class ConfigManager {
    private final FileConfiguration spirits;
    private final FileConfiguration traits;
    private final FileConfiguration playerData;
    private final FileConfiguration biomeGroups;
    private final FileConfiguration rewards;
    private final FileConfiguration books;
    private final FileConfiguration language;
    private final Map<String, List<String>> biomeMap;

    public ConfigManager() {
        // Add missing config entries
        final SpiritsUnchained instance = SpiritsUnchained.getInstance();
        YamlConfiguration config = YamlConfiguration.loadConfiguration(new InputStreamReader(instance.getResource("config.yml")));
        for (Map.Entry<String, Object> entry : config.getValues(true).entrySet()) {
            instance.getConfig().addDefault(entry.getKey(), entry.getValue());
        }
        instance.getConfig().options().copyDefaults(true);
        instance.saveConfig();

        this.spirits = loadConfig("spirits.yml", true);
        this.traits = loadConfig("traits.yml", true);
        this.biomeGroups = loadConfig("biome-groups.yml", true);
        this.playerData = loadConfig("player-data.yml", false);
        this.rewards = loadConfig("rewards.yml",true);
        this.books = loadConfig("books.yml", true);
        this.language = loadConfig("language.yml", true);

        this.biomeMap = fillBiomeMap();
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

        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);

        try {
            configuration = YamlConfiguration.loadConfiguration(new BufferedReader(new FileReader(file, StandardCharsets.UTF_8)));
            configuration.load(new BufferedReader(new FileReader(file, StandardCharsets.UTF_8)));
            if (override) {
                overrideConfiguration(configuration, file, name);
            }
            configuration = YamlConfiguration.loadConfiguration(new BufferedReader(new FileReader(file, StandardCharsets.UTF_8)));
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        return configuration;
    }

    @ParametersAreNonnullByDefault
    private void overrideConfiguration(FileConfiguration config, File file, String fileName) throws IOException {
        final InputStream inputStream = SpiritsUnchained.getInstance().getResource(fileName);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
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

    public String getTranslation(String path) {
        return ChatColors.color(this.language.getString(path, "Invalid Path!"));
    }

    public List<String> getTranslationList(String path) {
        return colorList(language.getStringList(path));
    }

    public static List<String> colorList(List<String> toColor) {
        final List<String> toReturn = new ArrayList<>();
        for (String line : toColor) {
            toReturn.add(ChatColors.color(line));
        }
        return toReturn;
    }
}
