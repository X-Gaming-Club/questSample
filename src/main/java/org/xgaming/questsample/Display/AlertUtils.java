package org.xgaming.questsample.Display;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.xgaming.questsample.QuestSample;

import java.time.Duration;
import java.util.UUID;

public class AlertUtils {
    public static AlertUtils instance;

    public static AlertUtils getInstance() {
        if (instance == null) {
            instance = new AlertUtils();
        }
        return instance;
    }
    public void showScreenAlert(String titleText, String subtitleText, int fadeInTicks, int stayTicks, int fadeOutTicks, UUID playerUUID) {
        Player player = Bukkit.getPlayer(playerUUID);
        if (player == null) {
            return;
        }

        // Convert tick durations to Duration
        Duration fadeIn = Duration.ofMillis(fadeInTicks * 50L);
        Duration stay = Duration.ofMillis(stayTicks * 50L);
        Duration fadeOut = Duration.ofMillis(fadeOutTicks * 50L);

        // Creating title components, handling empty strings
        Component title = titleText.isEmpty() ? Component.empty() : Component.text(titleText);
        Component subtitle = subtitleText.isEmpty() ? Component.empty() : Component.text(subtitleText);

        // Create the Title object
        Title screenTitle = Title.title(title, subtitle, Title.Times.times(fadeIn, stay, fadeOut));

        // Show the title to the player
        player.showTitle(screenTitle);
        player.sendMessage(titleText + " " + subtitleText);
        QuestSample.getPlugin().getLogger().info("Player " + player.getName() + " alert " + " " + titleText + " " + subtitleText + " ");
    }

}
