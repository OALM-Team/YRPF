package fr.yuki.YukiRPFramework.manager;

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
import fr.yuki.YukiRPFramework.net.payload.RequestThrowItemPayload;
import fr.yuki.YukiRPFramework.net.payload.RequestUseItemPayload;
import fr.yuki.YukiRPFramework.ui.UIState;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.enums.Animation;

public class ItemManager {
    public static void handleItemUse(Player player, RequestUseItemPayload payload) {
        Inventory inventory = InventoryManager.getMainInventory(player);
        InventoryItem inventoryItem = inventory.getItem(payload.getId());
        if(inventoryItem == null) return;
        if(inventoryItem.getAmount() <= 0) return;

        CharacterState state = CharacterManager.getCharacterStateByPlayer(player);
        if(state.isCuffed()) return;

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
        }
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
        }
    }
}
