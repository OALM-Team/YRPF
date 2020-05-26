package fr.yuki.YukiRPFramework.manager;

import com.google.gson.Gson;
import fr.yuki.YukiRPFramework.dao.HouseDAO;
import fr.yuki.YukiRPFramework.dao.HouseItemDAO;
import fr.yuki.YukiRPFramework.enums.ItemTemplateEnum;
import fr.yuki.YukiRPFramework.enums.ToastTypeEnum;
import fr.yuki.YukiRPFramework.i18n.I18n;
import fr.yuki.YukiRPFramework.inventory.Inventory;
import fr.yuki.YukiRPFramework.model.Account;
import fr.yuki.YukiRPFramework.model.House;
import fr.yuki.YukiRPFramework.model.HouseItemObject;
import fr.yuki.YukiRPFramework.net.payload.AddPhoneMessagePayload;
import fr.yuki.YukiRPFramework.net.payload.SetHouseInfosPayload;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Door;
import net.onfirenetwork.onsetjava.entity.Player;

import java.sql.SQLException;
import java.util.ArrayList;

public class HouseManager {
    public static ArrayList<House> houses;

    public static void init() throws SQLException {
        houses = HouseDAO.loadHouses();
        Onset.print("Loaded " + houses.size() + " house(s) from the database");
        spawnHouseItems();
    }

    public static void spawnHouseItems() throws SQLException {
        for(HouseItemObject houseItemObject : HouseItemDAO.loadHouseItems()) {
            House house = getHouseAtLocation(houseItemObject.getPosition());
            if(house == null) return;
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
        Door nearestDoor = WorldManager.getNearestDoor(player.getLocation());
        House house = getHouseAtLocation(nearestDoor.getLocation());
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

    public static ArrayList<House> getHouses() {
        return houses;
    }
}
