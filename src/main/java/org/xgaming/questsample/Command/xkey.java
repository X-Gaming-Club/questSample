package org.xgaming.questsample.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class xkey implements CommandExecutor {
    private final Plugin plugin;

    public xkey(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        if (command.getName().equalsIgnoreCase("xkey")) {
            if (args.length != 1) {
                sender.sendMessage(ChatColor.RED + "Usage: /xkey <api-key>");
                return false;
            }

            String apiKey = args[0];
            if (!isValidApiKey(apiKey)) {
                sender.sendMessage(ChatColor.RED + "API key must be an 8-digit alphanumeric value.");
                return false;
            }

            File file = new File(plugin.getDataFolder(),"config.yml");
            FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
            configuration.set("key",apiKey);
            try {
                plugin.getLogger().log(Level.INFO,"config saved");
                configuration.save(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            sender.sendMessage(ChatColor.GREEN + "API key set to: " + apiKey);
            return true;
        }

        return false;
    }

    private boolean isValidApiKey(String apiKey) {
        return apiKey.matches("[a-zA-Z0-9]{8}");
    }
}
