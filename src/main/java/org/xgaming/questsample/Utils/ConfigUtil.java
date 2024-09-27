package org.xgaming.questsample.Utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class ConfigUtil {
    private File file;
    private FileConfiguration config;

    public ConfigUtil(Plugin plugin, String path){
        String fullPath = plugin.getDataFolder().getAbsolutePath() + "/" + path;
        this.file = new File(fullPath);
        this.config = YamlConfiguration.loadConfiguration(this.file);

        // Debugging: Log the full path
        plugin.getLogger().info("Trying to load configuration at: " + fullPath);
        if (!this.file.exists()) {
            plugin.getLogger().warning("Configuration file does not exist: " + fullPath);
        } else {
            plugin.getLogger().info("Configuration file loaded successfully: " + fullPath);
        }
    }


    public ConfigUtil(String path){
        this.file = new File(path);
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    public boolean save(){
        try {
            this.config.save(this.file);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public File getFile() {
        return this.file;
    }

    public FileConfiguration getConfig() {
        return this.config;
    }

    public static void copyDefaultConfig(Plugin plugin, String resourcePath) {
        File configFile = new File(plugin.getDataFolder(), resourcePath);

        if (!configFile.exists()) {
            plugin.saveResource(resourcePath, false);
            plugin.getLogger().info("Created default config file: " + resourcePath);
        }
        else{
            plugin.getLogger().info("Config file found: " + resourcePath);
        }
    }
}

