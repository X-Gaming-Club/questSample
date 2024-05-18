package org.xgaming.questsample;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.xgaming.questsample.Command.quest;
import org.xgaming.questsample.Command.xkey;
import org.xgaming.questsample.Quest.QuestEvents;

import java.io.File;
import java.util.logging.Level;

public final class QuestSample extends JavaPlugin {
    private static Plugin plugin;
    @Override
    public void onEnable() {
        plugin = this;

        getCommand("xquest").setExecutor(new quest(plugin));
        getCommand("xkey").setExecutor(new xkey(plugin));
        getServer().getPluginManager().registerEvents(new QuestEvents(),plugin);
        copyDefaultConfig(plugin,"config.yml");

        File file = new File(plugin.getDataFolder(),"config.yml");
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        if(!configuration.contains("key"))
        {
            getLogger().log(Level.SEVERE,"API KEY NOT loaded, get it from https://www.xgaming.club/quest-generation-api");
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Plugin getPlugin() {
        return plugin;
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
