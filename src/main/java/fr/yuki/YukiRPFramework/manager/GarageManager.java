package fr.yuki.YukiRPFramework.manager;

import com.google.gson.Gson;
import fr.yuki.YukiRPFramework.dao.GarageDAO;
import fr.yuki.YukiRPFramework.dao.VehicleGarageDAO;
import fr.yuki.YukiRPFramework.enums.ItemTemplateEnum;
import fr.yuki.YukiRPFramework.enums.ToastTypeEnum;
import fr.yuki.YukiRPFramework.inventory.Inventory;
import fr.yuki.YukiRPFramework.model.*;
import fr.yuki.YukiRPFramework.net.payload.*;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Color;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.entity.Vehicle;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class GarageManager {

    private static ArrayList<VehicleGarage> vehicleGarages;

    public static void init() throws SQLException {
        vehicleGarages = VehicleGarageDAO.loadVehiclesGarage();
        Onset.print("Loaded " + vehicleGarages.size() + " vehicle(s) from the database");

        // Reset last garage to vehicle
        for(VehicleGarage vehicleGarage : vehicleGarages) {
            vehicleGarage.setGarageId(vehicleGarage.getGarageLastId());
        }
    }

    /**
     * Check if the player is near a garage to interact with
     * @param player The player
     */
    public static void handleGarageInteract(Player player) {
        for(Garage garage : WorldManager.getGarages()) {
            if(garage.isNear(player)) {
                openGarage(player, garage);
                return;
            }
        }
    }

    public static Garage getNearestGarage(Player player) {
        for(Garage garage : WorldManager.getGarages()) {
            if(garage.isNear(player)) {
                return garage;
            }
        }
        return null;
    }

    /**
     * Open the garage to the player
     * @param player The player
     * @param garage The garage to open
     */
    public static void openGarage(Player player, Garage garage) {
        if(player.getVehicle() != null) {
            storeVehicleInGarage(player, garage);
            return;
        }

        UIStateManager.handleUIToogle(player, "garage");
        Onset.print("Request garage open player=" + player.getSteamId() + " garageId=" + garage.getId() + " name=" + garage.getName());

        // Reset the garage first
        player.callRemoteEvent("GlobalUI:DispatchToUI",
                new Gson().toJson(new ResetVehicleGaragePayload()));

        // Send vehicles to players
        for(VehicleGarage vehicleGarage : getVehicleGaragesByPlayer(player, garage.getId())) {
            player.callRemoteEvent("GlobalUI:DispatchToUI",
                    new Gson().toJson(new AddVehicleGaragePayload(vehicleGarage.getModelId(), vehicleGarage.getUuid(),
                            vehicleGarage.getColor(), vehicleGarage.getLicencePlate())));
        }
    }

    /**
     * Store the vehicle in the garage
     * @param player The player
     * @param garage The garage to store
     */
    public static void storeVehicleInGarage(Player player, Garage garage) {
        Vehicle vehicle = player.getVehicle();
        if(!vehicle.getPropertyString("owner").equals(player.getSteamId())){
            UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, "Ce véhicule ne vous appartient pas");
            return;
        }
        // Exit the player from the vehicle
        player.exitVehicle();

        // Update the vehicle instance
        VehicleGarage vehicleGarage = vehicleGarages.stream().filter(x -> x.getUuid().equals(vehicle.getPropertyString("uuid")))
                .findFirst().orElse(null);
        if(vehicleGarage == null) {
            UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, "Ce véhicule ne vous appartient pas");
            return;
        }
        vehicleGarage.setGarageId(garage.getId());
        vehicleGarage.setGarageLastId(garage.getId());
        VehicleManager.clearKeysForVehicle(vehicle, player);
        vehicleGarage.save();

        // Destroy the vehicle
        vehicle.destroy();

        UIStateManager.sendNotification(player, ToastTypeEnum.SUCCESS, "Véhicule stocké dans le garage " + garage.getName());
    }

    /**
     * Get all the vehicles in a specific garage for a player
     * @param player The player
     * @param garageId The garage id
     * @return All players vehicle in the garage given
     */
    public static ArrayList<VehicleGarage> getVehicleGaragesByPlayer(Player player, int garageId) {
        return new ArrayList<>(vehicleGarages.stream().filter(x -> x.getOwner() == WorldManager.getPlayerAccount(player).getId() && x.getGarageId() == garageId)
                .collect(Collectors.toList()));
    }

    public static VehicleGarage findVehicleGarageByVehicle(Vehicle vehicle) {
        return vehicleGarages.stream().filter(x -> x.getUuid() == vehicle.getPropertyString("uuid")).findFirst().orElse(null);
    }

    /**
     * Handle the vehicle request from the player on the garage
     * @param player The player
     */
    public static void handleRequestVehicle(Player player, String vehicleUuid) {
        Garage garage = getNearestGarage(player);
        if(garage == null) {
            UIStateManager.handleUIToogle(player, "garage");
            return;
        }
        VehicleGarage vehicleGarage = getVehicleGaragesByPlayer(player, garage.getId()).stream()
                .filter(x -> x.getUuid().equals(vehicleUuid)).findFirst().orElse(null);
        if(vehicleGarage == null) return;

        if(VehicleManager.getNearestVehicle(player.getLocation()) != null) {
            if(VehicleManager.getNearestVehicle(player.getLocation()).getLocation().distance(player.getLocation()) < 400) {
                UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, "Il y a un véhicule dans la zone d'apparition");
                return;
            }
        }

        vehicleGarage.setGarageId(-1);
        vehicleGarage.setGarageLastId(garage.getId());
        VehicleManager.CreateVehicleResult createVehicleResult = VehicleManager.createVehicle(vehicleGarage.getModelId(),
                new Vector(garage.getX(), garage.getY(), garage.getZ()), player.getLocationAndHeading().getHeading(),
                player, vehicleGarage);
        UIStateManager.handleUIToogle(player, "garage");
        vehicleGarage.save();
    }

    public static ArrayList<VehicleGarage> getVehicleGarages() {
        return vehicleGarages;
    }

    public static void handleVSellerInteract(Player player) {
        for(VehicleSeller vehicleSeller : WorldManager.getVehicleSellers()) {
            if(vehicleSeller.isNear(player)) {
                openVehicleSeller(player, vehicleSeller);
                return;
            }
        }
    }

    public static VehicleSeller getNearVehicleSeller(Player player) {
        for(VehicleSeller vehicleSeller : WorldManager.getVehicleSellers()) {
            if(vehicleSeller.isNear(player)) {
                return vehicleSeller;
            }
        }
        return null;
    }

    public static void openVehicleSeller(Player player, VehicleSeller vehicleSeller) {
        UIStateManager.handleUIToogle(player, "vseller");
        for(SellListItem sellListItem : vehicleSeller.getSellList()) {
            player.callRemoteEvent("GlobalUI:DispatchToUI",
                    new Gson().toJson(new AddVSellerVehiclePayload(sellListItem.getModelId(), sellListItem.getPrice(),
                            sellListItem.getName(), sellListItem.getDescription())));
        }
    }

    public static void handleRequestBuyVehicle(Player player, RequestBuyVehiclePayload payload) {
        if(player.getVehicle() != null) {
            UIStateManager.handleUIToogle(player, "vseller");
            return;
        }

        VehicleSeller vehicleSeller = getNearVehicleSeller(player);
        if(vehicleSeller == null) {
            UIStateManager.handleUIToogle(player, "vseller");
            return;
        }
        SellListItem sellListItem = vehicleSeller.getSellList().stream().filter(x -> x.getModelId() == payload.getModelId()).findFirst().orElse(null);
        if(sellListItem == null) return;

        if(VehicleManager.getNearestVehicle(player.getLocation()) != null) {
            if(VehicleManager.getNearestVehicle(player.getLocation()).getLocation().distance(player.getLocation()) < 400) {
                UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, "Il y a un véhicule dans la zone d'apparition");
                return;
            }
        }

        // Check price
        Inventory inventory = InventoryManager.getMainInventory(player);
        if(inventory.getCashAmount() < sellListItem.getPrice()) {
            UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, "Vous n'avez pas assez d'argent sur vous pour ça");
            UIStateManager.handleUIToogle(player, "vseller");
            return;
        }

        UIStateManager.handleUIToogle(player, "vseller");

        // Remove cash and spawn vehicle
        inventory.removeItem(inventory.getItemByType(ItemTemplateEnum.CASH.id), sellListItem.getPrice());
        VehicleManager.CreateVehicleResult createVehicleResult = VehicleManager.createVehicle(sellListItem.getModelId(), player.getLocation(), player.getLocationAndHeading().getHeading(),
                player, null);
        createVehicleResult.getVehicleGarage().setColor(payload.getColor());
        createVehicleResult.getVehicleGarage().save();
        createVehicleResult.getVehicle().setColor(new Color(payload.getAWTColor().getRed(), payload.getAWTColor().getGreen(), payload.getAWTColor().getBlue()));

        UIStateManager.sendNotification(player, ToastTypeEnum.SUCCESS, "Félicitation vous avez acheté " + sellListItem.getName() +
                " pour " + sellListItem.getPrice() + "$");
    }
}
