package org.xgaming.questsample.Command;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import kong.unirest.json.JSONObject;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.xgaming.questsample.External.Server;
import org.xgaming.questsample.Quest.QuestEvents;
import org.xgaming.questsample.Quest.QuestManager;
import org.xgaming.questsample.Quest.QuestType;
import org.xgaming.questsample.QuestSample;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class quest implements CommandExecutor {
    private final Plugin plugin;

    public quest(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {

            if(!(sender instanceof Player)) return true;
            new BukkitRunnable() {
                @Override
                public void run() {
                    File configFile = new File(plugin.getDataFolder(), "config.yml");
                    if (!configFile.exists()) {
                        sender.sendMessage("Config file not found.");
                        return;
                    }

                    FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
                    String key = config.getString("key");
                    int amount = config.getInt("amount",5);

                    if (key == null || key.isEmpty()) {
                        sender.sendMessage("Key not found in config. Visit xgaming.club/quest-generation-api for a key");
                        return;
                    }

                    List<String> fields = new ArrayList<>();
                    if (args.length < 2) {
                        sender.sendMessage("Usage: /quest <defeat/collect> <goal>");
                        return;
                    }
                    if (args[0].equalsIgnoreCase("defeat")) {
                        fields.add("mobs");
                    } else if (args[0].equalsIgnoreCase("collect")) {
                        fields.add("material");
                    }

                    int n = 1;

                    String goal = String.join(", ", Arrays.copyOfRange(args, 1, args.length));

                    Server server = new Server(plugin);
                    String response = server.sendQuestGenerateRequest(key,goal,fields,n);
                    JSONObject responseJSON = new JSONObject(response);
                    if (response != null) {
                        String field = fields.get(0);
                        String  responsestr =  responseJSON.getString(field);
                        Material material;
                        EntityType mob;
                        QuestType questType;
                        QuestSample.getPlugin().getLogger().log(Level.INFO,"quest field : " + field);
                        if(args[0].equals( "defeat"))
                        {
                            material = null;
                            mob = EntityType.valueOf(responsestr.toUpperCase());
                            questType = QuestType.DEFEAT;
                        }
                        else{
                            mob = null;
                            material = Material.getMaterial(responsestr.toUpperCase());
                            questType = QuestType.COLLECT;
                        }

                        QuestType finalQuestType = questType;
                        Bukkit.getScheduler().runTask(plugin, () -> {
                            if(finalQuestType == QuestType.COLLECT)
                                QuestManager.getInstance().addQuest(((Player) sender),material,amount);
                            else
                                QuestManager.getInstance().addQuest(((Player) sender),mob,amount);

                        });
                    } else {
                        sender.sendMessage("Error processing generate request.");
                    }
                    }
            }.runTaskAsynchronously(plugin);

            return true;
        }
}
