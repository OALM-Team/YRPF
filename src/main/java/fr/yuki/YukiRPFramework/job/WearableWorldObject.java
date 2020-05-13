package fr.yuki.YukiRPFramework.job;

import fr.yuki.YukiRPFramework.character.CharacterJobLevel;
import fr.yuki.YukiRPFramework.character.CharacterToolAnimation;
import fr.yuki.YukiRPFramework.enums.ItemTemplateEnum;
import fr.yuki.YukiRPFramework.enums.JobEnum;
import fr.yuki.YukiRPFramework.enums.ToastTypeEnum;
import fr.yuki.YukiRPFramework.i18n.I18n;
import fr.yuki.YukiRPFramework.job.customGoal.DeliveryPointGoal;
import fr.yuki.YukiRPFramework.manager.*;
import fr.yuki.YukiRPFramework.model.Account;
import fr.yuki.YukiRPFramework.ui.UIState;
import fr.yuki.YukiRPFramework.vehicle.storeLayout.StoreLayoutTransform;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.entity.Vehicle;
import net.onfirenetwork.onsetjava.entity.WorldObject;
import net.onfirenetwork.onsetjava.enums.Animation;

import java.util.ArrayList;
import java.util.UUID;


public class WearableWorldObject {
    private String uuid;
    private int modelId;
    private boolean isPhysicEnable;
    private Animation wearAnimation;
    private CharacterToolAnimation toolAnimation;
    private Vector position;
    private WorldObject worldObject;
    private String vehicleUUID;
    private int vehicleStorageLayoutIndex;
    private DeliveryPointGoal deliveryPointGoal;
    private Vector originPosition;

    public WearableWorldObject(int modelId, boolean isPhysicEnable, Animation wearAnimation, CharacterToolAnimation toolAnimation, Vector position) {
        this.uuid = UUID.randomUUID().toString();
        this.modelId = modelId;
        this.isPhysicEnable = isPhysicEnable;
        this.wearAnimation = wearAnimation;
        this.toolAnimation = toolAnimation;
        this.position = position;
        this.originPosition = position;
        this.createWorldObject();
    }

    private void createWorldObject() {
        this.worldObject = Onset.getServer().createObject(this.position, this.modelId);
        this.worldObject.setScale(this.toolAnimation.getScale());
        if(this.isPhysicEnable)
            this.worldObject.setProperty("enablePhysic", 1, true);
        if(ModdingManager.isCustomModelId(this.modelId))
            ModdingManager.assignCustomModel(this.worldObject, this.modelId);
        this.worldObject.setProperty("uuid", this.uuid, true);
        this.worldObject.setProperty("canBeWear", 1, true);
    }

    /**
     * Wear the object on the player and play the animation
     * @param player The player
     */
    public void requestWear(Player player) {
        CharacterManager.getCharacterStateByPlayer(player).setWearableWorldObject(player, this);
        this.toolAnimation.attach(player);
        player.setAnimation(this.wearAnimation);
        this.worldObject.destroy();
        Account account = WorldManager.getPlayerAccount(player);

        // Set the delivery location
        if(deliveryPointGoal != null) {
            UIStateManager.sendNotification(player, ToastTypeEnum.SUCCESS, I18n.t(account.getLang(), "toast.delivery.need_to_be_delivered"));
            player.callRemoteEvent("Map:AddWaypoint", I18n.t(account.getLang(), "toast.delivery.delivery_point"), deliveryPointGoal.getWearableWorldObject().getUuid(),
                    deliveryPointGoal.getPosition().getX(), deliveryPointGoal.getPosition().getY(),
                    deliveryPointGoal.getPosition().getZ());
        }
    }

