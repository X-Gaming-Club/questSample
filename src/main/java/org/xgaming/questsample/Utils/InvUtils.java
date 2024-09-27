package org.xgaming.questsample.Utils;

import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;

public class InvUtils {
    public static HashMap<String, Integer> getInventory(Player player) {
        HashMap<String, Integer> inventory = new HashMap<>();
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            if(player.getInventory().getItem(i) == null) continue;
            String item = fetchItemName(player.getInventory().getItem(i));
            if(inventory.containsKey(item)) {
                inventory.put(item, inventory.get(item) + player.getInventory().getItem(i).getAmount());
            } else {
                inventory.put(item, player.getInventory().getItem(i).getAmount());
            }
        }
        return inventory;
    }

    public static String fetchItemName(ItemStack item) {
        //check if there is custom name else return item name/type
        if (item.getItemMeta().hasDisplayName()) {
            return item.getItemMeta().getDisplayName();
        } else {
            return item.getType().toString();
        }
    }
    public static int countItems(Player player, Material material) {
        int count = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == material) {
                count += item.getAmount();
            }
        }
        return count;
    }

    public static JSONObject getPlayerEquipmentInfo(Player player) {
        JSONObject equipmentInfo = new JSONObject();
        JSONObject armorInfo = new JSONObject();
        JSONObject weaponInfo = new JSONObject();

        PlayerInventory inventory = player.getInventory();

        // Getting Armor Information
        ItemStack[] armorItems = inventory.getArmorContents();
        String[] armorTypes = {"boots", "leggings", "chestplate", "helmet"};
        for (int i = 0; i < armorItems.length; i++) {
            JSONObject armorPiece = new JSONObject();
            ItemStack item = armorItems[i];
            if (item != null) {
                armorPiece.put("name", item.getType().name());
                armorPiece.put("enchants", getItemEnchants(item));
            } else {
                armorPiece.put("name", "NONE");
                armorPiece.put("enchants", new JSONArray());
            }
            armorInfo.put(armorTypes[i], armorPiece);
        }

        // Getting Weapon Information
        for (ItemStack item : inventory.getContents()) {
            if (item != null && isWeapon(item)) {
                weaponInfo.put("name", item.getType().name());
                weaponInfo.put("enchants", getItemEnchants(item));
                break; // Stop after finding the first weapon
            }
        }

        equipmentInfo.put("armor", armorInfo);
        equipmentInfo.put("weapon", weaponInfo);
        return equipmentInfo;
    }

    private static JSONArray getItemEnchants(ItemStack item) {
        JSONArray enchants = new JSONArray();
        item.getEnchantments().forEach((enchantment, level) -> {
            JSONObject enchantInfo = new JSONObject();
            enchantInfo.put(enchantment.getName(), level);
            enchants.put(enchantInfo);
        });
        return enchants;
    }

    public static int takeAllMaterial(Player player,Material material) {
        PlayerInventory inventory = player.getInventory();
        int count = 0;

        for (ItemStack item : inventory.getContents()) {
            if (item != null && item.getType() == material) {
                count += item.getAmount();
                inventory.remove(item);
            }
        }

        // If you also want to check the player's offhand slot
        ItemStack offhandItem = inventory.getItemInOffHand();
        if (offhandItem != null && offhandItem.getType() == Material.END_CRYSTAL) {
            count += offhandItem.getAmount();
            inventory.setItemInOffHand(null); // Remove item from offhand
        }

        return count;
    }

    public static void removeMaterialFromInventory(Player player, Material material, int amountToRemove) {
        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack != null && itemStack.getType() == material) {
                int amountInSlot = itemStack.getAmount();

                if (amountInSlot > amountToRemove) {
                    itemStack.setAmount(amountInSlot - amountToRemove);
                    break;
                } else if (amountInSlot <= amountToRemove) {
                    player.getInventory().remove(itemStack);
                    amountToRemove -= amountInSlot;

                    if (amountToRemove == 0) {
                        break;
                    }
                }
            }
        }
    }

    private static boolean isWeapon(ItemStack item) {
        switch (item.getType()) {
            case DIAMOND_SWORD:
            case IRON_SWORD:
            case STONE_SWORD:
            case WOODEN_SWORD:
            case GOLDEN_SWORD:
            case DIAMOND_AXE:
            case IRON_AXE:
            case STONE_AXE:
            case WOODEN_AXE:
            case GOLDEN_AXE:
            case BOW:
            case CROSSBOW:
            case TRIDENT:
                return true;
            default:
                return false;
        }
    }

}
