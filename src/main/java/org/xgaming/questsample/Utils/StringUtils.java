package org.xgaming.questsample.Utils;

import org.xgaming.questsample.QuestSample;

import java.util.List;

public class StringUtils {
    public static String findMatchingWord(String input, List<String> words) {
        input = input.toLowerCase();
        for (String word : words) {
            if (input.contains(word)) {
                //log
                QuestSample.getPlugin().getLogger().info("Word found: " + word);
                return word;
            }
        }
        //log
        QuestSample.getPlugin().getLogger().info("Word not found");
        return "";
    }
}
