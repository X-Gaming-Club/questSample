package org.xgaming.questsample.Quest;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class QuestManager {
    private static QuestManager instance;
    private static ConcurrentHashMap<UUID, Quest> playerQuests;

    public static QuestManager getInstance() {
        if (instance == null) {
            instance = new QuestManager();
            playerQuests = new ConcurrentHashMap<>();
        }
        return instance;
    }

    public void addQuest(Player player, Material material,int amount) {
        Quest quest = new Quest(QuestType.COLLECT,material,null,amount);
        playerQuests.put(player.getUniqueId(), quest);
        quest.start(player);
    }

    public void addQuest(Player player, EntityType mob, int amount) {
        Quest quest = new Quest(QuestType.DEFEAT,null,mob,amount);
        playerQuests.put(player.getUniqueId(), quest);
        quest.start(player);
    }

    public void removeQuest(UUID playerUUID) {
        playerQuests.remove(playerUUID);
    }

    public boolean hasQuest(UUID playerUUID) {
        return playerQuests.containsKey(playerUUID);
    }
    public Quest getQuest(UUID playerUUID){
        if(hasQuest(playerUUID))
        {
            return playerQuests.get(playerUUID);
        }
        return null;
    }
}
