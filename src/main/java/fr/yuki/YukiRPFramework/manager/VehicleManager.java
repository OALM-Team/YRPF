package fr.yuki.YukiRPFramework.manager;

import com.google.gson.Gson;
import fr.yuki.YukiRPFramework.dao.VehicleGarageDAO;
import fr.yuki.YukiRPFramework.enums.ItemTemplateEnum;
import fr.yuki.YukiRPFramework.enums.ToastTypeEnum;
import fr.yuki.YukiRPFramework.i18n.I18n;
import fr.yuki.YukiRPFramework.inventory.Inventory;
import fr.yuki.YukiRPFramework.inventory.InventoryItem;
import fr.yuki.YukiRPFramework.job.WearableWorldObject;
import fr.yuki.YukiRPFramework.job.customGoal.DeliveryPointGoal;
import fr.yuki.YukiRPFramework.model.Account;
import fr.yuki.YukiRPFramework.model.House;
import fr.yuki.YukiRPFramework.model.VehicleGarage;
import fr.yuki.YukiRPFramework.net.payload.AddVChestItemPayload;
import fr.yuki.YukiRPFramework.net.payload.AddVehicleGaragePayload;
import fr.yuki.YukiRPFramework.ui.UIState;
import fr.yuki.YukiRPFramework.utils.Basic;
import fr.yuki.YukiRPFramework.vehicle.storeLayout.*;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Color;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Door;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.entity.Vehicle;
import net.onfirenetwork.onsetjava.plugin.event.EventHandler;
import net.onfirenetwork.onsetjava.plugin.event.player.PlayerEnterVehicleEvent;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

public class VehicleManager {

    private static ArrayList<VehicleStoreLayout> vehicleStoreLayouts;

    public static void init() {
        vehicleStoreLayouts = new ArrayList<>();
        vehicleStoreLayouts.add(new MiniTruckStoreLayout());
        vehicleStoreLayouts.add(new GarbageTruckStoreLayout());
        vehicleStoreLayouts.add(new MiniPickupStoreLayout());
        vehicleStoreLayouts.add(new TruckStoreLayout());
    }

    public static class CreateVehicleResult {
        private Vehicle vehicle;
        private VehicleGarage vehicleGarage;

        public CreateVehicleResult(Vehicle vehicle, VehicleGarage vehicleGarage){
            this.vehicle = vehicle;
            this.vehicleGarage = vehicleGarage;
        }

        public Vehicle getVehicle() {
            return vehicle;
        }

        public VehicleGarage getVehicleGarage() {
            return vehicleGarage;
        }
    }

    /**
     * Create a vehicle at positon, if player is given, he will be the owner
     * @param modelId The model id
     * @param position The position
     * @param heading The heading
     * @param player Attach a player to the vehicle, the player will be the owner
     */
    public static CreateVehicleResult createVehicle(int modelId, Vector position, double heading, Player player, VehicleGarage vehicleGarage,
                                                    boolean isRental) {
        try {
            Vehicle vehicle = Onset.getServer().createVehicle(position, heading, modelId);
            vehicle.disableRespawn(); // Disable the respawn of it
            if(vehicleGarage == null) {
                vehicle.setLicensePlate(getRandomLicencePlate("C"));
                String vehicleUUID = UUID.randomUUID().toString();
                vehicle.setProperty("uuid", vehicleUUID, true);
                vehicle.setColor(new Color(Basic.randomNumber(0, 255), Basic.randomNumber(0, 255), Basic.randomNumber(0, 255)));

                // Save the vehicle in garage
                vehicleGarage = new VehicleGarage();
                vehicleGarage.setUuid(vehicleUUID);
                vehicleGarage.setOwner(WorldManager.getPlayerAccount(player).getId());
                vehicleGarage.setGarageId(-1);
                vehicleGarage.setGarageLastId(WorldManager.getGarages().get(0).getId());
                vehicleGarage.setModelId(modelId);
                vehicleGarage.setDamage("[0,0,0,0,0,0,0,0]");
                vehicleGarage.setHealth(5000);
                vehicleGarage.setRental(isRental);
                vehicleGarage.setLicencePlate(vehicle.getLicensePlate());
                vehicleGarage.setColor("#" + Integer.toHexString(vehicle.getColor().getRed()) + Integer.toHexString(vehicle.getColor().getGreen()) + Integer.toHexString(vehicle.getColor().getBlue()));
                if(!isRental) VehicleGarageDAO.createVehicleGarage(vehicleGarage);
                GarageManager.getVehicleGarages().add(vehicleGarage);
            }
            else {
                vehicleGarage.setRental(isRental);
                vehicle.setLicensePlate(vehicleGarage.getLicencePlate());
                vehicle.setProperty("uuid", vehicleGarage.getUuid(), true);
                java.awt.Color color = java.awt.Color.decode(vehicleGarage.getColor());
                vehicle.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue()));
            }

