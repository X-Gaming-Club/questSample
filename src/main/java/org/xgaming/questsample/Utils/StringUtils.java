package org.xgaming.questsample.Utils;

import org.xgaming.xnpc.xnpc;

import java.util.List;

public class StringUtils {
    public static String findMatchingWord(String input, List<String> words) {
        input = input.toLowerCase();
        for (String word : words) {
            if (input.contains(word)) {
                //log
                xnpc.getPlugin().getLogger().info("Word found: " + word);
                return word;
            }
        }
        //log
        xnpc.getPlugin().getLogger().info("Word not found");
        return "";
    }
}
