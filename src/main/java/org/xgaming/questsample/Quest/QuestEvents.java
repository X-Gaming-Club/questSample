package org.xgaming.questsample.Quest;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.xgaming.questsample.QuestSample;

import java.util.logging.Level;

public class QuestEvents implements Listener {

    private final QuestManager questManager;

    public QuestEvents() {
        this.questManager = QuestManager.getInstance();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (questManager.hasQuest(player.getUniqueId())) {
            questManager.removeQuest(player.getUniqueId());
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (questManager.hasQuest(player.getUniqueId())) {
            Quest quest = questManager.getQuest(player.getUniqueId());
            if(quest == null) return;
            if (quest.end()) {
                questManager.removeQuest(player.getUniqueId());
            } else {
                QuestSample.getPlugin().getLogger().log(Level.INFO,"Player killed mob");
                quest.update(player, 1); // Assuming player killed a mob
            }
        }
    }

    @EventHandler
    public void onPlayerItemPickup(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = ((Player) event.getEntity()).getPlayer();
            ItemStack item = event.getItem().getItemStack();
            if (questManager.hasQuest(player.getUniqueId())) {
                Quest quest = questManager.getQuest(player.getUniqueId());
                if (quest == null) return;
                if (quest.end()) {
                    questManager.removeQuest(player.getUniqueId());
                } else {
                    quest.update(player, item.getAmount());
                }
            }
        }
    }
}
