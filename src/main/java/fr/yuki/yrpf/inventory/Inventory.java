package fr.yuki.yrpf.inventory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import eu.bebendorf.ajorm.Model;
import eu.bebendorf.ajorm.annotation.Column;
import eu.bebendorf.ajorm.annotation.Table;
import fr.yuki.yrpf.character.CharacterState;
import fr.yuki.yrpf.enums.ItemTemplateEnum;
import fr.yuki.yrpf.enums.ToastTypeEnum;
import fr.yuki.yrpf.i18n.I18n;
import fr.yuki.yrpf.manager.CharacterManager;
import fr.yuki.yrpf.manager.ItemManager;
import fr.yuki.yrpf.manager.UIStateManager;
import fr.yuki.yrpf.manager.WorldManager;
import fr.yuki.yrpf.model.Account;
import fr.yuki.yrpf.model.Bag;
import fr.yuki.yrpf.net.payload.AddItemInventoryPayload;
import fr.yuki.yrpf.net.payload.RemoteItemInventoryPayload;
import fr.yuki.yrpf.net.payload.UpdateInventoryWeightPayload;
import fr.yuki.yrpf.net.payload.UpdateItemInventoryPayload;
import lombok.Getter;
import lombok.Setter;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter @Table("tbl_inventory")
public class Inventory extends Model {
    @Column(column = "id_inventory")
    private int id;
    @Column
    private int inventoryType = -1;
    @Column
    private int inventoryItemType = -1;
    @Column
    private int characterId = -1;
    @Column
    private int vehicleId = -1;
    @Column
    private int houseItemId = -1;
    @Column(size = 0)
    private String content = "[]";
    private int maxWeight = 55;

    private ArrayList<InventoryItem> inventoryItems = new ArrayList<>();

    /**
     * Try to add the inventory item to the inventory, return a existing item if its stacked
     * @param inventoryItem The item
     * @return The inventory item affected
     */
    public InventoryItem addItem(InventoryItem inventoryItem) {
        double itemWeight = inventoryItem.getTemplate().getWeight() * inventoryItem.getAmount();
        for(InventoryItem inventoryItemEntry : this.inventoryItems) {
            if(inventoryItemEntry.canStack(inventoryItem)) {
                if(itemWeight + this.getCurrentWeight() > this.getMaxWeight()) {
                    return null;
                }
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
        if(this.getId() < 0) return;
        this.content = new Gson().toJson(this.inventoryItems);
        super.save();
    }

    /**
     * Clean all items that don't need to be loaded.
     */
    public void cleanUnusableItems() {
        for(InventoryItem inventoryItem : this.inventoryItems.stream().collect(Collectors.toList())) {
            if(inventoryItem.getTemplate() == null) continue;
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
        float baseMaxWeight = this.maxWeight;

        Player player = WorldManager.findPlayerByAccountId(this.getCharacterId());
        if(player != null) {
            CharacterState state = CharacterManager.getCharacterStateByPlayer(player);
            if(state.getCurrentBag() != null) {
                Bag bag = ItemManager.bags.stream().filter(x -> x.getModelId() == state.getCurrentBag().getModelId())
                        .findFirst().orElse(null);
                return (float) (baseMaxWeight + bag.getBonusWeight());
            } else {
                return baseMaxWeight;
            }
        } else {
            return baseMaxWeight;
        }
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

    public List<InventoryItem> getItemsByType(String type) {
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
            WorldManager.findPlayerByAccountId(this.getCharacterId()).callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new UpdateInventoryWeightPayload(this.getCurrentWeight(), this.getMaxWeight())));
            UIStateManager.sendNotification(WorldManager.findPlayerByAccountId(this.getCharacterId()),
                    ToastTypeEnum.WARN,
                    "-" + amount + " " + I18n.t(account.getLang(), "item.name." + item.getTemplateId()));
        }
    }

    public void updateWeightView() {
        WorldManager.findPlayerByAccountId(this.getCharacterId()).callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new UpdateInventoryWeightPayload(this.getCurrentWeight(), this.getMaxWeight())));
    }

    public void throwItem(InventoryItem item, int quantity) {
        // TODO
    }

}
