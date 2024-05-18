package org.xgaming.questsample.Quest;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import org.xgaming.questsample.Display.AlertUtils;
import org.xgaming.questsample.QuestSample;

import java.util.logging.Level;

public class Quest {

    private QuestType questType;
    private Material material;
    private EntityType mob;
    private int amount;
    private int curamount;

    public Quest(QuestType questType, Material material, EntityType mob, int amount)
    {
        this.questType = questType;
        this.material = material;
        this.mob = mob;
        this.amount = amount;
        QuestSample.getPlugin().getLogger().log(Level.INFO,"QuestType " + this.questType );
    }
    public void start(Player player) {
        String questTypeString = (questType == QuestType.COLLECT) ? "Collect" : "Kill";
        String objectiveString = (questType == QuestType.COLLECT) ? material.name() : mob.name();
        String message = questTypeString + " " + amount + " " + objectiveString;

        AlertUtils.getInstance().showScreenAlert("Quest Started", message, 10, 30, 10, player.getUniqueId());
    }
    public void update(Player player, int collectedAmount) {
        curamount += collectedAmount;
        QuestSample.getPlugin().getLogger().log(Level.INFO,"Current Amount: " + curamount);

        if (curamount >= amount) {
            String questTypeString = (questType == QuestType.COLLECT) ? "Collected" : "Killed";
            String objectiveString = (questType == QuestType.COLLECT) ? material.name() : mob.name();
            String completedMessage = "Congratulations! You have " + questTypeString + " " + amount + " " + objectiveString + ".";
            AlertUtils.getInstance().showScreenAlert("Quest Completed", completedMessage, 10, 30, 10, player.getUniqueId());
        }
    }

    public boolean end()
    {
        return curamount >= amount;
    }
}