    /**
     * Throw the object on the ground and recreate the world object properly
     * @param player The player
     */
    public void requestUnwear(Player player, boolean delete) {
        // Put this resource in the chest
        Account account = WorldManager.getPlayerAccount(player);
        if(!delete) {
            Vehicle nearbyVehicle = VehicleManager.getNearestVehicle(player.getLocation());
            if(nearbyVehicle != null) {
                if(nearbyVehicle.getLocation().distance(player.getLocation()) < VehicleManager.getInteractionDistance(nearbyVehicle)) {
                    /**if(!VehicleManager.canStoreWorldWearableObject(nearbyVehicle)) {
                        UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, "Impossible car ce véhicule ne le permet pas");
                        return;
                    }*/
                    if(nearbyVehicle.getPropertyInt("locked") == 1) {
                        UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, I18n.t(account.getLang(), "action.vehicle.locked"));
                        return;
                    }

                    if(!VehicleManager.storeWorldWearableObject(nearbyVehicle, this)) {
                        UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, I18n.t(account.getLang(), "toast.vehicle.no_space_left"));
                        return;
                    }

                    // Unattach the item
                    this.position = player.getLocation();
                    this.toolAnimation.unAttach();
                    player.setAnimation(Animation.STOP);
                    CharacterManager.getCharacterStateByPlayer(player).setWearableWorldObject(player, null);

                    if(deliveryPointGoal != null)
                        player.callRemoteEvent("Map:RemoveWaypoint", deliveryPointGoal.getWearableWorldObject().getUuid());

                    return;
                }
            }
        }

        // Delivery reward
        if(deliveryPointGoal != null) {
            if(deliveryPointGoal.isNear(player)) {
                JobManager.getWearableWorldObjects().remove(this);
                ArrayList<CharacterJobLevel> characterJobLevels = account.decodeCharacterJob();
                CharacterJobLevel characterJobLevel = characterJobLevels.stream().filter(x -> x.getJobId().equals(JobEnum.DELIVERY.type)).findFirst().orElse(null);

                int rewardPerDistance = (int)Math.floor((originPosition.distance(player.getLocation()) / 1000) * characterJobLevel.getJobLevel().getLevel());
                InventoryManager.addItemToPlayer(player, ItemTemplateEnum.CASH.id, rewardPerDistance);
                SoundManager.playSound3D("sounds/cash_register.mp3", player.getLocation(), 200, 0.3);
                delete = true;
                JobManager.addExp(player, JobEnum.DELIVERY, 15);
                UIStateManager.sendNotification(player, ToastTypeEnum.SUCCESS, I18n.t(account.getLang(), "toast.delivery.shipped",
                        String.valueOf(rewardPerDistance), String.valueOf(Math.floor(originPosition.distance(player.getLocation()) / 100))));
            }
            player.callRemoteEvent("Map:RemoveWaypoint", deliveryPointGoal.getWearableWorldObject().getUuid());
        }

        // Throw on ground
        this.position = player.getLocation();
        this.toolAnimation.unAttach();
        if(!delete) this.createWorldObject();
        player.setAnimation(Animation.STOP);
        CharacterManager.getCharacterStateByPlayer(player).setWearableWorldObject(player, null);

        if(delete) JobManager.getWearableWorldObjects().remove(this);
    }

    /**
     * Store this in the chest of a vehicle at a specific location
     * @param vehicle The vehicle
     * @param storeLayoutTransform The position to store
     */
    public void storeInVehicle(Vehicle vehicle, StoreLayoutTransform storeLayoutTransform) {
        this.worldObject = Onset.getServer().createObject(this.position, this.modelId);
        this.worldObject.attach(vehicle, storeLayoutTransform.getPosition(), storeLayoutTransform.getRotation(), "");
        this.worldObject.setScale(storeLayoutTransform.getScale());
        this.setVehicleUUID(vehicle.getPropertyString("uuid"));
        this.setVehicleStorageLayoutIndex(storeLayoutTransform.getIndex());
        if(ModdingManager.isCustomModelId(this.modelId))
            ModdingManager.assignCustomModel(this.worldObject, this.modelId);
    }

    public void removeFromVehicle() {
        this.setVehicleUUID("");
        this.setVehicleStorageLayoutIndex(-1);
        this.worldObject.destroy();
    }

    public String getUuid() {
        return uuid;
    }

    public Vector getPosition() {
        return position;
    }

    public int getModelId() {
        return modelId;
    }

    public String getVehicleUUID() {
        return vehicleUUID;
    }

    public void setVehicleUUID(String vehicleUUID) {
        this.vehicleUUID = vehicleUUID;
    }

    public WorldObject getWorldObject() {
        return worldObject;
    }

    public int getVehicleStorageLayoutIndex() {
        return vehicleStorageLayoutIndex;
    }

    public void setVehicleStorageLayoutIndex(int vehicleStorageLayoutIndex) {
        this.vehicleStorageLayoutIndex = vehicleStorageLayoutIndex;
    }

    public DeliveryPointGoal getDeliveryPointGoal() {
        return deliveryPointGoal;
    }

    public void setDeliveryPointGoal(DeliveryPointGoal deliveryPointGoal) {
        this.deliveryPointGoal = deliveryPointGoal;
    }
}