            vehicle.setProperty("owner", player.getSteamId(), true);
            vehicle.setProperty("locked", 0, true);
            vehicle.setProperty("fuel", 50, true);

            // Create the key for the player
            createKeyForVehicle(vehicle, player);
            player.enterVehicle(vehicle);
            vehicle.setHeading(heading);

            return new CreateVehicleResult(vehicle, vehicleGarage);
        } catch (Exception ex) {
            ex.printStackTrace();
            Onset.print("Can't create the vehicle: " + ex.toString());
            return null;
        }
    }

    /**
     * Create a vehicle key for the vehicle in the player inventory
     * @param vehicle The vehicle
     * @param player The player where the key will be put in
     * @return The key item
     */
    public static InventoryItem createKeyForVehicle(Vehicle vehicle, Player player) {
        String vehicleUUID = vehicle.getPropertyString("uuid");
        InventoryItem keyItem = InventoryManager.addItemToPlayer(player, ItemTemplateEnum.VKEY.id, 1, false);
        if(keyItem == null) return null;
        keyItem.getExtraProperties().put("vehicle_uuid", vehicleUUID);
        InventoryManager.getMainInventory(player).save();
        return keyItem;
    }

    /**
     * Delete all keys in player inventory for the vehicle
     * @param vehicle The vehicle
     * @param player The player where delete keys
     */
    public static void clearKeysForVehicle(Vehicle vehicle, Player player) {
        for(InventoryItem keyItem : InventoryManager.getMainInventory(player).getItemsByType(ItemTemplateEnum.VKEY.id)) {
            try {
                if(keyItem.getExtraProperties().get("vehicle_uuid").equals(vehicle.getPropertyString("uuid"))) {
                    InventoryManager.getMainInventory(player).removeItem(keyItem, keyItem.getAmount());
                }
            }
            catch (Exception ex) {}
        }
    }

    /**
     * The player request to lock the nearest vehicle
     * @param player The player
     */
    public static void handleVehicleLockRequest(Player player) {
        Onset.print("Request lock toogle from player="+player.getSteamId());

        Account account = WorldManager.getPlayerAccount(player);
        Door nearestDoor = WorldManager.getNearestDoor(player.getLocation());
        if(nearestDoor.getLocation().distance(player.getLocation()) < 200) {
            House house = HouseManager.getHouseAtLocation(nearestDoor.getLocation());
            if(house != null) {
                if(HouseManager.canBuildInHouse(player, house)) {
                    if(house.isLocked()) {
                        house.setLocked(false);
                        UIStateManager.sendNotification(player, ToastTypeEnum.WARN, I18n.t(account.getLang(), "toast.house.doors_unlocked"));
                    } else {
                        house.setLocked(true);
                        for(Door door : house.getLine3D().getDoorsInside()) {
                            door.close();
                        }
                        UIStateManager.sendNotification(player, ToastTypeEnum.SUCCESS, I18n.t(account.getLang(), "toast.house.doors_locked"));
                    }
                    SoundManager.playSound3D("sounds/lock_door.mp3", nearestDoor.getLocation(), 500, 0.8);
                    return;
                }
            }
        }

        for(Vehicle vehicle : Onset.getServer().getVehicles()) {
            if(vehicle.getLocation().distance(player.getLocation()) < 1500) {
                tryToogleLockVehicle(player, vehicle);
            }
        }
    }

    /**
     * Try to lock/unlock the vehicle if player have the right key
     * @param player The player
     * @param vehicle The vehicle
     */
    public static void tryToogleLockVehicle(Player player, Vehicle vehicle) {
        Account account = WorldManager.getPlayerAccount(player);
        for(InventoryItem keyItem : InventoryManager.getMainInventory(player).getItemsByType(ItemTemplateEnum.VKEY.id)) {
            if(keyItem.getExtraProperties().get("vehicle_uuid").equals(vehicle.getPropertyString("uuid"))) {
                if(vehicle.getPropertyInt("locked") == 1) {
                    UIStateManager.sendNotification(player, ToastTypeEnum.WARN, I18n.t(account.getLang(),
                            "toast.vehicle.lock_off", vehicle.getLicensePlate()));
                    vehicle.setProperty("locked", 0, true);
                    SoundManager.playSound3D("sounds/carUnlock.mp3", vehicle.getLocation(), 2500, 1);
                } else {
                    UIStateManager.sendNotification(player, ToastTypeEnum.WARN, I18n.t(account.getLang(),
                            "toast.vehicle.lock_on", vehicle.getLicensePlate()));
                    vehicle.setProperty("locked", 1, true);
                    SoundManager.playSound3D("sounds/carLock.mp3", vehicle.getLocation(), 2500, 1);
                }
                blinkVehicleLight(vehicle);
            }
        }
    }

    /**
     * Blink lights of the vehicle
     * @param vehicle The vehicle
     */
    private static void blinkVehicleLight(Vehicle vehicle) {
        for(int i=0; i<6; i++){
            int c = i;
            Onset.delay(c*200, () -> {
                vehicle.setLightOn(c%2==0);
            });
        }
    }

    /**
     * Get a random licence plate
     * @return A random licence plate
     */
    private static String getRandomLicencePlate(String prefix) {
        return prefix + "-" + Basic.randomNumber(1000, 9999);
    }

    public static void onPlayerEnterVehicle(Player player, Vehicle vehicle, int seatId) {
        if(seatId == 1) {
            if(vehicle.getPropertyInt("fuel") > 0) {
                vehicle.setEngineOn(true);
            }
        }

        Account account = WorldManager.getPlayerAccount(player);
        // Show waypoints for items in storage
        for(WearableWorldObject wearableWorldObject : getVehicleWearableObjects(vehicle)) {
            if(wearableWorldObject.getDeliveryPointGoal() == null) continue;
            DeliveryPointGoal deliveryPointGoal = wearableWorldObject.getDeliveryPointGoal();
            player.callRemoteEvent("Map:AddWaypoint", I18n.t(account.getLang(), "toast.delivery.delivery_point"), deliveryPointGoal.getWearableWorldObject().getUuid(),
                    deliveryPointGoal.getPosition().getX(), deliveryPointGoal.getPosition().getY(),
                    deliveryPointGoal.getPosition().getZ());
        }
    }

    public static void onPlayerVehicleExit(Player player, Vehicle vehicle, int seatId) {
        if(seatId == 1) vehicle.setEngineOn(false);

        // Remove waypoints for items in storage
        for(WearableWorldObject wearableWorldObject : getVehicleWearableObjects(vehicle)) {
            if(wearableWorldObject.getDeliveryPointGoal() == null) continue;
            DeliveryPointGoal deliveryPointGoal = wearableWorldObject.getDeliveryPointGoal();
            player.callRemoteEvent("Map:RemoveWaypoint", deliveryPointGoal.getWearableWorldObject().getUuid());
        }
    }

    public static Vehicle getNearestVehicle(Vector position) {
        Vehicle nearestVehicle = null;
        for(Vehicle vehicle : Onset.getVehicles()) {
            try {
                if(nearestVehicle == null) {
                    nearestVehicle = vehicle;
                }
                else {
                    if(vehicle.getLocation().distance(position) < nearestVehicle.getLocation().distance(position)) {
                        nearestVehicle = vehicle;
                    }
                }
            }catch (Exception ex) {
                continue;
            }
        }
        return nearestVehicle;
    }

    public static boolean canStoreWorldWearableObject(Vehicle vehicle) {
        if(vehicle.getModel() == 22 || vehicle.getModel() == 23)
            return true;
        return false;
    }

    public static int getInteractionDistance(Vehicle vehicle) {
        if(vehicle.getModel() == 22 || vehicle.getModel() == 23)
            return 500;
        if(vehicle.getModel() == 9 || vehicle.getModel() == 28)
            return 700;
        return 400;
    }


    public static boolean storeWorldWearableObject(Vehicle vehicle, WearableWorldObject wearableWorldObject) {
        VehicleStoreLayout vehicleStoreLayout = vehicleStoreLayouts.stream()
                .filter(x -> x.isAdaptedForModel(vehicle.getModel())).findFirst().orElse(null);
        if(vehicleStoreLayout == null) return false;
        return vehicleStoreLayout.store(vehicle, wearableWorldObject);
    }

    public static ArrayList<WearableWorldObject> getVehicleWearableObjects(Vehicle vehicle) {
        return new ArrayList<>(JobManager.getWearableWorldObjects().stream()
                .filter(x -> x.getVehicleUUID() != null)
                .filter(x -> x.getVehicleUUID().equals(vehicle.getPropertyString("uuid")))
                .collect(Collectors.toList()));
    }

    public static boolean handleVehicleChestStorageRequest(Player player) {
        if(player.getVehicle() != null) return false;
        Vehicle vehicle = getNearestVehicle(player.getLocation());
        if(vehicle == null) return false;
        if(vehicle.getModel() == 33 || vehicle.getModel() == 34) return false;
        if(vehicle.getLocation().distance(player.getLocation()) > getInteractionDistance(vehicle)) return false;

        Account account = WorldManager.getPlayerAccount(player);
        try{
            if(vehicle.getPropertyInt("locked") == 1) {
                UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, I18n.t(account.getLang(), "action.vehicle.locked"));
                return true;
            }
            if(!UIStateManager.handleUIToogle(player, "vchest")) return true;
        } catch (Exception ex) {
            return false;
        }
        for(WearableWorldObject wearableWorldObject : getVehicleWearableObjects(vehicle)) {
            try {
                player.callRemoteEvent("GlobalUI:DispatchToUI",
                        new Gson().toJson(new AddVChestItemPayload(wearableWorldObject.getUuid(), wearableWorldObject.getModelId(), "")));
            }catch (Exception ex) {

            }
        }
        return true;
    }

    public static void handleRequestWearFromVehicleChest(Player player, String uuid) {
        if(player.getVehicle() != null) return;
        Vehicle vehicle = getNearestVehicle(player.getLocation());
        if(vehicle.getLocation().distance(player.getLocation()) > VehicleManager.getInteractionDistance(vehicle)) return;

        Account account = WorldManager.getPlayerAccount(player);
        if(vehicle.getPropertyInt("locked") == 1) {
            UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, I18n.t(account.getLang(), "action.vehicle.locked"));
            return;
        }
        WearableWorldObject wearableWorldObject = getVehicleWearableObjects(vehicle).stream()
                .filter(x -> x.getUuid().equals(uuid)).findFirst().orElse(null);
        if(wearableWorldObject == null) return;
        UIStateManager.handleUIToogle(player, "vchest");
        if(CharacterManager.getCharacterStateByPlayer(player).getWearableWorldObject() != null) {
            UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, I18n.t(account.getLang(), "action.vehicle.wearSomething"));
            return;
        }

        wearableWorldObject.removeFromVehicle();
        wearableWorldObject.requestWear(player);
    }
}
