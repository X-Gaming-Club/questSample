package org.xgaming.questsample.External;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import kong.unirest.json.JSONObject;

import java.util.List;
import java.util.logging.Level;

import org.bukkit.plugin.Plugin;

public class Server {
    Plugin plugin;

    public Server(Plugin plugin) {
        this.plugin = plugin;
    }

    public String serverRequest(String message, String url) {
        try {
            plugin.getLogger().info("Requesting: " + url);
            plugin.getLogger().info("Sent : " + message);
            HttpResponse<JsonNode> response = Unirest.post(url)
                    .header("Content-Type", "application/json")
                    .body(message).asJson();

            // Check the response status
            if (response.getStatus() == 200) {
                String reply = response.getBody().toString();
                plugin.getLogger().info("Received :" + reply);
                return reply;
            } else {
                // Log non-200 responses
                plugin.getLogger().log(Level.SEVERE, "Non-200 Response: " + response.getStatus());
                return null;
            }

        } catch (UnirestException e) {
            if (e.getCause() instanceof java.net.SocketTimeoutException) {
                plugin.getLogger().log(Level.SEVERE, "Socket timeout: ");
            } else {
                plugin.getLogger().log(Level.SEVERE, "HTTP request failed: " + e.getMessage());
            }
            return "{}";
        }
    }
    public String sendQuestGenerateRequest(String key, String goal, List<String> fields, int n) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("key", key);
        requestBody.put("fields", fields);
        requestBody.put("goal", goal);
        requestBody.put("n", n);

        try {
            String url = "https://authdev.xgaming.club/xquest/generate";
            return serverRequest(requestBody.toString(), url);

        } catch (Exception e) {
            plugin.getLogger().severe("Error sending quest generation request: " + e.getMessage());
            return null;
        }
    }
}