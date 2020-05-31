package fr.yuki.YukiRPFramework.manager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fr.yuki.YukiRPFramework.character.CharacterLoopAnimation;
import fr.yuki.YukiRPFramework.character.CharacterState;
import fr.yuki.YukiRPFramework.character.CharacterToolAnimation;
import fr.yuki.YukiRPFramework.enums.ToastTypeEnum;
import fr.yuki.YukiRPFramework.i18n.I18n;
import fr.yuki.YukiRPFramework.inventory.Inventory;
import fr.yuki.YukiRPFramework.inventory.InventoryItem;
import fr.yuki.YukiRPFramework.job.ObjectPlacementInstance;
import fr.yuki.YukiRPFramework.job.WearableWorldObject;
import fr.yuki.YukiRPFramework.job.placementObject.GeneratorPlacementInstance;
import fr.yuki.YukiRPFramework.job.placementObject.GrowBoxPlacementInstance;
import fr.yuki.YukiRPFramework.job.tools.Generator;
import fr.yuki.YukiRPFramework.model.Account;
import fr.yuki.YukiRPFramework.model.Bag;
import fr.yuki.YukiRPFramework.model.ItemShopObject;
import fr.yuki.YukiRPFramework.model.Mask;
import fr.yuki.YukiRPFramework.net.payload.RequestThrowItemPayload;
import fr.yuki.YukiRPFramework.net.payload.RequestUseItemPayload;
import fr.yuki.YukiRPFramework.ui.UIState;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.entity.Vehicle;
import net.onfirenetwork.onsetjava.enums.Animation;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class ItemManager {
    public static ArrayList<Mask> masks = new ArrayList<>();
    public static ArrayList<Bag> bags = new ArrayList<>();

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
            return;
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
                state.unattachBag();
                return;
            }
            state.attachBag(bag, player);
            inventory.removeItem(inventoryItem, 1);
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
        }
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
}
