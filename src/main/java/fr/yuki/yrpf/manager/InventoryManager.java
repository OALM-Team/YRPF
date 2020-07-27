package fr.yuki.yrpf.manager;

import com.google.gson.Gson;
import eu.bebendorf.ajorm.Repo;
import fr.yuki.yrpf.character.CharacterState;
import fr.yuki.yrpf.enums.ToastTypeEnum;
import fr.yuki.yrpf.i18n.I18n;
import fr.yuki.yrpf.inventory.Inventory;
import fr.yuki.yrpf.inventory.InventoryItem;
import fr.yuki.yrpf.model.Account;
import fr.yuki.yrpf.model.GroundItem;
import fr.yuki.yrpf.model.ItemTemplate;
import fr.yuki.yrpf.model.VehicleGarage;
import fr.yuki.yrpf.net.payload.*;
import fr.yuki.yrpf.utils.Basic;
import lombok.Getter;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;

import java.sql.SQLException;
import java.util.*;

public class InventoryManager {
    @Getter
    private static Map<Integer, ItemTemplate> itemTemplates;
    @Getter
    private static Map<Integer, Inventory> inventories;

    public static void init() throws SQLException {
        itemTemplates = new HashMap<>();
        Repo.get(ItemTemplate.class).all().forEach(it -> {
            itemTemplates.put(it.getId(), it);
        });
        Onset.print("Loaded " + itemTemplates.size() + " item template(s) from the database");

        inventories = new HashMap<>();
        Repo.get(Inventory.class).all().forEach(it -> {
            it.parseContent();
            inventories.put(it.getId(), it);
        });
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
            try {
                if(accountId == inventoryEntry.getValue().getCharacterId()) {
                    playerInventories.add(inventoryEntry.getValue());
                }
            }catch (Exception ex) {}
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
    public static InventoryItem addItemToPlayer(Player player, String templateId, int quantity, boolean checkWeight) {
        try {
            Onset.print("Add item=" + templateId + " to size=" + getInventoriesForAccount(player.getPropertyInt("accountId")).size());
            Inventory inventory = getInventoriesForAccount(player.getPropertyInt("accountId")).get(0);
            InventoryItem inventoryItem = new InventoryItem();
            inventoryItem.setId(UUID.randomUUID().toString());
            inventoryItem.setTemplateId(templateId);
            inventoryItem.setAmount(quantity);
            inventoryItem.setExtraProperties(new HashMap<>());

            Account account = WorldManager.getPlayerAccount(player);
            if(checkWeight) {
                if(!checkInventoryWeight(player, inventoryItem)) {
                    UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, I18n.t(account.getLang(), "toast.inventory.no_space_left"));
                    return null;
                }
            }
            inventoryItem = inventory.addItem(inventoryItem);
            inventory.save();
            Onset.print("Inventory weight is now " + inventory.getCurrentWeight() + "/" + inventory.getMaxWeight());
            return inventoryItem;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
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
        if(inventory.getCurrentWeight() + (inventoryItem.getTemplate().getWeight() * inventoryItem.getAmount()) > inventory.getMaxWeight()) {
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
                new Vector(player.getLocation().getX() + Basic.randomNumber(-150, 150),
                        player.getLocation().getY() + Basic.randomNumber(-150, 150),
                        player.getLocation().getZ()));
        WorldManager.getGroundItems().add(groundItem);
        InventoryManager.getMainInventory(player).save();
        inventory.updateWeightView();
    }

    public static void openTransfertInventory(Player player, Inventory inventory, Inventory chest) {
        CharacterState state = CharacterManager.getCharacterStateByPlayer(player);
        if(!UIStateManager.handleUIToogle(player, "transfertInventory")) {
            state.setCurrentChest(null);
            return;
        }
        state.setCurrentChest(chest);
        updateChestView(player, chest);
    }

    public static void updateChestView(Player player, Inventory chest) {
        Onset.print("Send chest data id="+chest.getId());
        // Clear chest content
        player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new ClearChestContentPayload()));

        // Add items to chest
        for(InventoryItem inventoryItem : chest.getInventoryItems()) {
            player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new AddItemChestPayload(inventoryItem)));
        }
        player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new UpdateChestWeightPayload(chest.getCurrentWeight(), chest.getMaxWeight())));
    }

    public static void handleTransfertToChest(Player player, String itemId) {
        CharacterState state = CharacterManager.getCharacterStateByPlayer(player);
        if(state.getCurrentChest() == null) {
            UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, "Pas de coffre disponible");
            return;
        }
        int amount = 1;
        InventoryItem inventoryItem = InventoryManager.getMainInventory(player)
                .getItem(itemId);
        if(inventoryItem == null) return;
        if(inventoryItem.getTemplate().getId() == 2) { // Cash
            amount = inventoryItem.getAmount();
        }
        InventoryItem copyItem = inventoryItem.copy();
        copyItem.setAmount(amount);
        if(state.getCurrentChest().addItem(copyItem) == null) {
            return;
        }
        InventoryManager.getMainInventory(player).removeItem(inventoryItem, amount);
        updateChestView(player, state.getCurrentChest());
        state.getCurrentChest().save();
    }

    public static void handleTransfertToInventory(Player player, String itemId) {
        CharacterState state = CharacterManager.getCharacterStateByPlayer(player);
        if(state.getCurrentChest() == null) {
            UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, "Pas de coffre disponible");
            return;
        }
        int amount = 1;
        InventoryItem inventoryItem = state.getCurrentChest().getItem(itemId);
        if(inventoryItem == null) return;
        if(inventoryItem.getTemplate().getId() == 2) { // Cash
            amount = inventoryItem.getAmount();
        }
        InventoryItem copyItem = inventoryItem.copy();
        copyItem.setAmount(amount);
        if(InventoryManager.getMainInventory(player).addItem(copyItem) == null) {
            return;
        }
        state.getCurrentChest().removeItem(inventoryItem, amount);
        updateChestView(player, state.getCurrentChest());
        state.getCurrentChest().save();
    }

    public static Inventory getVehicleInventory(VehicleGarage vehicleGarage) {
        return InventoryManager.getInventories().entrySet().stream().filter
                (x -> x.getValue().getVehicleId() == vehicleGarage.getId()).findFirst().orElse(null).getValue();
    }
}
