package fr.yuki.YukiRPFramework.manager;

import com.google.gson.Gson;
import fr.yuki.YukiRPFramework.dao.InventoryDAO;
import fr.yuki.YukiRPFramework.dao.ItemTemplateDAO;
import fr.yuki.YukiRPFramework.enums.ToastTypeEnum;
import fr.yuki.YukiRPFramework.i18n.I18n;
import fr.yuki.YukiRPFramework.inventory.Inventory;
import fr.yuki.YukiRPFramework.inventory.InventoryItem;
import fr.yuki.YukiRPFramework.model.Account;
import fr.yuki.YukiRPFramework.model.GroundItem;
import fr.yuki.YukiRPFramework.model.ItemTemplate;
import fr.yuki.YukiRPFramework.net.payload.RemoteItemInventoryPayload;
import fr.yuki.YukiRPFramework.net.payload.RequestInventoryContentPayload;
import fr.yuki.YukiRPFramework.net.payload.RequestThrowItemPayload;
import fr.yuki.YukiRPFramework.utils.Basic;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;

import java.sql.SQLException;
import java.util.*;

public class InventoryManager {
    private static HashMap<Integer, ItemTemplate> itemTemplates;
    private static HashMap<Integer, Inventory> inventories;

    public static void init() throws SQLException {
        itemTemplates = ItemTemplateDAO.getItemTemplates();
        Onset.print("Loaded " + itemTemplates.size() + " item template(s) from the database");

        inventories = InventoryDAO.loadInventories();
        Onset.print("Loaded " + inventories.size() + " inventorie(s) from the database");
    }

    /**
     * Find a inventory by the id
     * @param id The inventory id
     * @return The inventory
     */
    public static Inventory getInventoryById(int id) {
        if(!inventories.containsKey(id)) return null;
        return inventories.get(id);
    }

    /**
     * Get all inventories for the player
     * @param accountId The account id
     * @return All inventories of the player
     */
    public static ArrayList<Inventory> getInventoriesForAccount(int accountId) {
        ArrayList<Inventory> playerInventories = new ArrayList<>();
        for(Map.Entry<Integer, Inventory> inventoryEntry : inventories.entrySet()) {
            if(accountId == inventoryEntry.getValue().getCharacterId()) {
                playerInventories.add(inventoryEntry.getValue());
            }
        }
        return playerInventories;
    }

    /**
     * Try to add the item to the player inventory, this function check the weight
     * @param player The player
     * @param templateId The template id
     * @param quantity The quantity
     * @return The fresh or existing item
     */
    public static InventoryItem addItemToPlayer(Player player, String templateId, int quantity) {
        Onset.print("Add item=" + templateId + " to size=" + getInventoriesForAccount(player.getPropertyInt("accountId")).size());
        Inventory inventory = getInventoriesForAccount(player.getPropertyInt("accountId")).get(0);
        InventoryItem inventoryItem = new InventoryItem();
        inventoryItem.setId(UUID.randomUUID().toString());
        inventoryItem.setTemplateId(templateId);
        inventoryItem.setAmount(quantity);
        inventoryItem.setExtraProperties(new HashMap<>());

        Account account = WorldManager.getPlayerAccount(player);
        if(!checkInventoryWeight(player, inventoryItem)) {
            UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, I18n.t(account.getLang(), "toast.inventory.no_space_left"));
            return null;
        }
        inventoryItem = inventory.addItem(inventoryItem);
        inventory.save();
        Onset.print("Inventory weight is now " + inventory.getCurrentWeight() + "/" + inventory.getMaxWeight());
        return inventoryItem;
    }

    public static Inventory getMainInventory(Player player) {
        return getInventoriesForAccount(player.getPropertyInt("accountId")).get(0);
    }

    /**
     * Check if the item can fit in the inventory
     * @param player The player
     * @param inventoryItem The item to fit
     * @return If the weight is ok
     */
    public static boolean checkInventoryWeight(Player player, InventoryItem inventoryItem) {
        Inventory inventory = getInventoriesForAccount(player.getPropertyInt("accountId")).get(0);
        if(inventory.getCurrentWeight() + inventoryItem.getTemplate().getWeight() > inventory.getMaxWeight()) {
            return false;
        }
        return true;
    }

    /**
     * The client request the inventory content
     * @param player The player
     * @param payload The inventory type
     */
    public static void handleRequestContent(Player player, RequestInventoryContentPayload payload) {
        Onset.print("Request inventory type="+payload.getType());
        switch (payload.getType()) {
            case "main":
                getInventoriesForAccount(player.getPropertyInt("accountId")).get(0)
                        .sendInventoryContent(player);
                break;
        }
    }

    public static void handleThrowItem(Player player, RequestThrowItemPayload payload) {
        Onset.print("Player request to throw a item id=" + payload.getId() + " quantity=" + payload.getQuantity());
        if(payload.getQuantity() <= 0) return;
        Inventory inventory = getMainInventory(player);
        InventoryItem sourceItem = inventory.getItem(payload.getId());
        if(sourceItem == null) return;
        if(payload.getQuantity() > sourceItem.getAmount()) return;

        InventoryItem inventoryItem = sourceItem;
        if(payload.getQuantity() < sourceItem.getAmount()) {
            inventoryItem = sourceItem.copy();
            inventoryItem.setAmount(payload.getQuantity());
            sourceItem.setAmount(sourceItem.getAmount() - payload.getQuantity());
            inventory.updateItemPlayerView(sourceItem);
        } else {
            inventory.getInventoryItems().remove(sourceItem);
            player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new RemoteItemInventoryPayload(sourceItem.getId())));
        }

        GroundItem groundItem = new GroundItem(inventoryItem, payload.getQuantity(),
                new Vector(player.getLocation().getX() + Basic.randomNumber(20, 60),
                        player.getLocation().getY() + Basic.randomNumber(20, 60),
                        player.getLocation().getZ()));
        WorldManager.getGroundItems().add(groundItem);
        InventoryManager.getMainInventory(player).save();
    }

    public static HashMap<Integer, ItemTemplate> getItemTemplates() {
        return itemTemplates;
    }

    public static HashMap<Integer, Inventory> getInventories() {
        return inventories;
    }
}
