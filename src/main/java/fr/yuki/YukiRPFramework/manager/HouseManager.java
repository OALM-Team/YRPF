package fr.yuki.YukiRPFramework.manager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import eu.bebendorf.ajorm.Repo;
import fr.yuki.YukiRPFramework.character.CharacterState;
import fr.yuki.YukiRPFramework.dao.HouseDAO;
import fr.yuki.YukiRPFramework.dao.HouseItemDAO;
import fr.yuki.YukiRPFramework.enums.ItemTemplateEnum;
import fr.yuki.YukiRPFramework.enums.ToastTypeEnum;
import fr.yuki.YukiRPFramework.i18n.I18n;
import fr.yuki.YukiRPFramework.inventory.Inventory;
import fr.yuki.YukiRPFramework.job.ObjectPlacementInstance;
import fr.yuki.YukiRPFramework.job.placementObject.GenericPlacementInstance;
import fr.yuki.YukiRPFramework.model.*;
import fr.yuki.YukiRPFramework.net.payload.SetHouseInfosPayload;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Door;
import net.onfirenetwork.onsetjava.entity.Player;

import java.io.File;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.ArrayList;

public class HouseManager {
    public static ArrayList<House> houses;
    public static ArrayList<ItemShopObject> itemShopObjects;

    public static void init() throws SQLException {
        houses = HouseDAO.loadHouses();
        Onset.print("Loaded " + houses.size() + " house(s) from the database");

        loadItemsShop();
        spawnHouseItems();
    }

    private static void loadItemsShop() {
        try {
            new File("yrpf").mkdir();
            itemShopObjects = new Gson().fromJson(new FileReader("yrpf/itemshop.json"),  new TypeToken<ArrayList<ItemShopObject>>(){}.getType());
            Onset.print("Loaded " + itemShopObjects.size() + " item(s) shop from the cache");
        } catch (Exception e) {
            Onset.print("Can't load job cache: " + e.toString());
        }
    }

    public static void spawnHouseItems() throws SQLException {
        for(HouseItemObject houseItemObject : HouseItemDAO.loadHouseItems()) {
            House house = getHouseAtLocation(houseItemObject.getPosition());
            if(house == null) {
                Onset.print("Can't find the house for item: " + houseItemObject.getId());
                return;
            }
            houseItemObject.setHouse(house);
            house.getHouseItemObjects().add(houseItemObject);
            houseItemObject.spawn();
        }
    }

    public static House getHouseAtLocation(Vector position) {
        return houses.stream().filter(x -> x.getLine3D().isInside(position)).findFirst().orElse(null);
    }

    public static void handleHouseMenu(Player player, Vector origin) {
        House house = getHouseAtLocation(origin);
        if(house == null) return;
        Account account = WorldManager.getPlayerAccount(player);
        if(house.getAccountId() != -1) {
            UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, I18n.t(account.getLang(), "toast.house.already_buy"));
            return;
        }
        if(!UIStateManager.handleUIToogle(player, "houseBuy")) {
            return;
        }
        player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new SetHouseInfosPayload
                (house.getPrice(), house.getName())));
    }

    public static void handleBuyHouseRequest(Player player) throws SQLException {
        House house = HouseManager.getHouseAtLocation(player.getLocation());
        if(house == null) {
            Onset.print("houseid by door: " + house.getId());
            house = HouseManager.getHouseAtLocation(WorldManager.getNearestDoor(player.getLocation()).getLocation());
        }
        else {
            Onset.print("houseid by playerloc: " + house.getId());
        }
        if(house == null) return;
        Account account = WorldManager.getPlayerAccount(player);
        if(house.getAccountId() != -1) {
            UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, I18n.t(account.getLang(), "toast.house.already_buy"));
            return;
        }
        Inventory inventory = InventoryManager.getMainInventory(player);
        if(inventory.getCashAmount() < house.getPrice()) {
            UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, I18n.t(account.getLang(), "toast.house.no_enought_money_on_me"));
            return;
        }

        // Open doors
        for(Door door : house.getLine3D().getDoorsInside()) {
            door.open();
        }
        inventory.removeItem(inventory.getItemByType(ItemTemplateEnum.CASH.id), house.getPrice());
        house.setAccountId(account.getId());
        HouseDAO.saveHouse(house);
        UIStateManager.sendNotification(player, ToastTypeEnum.SUCCESS, I18n.t(account.getLang(), "toast.house.success_buy"));
        UIStateManager.handleUIToogle(player, "houseBuy");
    }

    public static void handleRequestBuyItemShop(Player player, int modelId) {
        CharacterState characterState = CharacterManager.getCharacterStateByPlayer(player);
        if(characterState.getCurrentObjectPlacementInstance() != null) return;
        if(characterState.getWearableWorldObject() != null) return;
        Account account = WorldManager.getPlayerAccount(player);

        Inventory inventory = InventoryManager.getMainInventory(player);
        ItemShopObject itemShopObject = itemShopObjects.stream().filter(x -> x.getModelId() == modelId).findFirst().orElse(null);
        if(itemShopObject == null) return;
        if(itemShopObject.getPrice() > inventory.getCashAmount()) {
            UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, I18n.t(account.getLang(), "toast.house.no_enought_money_on_me"));
            return;
        }

        UIStateManager.handleUIToogle(player, "phone");
        ObjectPlacementInstance objectPlacementInstance = new GenericPlacementInstance(player.getLocation(), modelId, itemShopObject.getPrice());
        characterState.setCurrentObjectPlacementInstance(objectPlacementInstance);
        objectPlacementInstance.spawn();
        objectPlacementInstance.setEditableBy(player);
    }

    public static void handleGiveHouseKey(Player player, int target) {
        Player playerTarget = Onset.getPlayers().stream().filter(x -> x.getId() == target)
                .findFirst().orElse(null);
        if(playerTarget == null) return;
        House house = HouseManager.getHouseAtLocation(player.getLocation());
        if(house == null) return;
        if(!HouseManager.canBuildInHouse(player, house)) return;
        Account targetAccount = WorldManager.getPlayerAccount(playerTarget);
        if(house.getAllowedPlayers().contains(targetAccount) || playerTarget.getId() == house.getAccountId()) return;
        house.getAllowedPlayers().add(targetAccount.getId());
        UIStateManager.sendNotification(playerTarget, ToastTypeEnum.SUCCESS, "Vous avez désormais les clés de la maison");
    }

    public static boolean canBuildInHouse(Player player, House house) {
        Account account = WorldManager.getPlayerAccount(player);
        if(account.getId() == house.getAccountId()) {
            return true;
        }
        if(house.getAllowedPlayers().contains(account.getId())) {
            return true;
        }

        // Check same compagny
        if(account.getCompanyId() != -1) {
            Account ownerAccount = Repo.get(Account.class).get(house.getAccountId());
            if(ownerAccount != null) {
                if(account.getCompanyId() == ownerAccount.getCompanyId()) {
                    return true;
                }
            }
        }

        return false;
    }

    public static ArrayList<House> getHouses() {
        return houses;
    }

    public static ArrayList<ItemShopObject> getItemShopObjects() {
        return itemShopObjects;
    }
}
