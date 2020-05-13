package fr.yuki.YukiRPFramework.inventory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fr.yuki.YukiRPFramework.dao.InventoryDAO;
import fr.yuki.YukiRPFramework.enums.ItemTemplateEnum;
import fr.yuki.YukiRPFramework.enums.ToastTypeEnum;
import fr.yuki.YukiRPFramework.i18n.I18n;
import fr.yuki.YukiRPFramework.manager.UIStateManager;
import fr.yuki.YukiRPFramework.manager.WorldManager;
import fr.yuki.YukiRPFramework.model.Account;
import fr.yuki.YukiRPFramework.net.payload.AddItemInventoryPayload;
import fr.yuki.YukiRPFramework.net.payload.RemoteItemInventoryPayload;
import fr.yuki.YukiRPFramework.net.payload.UpdateInventoryWeightPayload;
import fr.yuki.YukiRPFramework.net.payload.UpdateItemInventoryPayload;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Inventory {
    private int id;
    private int inventoryType;
    private int inventoryItemType;
    private int characterId;
    private int vehicleId;
    private String content;
    private ArrayList<InventoryItem> inventoryItems;

    public Inventory() {
        this.inventoryItems = new ArrayList<InventoryItem>();
    }

    /**
     * Try to add the inventory item to the inventory, return a existing item if its stacked
     * @param inventoryItem The item
     * @return The inventory item affected
     */
    public InventoryItem addItem(InventoryItem inventoryItem) {
        for(InventoryItem inventoryItemEntry : this.inventoryItems) {
            if(inventoryItemEntry.canStack(inventoryItem)) {
                inventoryItemEntry.setAmount
                        (inventoryItemEntry.getAmount() + inventoryItem.getAmount());
                this.updateItemPlayerView(inventoryItemEntry);

                // Display the notification
                if(WorldManager.findPlayerByAccountId(this.getCharacterId()) != null) {
                    Account account = WorldManager.getPlayerAccount(WorldManager.findPlayerByAccountId(this.getCharacterId()));
                    UIStateManager.sendNotification(WorldManager.findPlayerByAccountId(this.getCharacterId()),
                            ToastTypeEnum.SUCCESS,
                            "+" + inventoryItem.getAmount() + " " +
                                    I18n.t(account.getLang(), "item.name." + inventoryItem.getTemplateId()));
                }

                return inventoryItemEntry;
            }
        }
        this.inventoryItems.add(inventoryItem);
        this.addItemPlayerView(inventoryItem);

        // Display the notification
        if(WorldManager.findPlayerByAccountId(this.getCharacterId()) != null) {
            Account account = WorldManager.getPlayerAccount(WorldManager.findPlayerByAccountId(this.getCharacterId()));
            UIStateManager.sendNotification(WorldManager.findPlayerByAccountId(this.getCharacterId()),
                    ToastTypeEnum.SUCCESS,
                    "+" + inventoryItem.getAmount() + " " +
                            I18n.t(account.getLang(), "item.name." + inventoryItem.getTemplateId()));
        }

        return inventoryItem;
    }

    /**
     * Display the new item to the client
     * @param inventoryItem The item
     */
    public void addItemPlayerView(InventoryItem inventoryItem) {
        if(WorldManager.findPlayerByAccountId(this.getCharacterId()) == null) return;
        Player player = WorldManager.findPlayerByAccountId(this.getCharacterId());
        if(player == null) return;
        player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new AddItemInventoryPayload(inventoryItem)));
        player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new UpdateInventoryWeightPayload(this.getCurrentWeight(), this.getMaxWeight())));
    }

    /**
     * Update a existing item to the client
     * @param inventoryItem The item
     */
    public void updateItemPlayerView(InventoryItem inventoryItem) {
        if(WorldManager.findPlayerByAccountId(this.getCharacterId()) == null) return;
        Player player = WorldManager.findPlayerByAccountId(this.getCharacterId());
        if(player == null) return;
        player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new UpdateItemInventoryPayload(inventoryItem)));
        player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new UpdateInventoryWeightPayload(this.getCurrentWeight(), this.getMaxWeight())));
    }

    /**
     * Compute the content and save it into the database
     */
    public void save() {
        try {
            if(this.getId() <= 0) return;
            this.content = new Gson().toJson(this.inventoryItems);
            InventoryDAO.updateInventory(this);
        } catch (Exception ex) {
            Onset.print("Can't save the inventory: " + ex.toString());
        }
    }

    /**
     * Clean all items that don't need to be loaded.
     */
    public void cleanUnusableItems() {
        for(InventoryItem inventoryItem : this.inventoryItems.stream().collect(Collectors.toList())) {
            if(inventoryItem.getTemplate().getId() == Integer.parseInt(ItemTemplateEnum.VKEY.id)) {
                this.inventoryItems.remove(inventoryItem);
            }
        }
        this.save();
    }

    /**
     * Parse the content into a item array list
     */
    public void parseContent() {
        this.inventoryItems = new Gson().fromJson(this.content,
                new TypeToken<ArrayList<InventoryItem>>(){}.getType());
        this.cleanUnusableItems();
    }

    /**
     * Send all the items to the client
     * @param player The player
     */
    public void sendInventoryContent(Player player) {
        for(InventoryItem inventoryItem : this.inventoryItems) {
            this.addItemPlayerView(inventoryItem);
        }
        player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new UpdateInventoryWeightPayload(this.getCurrentWeight(), this.getMaxWeight())));
    }

    /**
     * Get the max weight of the inventory
     * @return The max weight
     */
    public float getMaxWeight() {
        return 30;
    }

    /**
     * Get the current weight of the inventory
     * @return The current weight
     */
    public float getCurrentWeight() {
        float weight = 0;
        for(InventoryItem inventoryItem : this.inventoryItems) {
            weight += inventoryItem.getTemplate().getWeight() * inventoryItem.getAmount();
        }
        return weight;
    }

    /**
     * Find a item by the type
     * @param type Type item id
     * @return The inventory item
     */
    public InventoryItem getItemByType(String type) {
        return this.inventoryItems.stream().filter(x -> x.getTemplateId().equals(type)).findFirst().orElse(null);
    }

    public InventoryItem getItem(String id) {
        return this.inventoryItems.stream().filter(x -> x.getId().equals(id)).findFirst().orElse(null);
    }

    public ArrayList<InventoryItem> getItemsByType(String type) {
        return new ArrayList<>(this.inventoryItems.stream().filter(x -> x.getTemplateId().equals(type)).collect(Collectors.toList()));
    }

    /**
     * Get the cash amount on the player
     * @return The cash amount
     */
    public int getCashAmount() {
        InventoryItem cashItem = this.getItemByType(ItemTemplateEnum.CASH.id);
        if(cashItem == null) return 0;
        return cashItem.getAmount();
    }

    /**
     * Remove item amount to the inventory, delete it if the amount if higher than the item amount
     * @param item The item
     * @param amount The amount
     */
    public void removeItem(InventoryItem item, int amount) {
        if(amount >= item.getAmount()) {
            this.inventoryItems.remove(item);
            this.save();

            if(WorldManager.findPlayerByAccountId(this.getCharacterId()) != null) {
                WorldManager.findPlayerByAccountId(this.getCharacterId())
                        .callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new RemoteItemInventoryPayload(item.getId())));
            }
        } else {
            item.setAmount(item.getAmount() - amount);
            this.updateItemPlayerView(item);
            this.save();
        }

        // Display the notification
        if(WorldManager.findPlayerByAccountId(this.getCharacterId()) != null) {
            Account account = WorldManager.getPlayerAccount(WorldManager.findPlayerByAccountId(this.getCharacterId()));
            UIStateManager.sendNotification(WorldManager.findPlayerByAccountId(this.getCharacterId()),
                    ToastTypeEnum.WARN,
                    "-" + amount + " " + I18n.t(account.getLang(), "item.name." + item.getTemplateId()));
        }
    }

    public void throwItem(InventoryItem item, int quantity) {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInventoryType() {
        return inventoryType;
    }

    public void setInventoryType(int inventoryType) {
        this.inventoryType = inventoryType;
    }

    public int getInventoryItemType() {
        return inventoryItemType;
    }

    public void setInventoryItemType(int inventoryItemType) {
        this.inventoryItemType = inventoryItemType;
    }

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<InventoryItem> getInventoryItems() {
        return inventoryItems;
    }

    public void setInventoryItems(ArrayList<InventoryItem> inventoryItems) {
        this.inventoryItems = inventoryItems;
    }
}
