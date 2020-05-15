package fr.yuki.YukiRPFramework.manager;

import fr.yuki.YukiRPFramework.character.CharacterState;
import fr.yuki.YukiRPFramework.character.CharacterToolAnimation;
import fr.yuki.YukiRPFramework.enums.ToastTypeEnum;
import fr.yuki.YukiRPFramework.i18n.I18n;
import fr.yuki.YukiRPFramework.inventory.Inventory;
import fr.yuki.YukiRPFramework.inventory.InventoryItem;
import fr.yuki.YukiRPFramework.job.WearableWorldObject;
import fr.yuki.YukiRPFramework.model.Account;
import fr.yuki.YukiRPFramework.net.payload.RequestThrowItemPayload;
import fr.yuki.YukiRPFramework.net.payload.RequestUseItemPayload;
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

        switch (inventoryItem.getTemplateId()) {
            case "11": // Temp need to use a enum
                usePot(player, inventoryItem);
                break;
        }
    }

    private static void usePot(Player player, InventoryItem inventoryItem) {
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
}
