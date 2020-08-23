package fr.yuki.yrpf.manager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fr.yuki.yrpf.character.CharacterLoopAnimation;
import fr.yuki.yrpf.character.CharacterState;
import fr.yuki.yrpf.character.CharacterToolAnimation;
import fr.yuki.yrpf.enums.ItemTemplateEnum;
import fr.yuki.yrpf.enums.ToastTypeEnum;
import fr.yuki.yrpf.house.itembehavior.HouseChestBehavior;
import fr.yuki.yrpf.i18n.I18n;
import fr.yuki.yrpf.inventory.Inventory;
import fr.yuki.yrpf.inventory.InventoryItem;
import fr.yuki.yrpf.job.ObjectPlacementInstance;
import fr.yuki.yrpf.job.WearableWorldObject;
import fr.yuki.yrpf.job.placementObject.GeneratorPlacementInstance;
import fr.yuki.yrpf.job.placementObject.GrowBoxPlacementInstance;
import fr.yuki.yrpf.job.tools.Generator;
import fr.yuki.yrpf.model.*;
import fr.yuki.yrpf.net.payload.*;
import fr.yuki.yrpf.phone.UrgencyPhoneMessage;
import fr.yuki.yrpf.utils.Basic;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.entity.Vehicle;
import net.onfirenetwork.onsetjava.enums.Animation;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ItemManager {
    public static ArrayList<Mask> masks = new ArrayList<>();
    public static ArrayList<Bag> bags = new ArrayList<>();
    private static HashMap<Integer, String> customItemPictures = new HashMap<>();

    public static void init() {
        loadMasks();
        loadBags();
    }

    private static void loadMasks() {
        try {
            new File("yrpf").mkdir();
            masks = new Gson().fromJson(new FileReader("yrpf/masks.json"),  new TypeToken<ArrayList<Mask>>(){}.getType());
            Onset.print("Loaded " + masks.size() + " mask(s) from the cache");
        } catch (Exception e) {
            Onset.print("Can't load mask cache: " + e.toString());
        }
    }

    private static void loadBags() {
        try {
            new File("yrpf").mkdir();
            bags = new Gson().fromJson(new FileReader("yrpf/bags.json"),  new TypeToken<ArrayList<Bag>>(){}.getType());
            Onset.print("Loaded " + bags.size() + " bag(s) from the cache");
        } catch (Exception e) {
            Onset.print("Can't load bags cache: " + e.toString());
        }
    }

    public static void syncCustomImages(Player player) {
        for(Map.Entry<Integer, String> image : customItemPictures.entrySet()) {
            player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new AddCustomItemImagePayload(
                    image.getKey(), image.getValue()
            )));
        }
        for(AddImageResourcePayload payload : ModdingManager.getImageResourcePayloads()) {
            Onset.print("Dispatch image resource group=" + payload.getGroup() + " key=" + payload.getKey() + " value="+payload.getValue());
            player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(payload));
        }
    }

    public static void handleItemUse(Player player, RequestUseItemPayload payload) {
        Inventory inventory = InventoryManager.getMainInventory(player);
        InventoryItem inventoryItem = inventory.getItem(payload.getId());
        if(inventoryItem == null) return;
        if(inventoryItem.getAmount() <= 0) return;
        Account account = WorldManager.getPlayerAccount(player);

        CharacterState state = CharacterManager.getCharacterStateByPlayer(player);
        if(state.isCuffed()) return;

        // Check food item
        if(inventoryItem.getTemplate().getFoodValue() != 0 || inventoryItem.getTemplate().getDrinkValue() != 0) {
            useFood(player, inventoryItem);
        }

        // Check weapon items
        if(inventoryItem.getTemplate().getWeaponId() != -1) {
            if(WeaponManager.requestEquipWeapon(player, inventoryItem.getTemplate().getWeaponId())) {
                inventory.removeItem(inventoryItem, 1);
            }
            else {
                UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, I18n.t(account.getLang(), "toast.weapon.no_slot_available"));
            }
            return;
        }

        // Check mask item
        if(inventoryItem.getTemplate().getMaskId() != -1) {
            Mask mask = masks.stream().filter(x -> x.getModelId() == inventoryItem.getTemplate().getMaskId())
                    .findFirst().orElse(null);
            if(mask == null) {
                Onset.print("Can't find the mask in the masks.json id: " + mask.getModelId());
                return;
            }
            if(state.getCurrentMask() != null) {
                state.unattachMask();
                return;
            }
            state.attachMask(mask, player);
            return;
        }

        // Check bag item
        if(inventoryItem.getTemplate().getBagId() != -1) {
            Bag bag = bags.stream().filter(x -> x.getModelId() == inventoryItem.getTemplate().getBagId())
                    .findFirst().orElse(null);
            if(bag == null) {
                Onset.print("Can't find the bag in the bags.json id: " + inventoryItem.getTemplate().getBagId());
                return;
            }
            if(state.getCurrentBag() != null) {
                state.unattachBag(player);
                return;
            }
            state.attachBag(bag, player);
            inventory.removeItem(inventoryItem, 1);

            inventory.updateWeightView();
            return;
        }

        // Check custom actions per item
        switch (inventoryItem.getTemplateId()) {
            case "11": // Temp need to use a enum
                usePot(player, inventoryItem);
                break;

            case "10": // Ticket livraison: growbox
                useDeliveryTicketGrowbox(player, inventoryItem);
                break;

            case "14": // Ticket livraison: generator
                useDeliveryTicketGenerator(player, inventoryItem);
                break;

            case "15": // Jerrican fuel
                useJerricanFuel(player, inventoryItem);
                break;

            case "16": // Cuff
                useCuff(player, inventoryItem);
                break;

            case "17": // Defibrilator
                useDefibrilator(player, inventoryItem);
                break;

            case "18": // Light saber
                useLightSaber(player, inventoryItem);
                break;

            case "20": // Ammo
                useAmmo(player, inventoryItem);
                break;

            case "28": // RepairKit
                useRepairKit(player, inventoryItem);
                break;

            case "33": // Bandage
                useBandage(player, inventoryItem);
                break;

            case "34": // Jack
                useJack(player, inventoryItem);
                break;

            case "35": // Drill
                useDrill(player, inventoryItem);
                break;

            case "37": // Crowbar
                useCrowbar(player, inventoryItem);
                break;

            default:
                Onset.getServer().callLuaEvent("YRPF:ItemAPI:OnUse", player.getId(),
                        inventoryItem.getTemplate().getId(),
                        inventoryItem.getId());
                break;
        }
    }

    private static void useCrowbar(Player player, InventoryItem inventoryItem) {
        if(player.getVehicle() != null) return;
        CharacterState state = CharacterManager.getCharacterStateByPlayer(player);
        if(!state.canInteract()) return;
        ATM atm = ATMManager.getNearATM(player);
        if(atm == null) return;
        if(!atm.isCanBeRob()) {
            UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, "Cette ATM ne contient pas d'argent");
            return;
        }
        if(atm.getLastRobTime() + (60000 * 20) > System.currentTimeMillis()) {
            UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, "Cette ATM ne contient pas d'argent");
            return;
        }

        // Create urgency
        UrgencyRequestPayload urgencyRequestPayload = new UrgencyRequestPayload();
        urgencyRequestPayload.setService("police");
        urgencyRequestPayload.setText("(AUTOMATIQUE) Vol d'ATM en cours");
        PhoneManager.handleUrgencyRequest(player, urgencyRequestPayload);

        atm.setLastRobTime(System.currentTimeMillis());
        Onset.delay(2500, () -> {
            SoundManager.playSound3D("sounds/atm_alarm.mp3", new Vector(atm.getX(), atm.getY(), atm.getZ()), 4000, 0.4);
        });
        CharacterManager.setCharacterFreeze(player, true);
        CharacterLoopAnimation characterLoopAnimation = new CharacterLoopAnimation(player, Animation.PICKAXE_SWING, 5000, 5,
                "sounds/metal_hit.mp3");
        characterLoopAnimation.setTool(new CharacterToolAnimation(50069, new Vector(-10,4,20),
                new Vector(0,90,0), new Vector(1.2, 1.2, 1.2), "hand_r"));
        characterLoopAnimation.start();
        Onset.delay(30000, () -> {
            characterLoopAnimation.stop();
            CharacterManager.setCharacterFreeze(player, false);
            InventoryManager.addItemToPlayer(player, ItemTemplateEnum.CASH.id, Basic.randomNumber(100, 400), false);
        });
    }

    private static void useDrill(Player player, InventoryItem inventoryItem) {
        if(player.getVehicle() != null) return;
        CharacterState state = CharacterManager.getCharacterStateByPlayer(player);
        if(!state.canInteract()) return;
        Inventory inventory = InventoryManager.getMainInventory(player);
        inventory.removeItem(inventoryItem, 1);

        House house = HouseManager.getHouseAtLocation(player.getLocation());
        if(house == null) return;
        HouseItemObject houseItemObject = house.getNearbyHouseItem(player);
        if(houseItemObject == null) return;
        if(houseItemObject.getItemBehavior() == null) return;

        // Open chest
        if(houseItemObject.getFunctionId() == 3) {
            CharacterLoopAnimation characterLoopAnimation = new CharacterLoopAnimation(player, Animation.COMBINE,
                    5000, 3, "sounds/drill.mp3");
            characterLoopAnimation.start();
            CharacterManager.setCharacterFreeze(player, true);
            Onset.delay(20000, () -> {
                CharacterManager.setCharacterFreeze(player, false);
                ((HouseChestBehavior)houseItemObject.getItemBehavior()).setOpen(true);
                UIStateManager.sendNotification(player, ToastTypeEnum.SUCCESS, "Le coffre est désormais ouvert");
            });
        }
    }

    private static void useJack(Player player, InventoryItem inventoryItem) {
        if(player.getVehicle() != null) return;
        CharacterState state = CharacterManager.getCharacterStateByPlayer(player);
        if(!state.canInteract()) return;
        Vehicle vehicle = VehicleManager.getNearestVehicle(player.getLocation());
        if(vehicle == null) return;
        if(vehicle.getLocation().distance(player.getLocation()) > 500) return;
        //Inventory inventory = InventoryManager.getMainInventory(player);
        //inventory.removeItem(inventoryItem, 1);

        CharacterLoopAnimation characterLoopAnimation = new CharacterLoopAnimation(player, Animation.COMBINE,
                5000, 2, "sounds/car_repair_1.mp3");
        characterLoopAnimation.start();
        CharacterManager.setCharacterFreeze(player, true);
        vehicle.setEngineOn(false);
        Onset.delay(15000, () -> {
            CharacterManager.setCharacterFreeze(player, false);
            vehicle.setRotation(new Vector(0 , vehicle.getRotation().getY(),0));
        });
    }

    private static void useBandage(Player player, InventoryItem inventoryItem) {
        CharacterState state = CharacterManager.getCharacterStateByPlayer(player);
        Account account = WorldManager.getPlayerAccount(player);
        if(!state.canInteract()) {
            return;
        }
        if(state.getWearableWorldObject() != null) { // Can't use the item because the player wear something already
            UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, I18n.t(account.getLang(), "action.vehicle.wearSomething"));
            return;
        }
        if(player.isDead()) return;
        if(player.getHealth() >= 100) return;

        player.setAnimation(Animation.COMBINE);
        if(player.getHealth() + 25 > 100) {
            player.setHealth(100);
        } else {
            player.setHealth(player.getHealth() + 25);
        }

        Inventory inventory = InventoryManager.getMainInventory(player);
        inventory.removeItem(inventoryItem, 1);
    }

    private static void useFood(Player player, InventoryItem inventoryItem) {
        CharacterState state = CharacterManager.getCharacterStateByPlayer(player);
        Account account = WorldManager.getPlayerAccount(player);
        if(!state.canInteract()) {
            return;
        }
        if(state.getWearableWorldObject() != null) { // Can't use the item because the player wear something already
            UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, I18n.t(account.getLang(), "action.vehicle.wearSomething"));
            return;
        }
        CharacterManager.applyFoodChange(player, inventoryItem.getTemplate().getFoodValue());
        CharacterManager.applyDrinkChange(player, inventoryItem.getTemplate().getDrinkValue());
        player.setAnimation(Animation.DRINKING);

        Inventory inventory = InventoryManager.getMainInventory(player);
        inventory.removeItem(inventoryItem, 1);
    }

    private static void useCuff(Player player, InventoryItem inventoryItem) {
        if(player.getVehicle() != null) return;
        Player nearestPlayer = WorldManager.getNearestPlayer(player);
        if(nearestPlayer == null) return;
        if(nearestPlayer.getLocation().distance(player.getLocation()) > 200) return;
        WorldManager.cuffPlayer(nearestPlayer);

        Inventory inventory = InventoryManager.getMainInventory(player);
        inventory.removeItem(inventoryItem, 1);
    }

    private static void usePot(Player player, InventoryItem inventoryItem) {
        if(player.getVehicle() != null) return;
        Account account = WorldManager.getPlayerAccount(player);
        CharacterState state = CharacterManager.getCharacterStateByPlayer(player);
        if(state.getWearableWorldObject() != null) { // Can't use the item because the player wear something already
            UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, I18n.t(account.getLang(), "action.vehicle.wearSomething"));
            return;
        }

        // Put the object in the hand
        CharacterToolAnimation characterToolAnimation = new CharacterToolAnimation(554, new Vector(0, 8, 0),
                new Vector(90,0, 90), new Vector(1.50,1.50,2.40), "hand_r");
        WearableWorldObject wearableWorldObject = new WearableWorldObject(554, true,
                Animation.CARRY_IDLE, characterToolAnimation, player.getLocation());
        JobManager.getWearableWorldObjects().add(wearableWorldObject);
        JobManager.handleWearObjectRequest(player, wearableWorldObject.getUuid());

        // Delete the item from the inventory
        Inventory inventory = InventoryManager.getMainInventory(player);
        inventory.removeItem(inventoryItem, 1);
    }

    private static void useDeliveryTicketGrowbox(Player player, InventoryItem inventoryItem) {
        if(player.getVehicle() != null) return;
        CharacterState characterState = CharacterManager.getCharacterStateByPlayer(player);
        if(characterState.getCurrentObjectPlacementInstance() != null) {
            return;
        }

        UIStateManager.handleUIToogle(player, "inventory");
        ObjectPlacementInstance objectPlacementInstance = new GrowBoxPlacementInstance(50007, player.getLocation());
        characterState.setCurrentObjectPlacementInstance(objectPlacementInstance);
        objectPlacementInstance.spawn();
        objectPlacementInstance.setEditableBy(player);
    }

    private static void useDeliveryTicketGenerator(Player player, InventoryItem inventoryItem) {
        if(player.getVehicle() != null) return;
        CharacterState characterState = CharacterManager.getCharacterStateByPlayer(player);
        if(characterState.getCurrentObjectPlacementInstance() != null) {
            return;
        }

        UIStateManager.handleUIToogle(player, "inventory");
        ObjectPlacementInstance objectPlacementInstance = new GeneratorPlacementInstance( player.getLocation());
        characterState.setCurrentObjectPlacementInstance(objectPlacementInstance);
        objectPlacementInstance.spawn();
        objectPlacementInstance.setEditableBy(player);
    }

    private static void useJerricanFuel(Player player, InventoryItem inventoryItem) {
        if(player.getVehicle() != null) return;
        Account account = WorldManager.getPlayerAccount(player);
        CharacterState state = CharacterManager.getCharacterStateByPlayer(player);
        if(state.getWearableWorldObject() != null) { // Can't use the item because the player wear something already
            UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, I18n.t(account.getLang(), "action.vehicle.wearSomething"));
            return;
        }

        Generator generator = GrowboxManager.getGeneratorNearby(player.getLocation(), 250);
        if(generator != null) {
            if(generator.getFuel() >= 100) return;

            // Delete the item from the inventory
            Inventory inventory = InventoryManager.getMainInventory(player);
            inventory.removeItem(inventoryItem, 1);

            CharacterManager.setCharacterFreeze(player, true);
            CharacterLoopAnimation characterLoopAnimation = new CharacterLoopAnimation(player, Animation.FISHING, 10000, 1,
                    "sounds/water_fill.mp3");
            characterLoopAnimation.setTool(new CharacterToolAnimation(507, new Vector(5,10,-15),
                    new Vector(0,90,0), new Vector(0.75, 0.75, 0.75), "hand_r"));
            characterLoopAnimation.start();

            // Put the object in the hand
            Onset.delay(10000, () -> {
                generator.setFuel(100);
                CharacterManager.setCharacterFreeze(player, false);
                characterLoopAnimation.stop();
            });
            return;
        }

        Vehicle vehicleNearby = VehicleManager.getNearestVehicle(player.getLocation());
        if(vehicleNearby != null) {
            if(vehicleNearby.getLocation().distance(player.getLocation()) < 300) {
                FuelManager.interactFuelPoint(player, true);
                return;
            }
        }
    }

    private static void useDefibrilator(Player player, InventoryItem inventoryItem) {
        if(player.getVehicle() != null) return;
        Player nearestPlayer = WorldManager.getNearestPlayer(player);
        if(nearestPlayer == null) return;
        if(nearestPlayer.getLocation().distance(player.getLocation()) > 200) return;
        WorldManager.revive(player, nearestPlayer);
        Inventory inventory = InventoryManager.getMainInventory(player);
        inventory.removeItem(inventoryItem, 1);
    }

    private static void useLightSaber(Player player, InventoryItem inventoryItem) {
        if(player.getVehicle() != null) return;
        Account account = WorldManager.getPlayerAccount(player);
        CharacterState state = CharacterManager.getCharacterStateByPlayer(player);
        if(state.getWearableWorldObject() != null) { // Can't use the item because the player wear something already
            UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, I18n.t(account.getLang(), "action.vehicle.wearSomething"));
            return;
        }

        // Put the object in the hand
        CharacterToolAnimation characterToolAnimation = new CharacterToolAnimation(50009, new Vector(-7, 7, 0),
                new Vector(90,90, 0), new Vector(0.05,0.05,0.05), "hand_r");
        WearableWorldObject wearableWorldObject = new WearableWorldObject(50009, true,
                Animation.STOP, characterToolAnimation, player.getLocation());
        JobManager.getWearableWorldObjects().add(wearableWorldObject);
        JobManager.handleWearObjectRequest(player, wearableWorldObject.getUuid());
        SoundManager.playSound3D("sounds/ls_sound.mp3", player.getLocation(), 700, 1);
    }

    private static void useAmmo(Player player, InventoryItem inventoryItem) {
        if(WeaponManager.fillWeaponWithAmmo(player)) {
            Inventory inventory = InventoryManager.getMainInventory(player);
            inventory.removeItem(inventoryItem, 1);
        }
    }

    private static void useRepairKit(Player player, InventoryItem inventoryItem) {
        if(player.getVehicle() != null) return;
        CharacterState state = CharacterManager.getCharacterStateByPlayer(player);
        if(!state.canInteract()) return;
        Vehicle vehicle = VehicleManager.getNearestVehicle(player.getLocation());
        if(vehicle == null) return;
        if(vehicle.getLocation().distance(player.getLocation()) > 500) return;
        Inventory inventory = InventoryManager.getMainInventory(player);
        inventory.removeItem(inventoryItem, 1);

        vehicle.setHood(80);
        CharacterLoopAnimation characterLoopAnimation = new CharacterLoopAnimation(player, Animation.COMBINE,
                5000, 2, "sounds/car_repair_1.mp3");
        characterLoopAnimation.start();
        CharacterManager.setCharacterFreeze(player, true);
        vehicle.setEngineOn(false);
        Onset.delay(15000, () -> {
            CharacterManager.setCharacterFreeze(player, false);
            vehicle.setHealth(5000);
            for(int i = 0; i < 8; i++) {
                vehicle.setDamage(i + 1, 0);
            }
            vehicle.setHood(0);
        });
    }

    public static HashMap<Integer, String> getCustomItemPictures() {
        return customItemPictures;
    }
}
