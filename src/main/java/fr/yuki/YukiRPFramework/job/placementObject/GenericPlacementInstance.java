package fr.yuki.YukiRPFramework.job.placementObject;

import fr.yuki.YukiRPFramework.enums.ItemTemplateEnum;
import fr.yuki.YukiRPFramework.enums.ToastTypeEnum;
import fr.yuki.YukiRPFramework.i18n.I18n;
import fr.yuki.YukiRPFramework.inventory.Inventory;
import fr.yuki.YukiRPFramework.inventory.InventoryItem;
import fr.yuki.YukiRPFramework.job.ObjectPlacementInstance;
import fr.yuki.YukiRPFramework.job.deliveryPackage.GeneratorDeliveryPackage;
import fr.yuki.YukiRPFramework.job.deliveryPackage.GenericDeliveryPackage;
import fr.yuki.YukiRPFramework.manager.InventoryManager;
import fr.yuki.YukiRPFramework.manager.UIStateManager;
import fr.yuki.YukiRPFramework.manager.WorldManager;
import fr.yuki.YukiRPFramework.model.Account;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;

public class GenericPlacementInstance extends ObjectPlacementInstance {
    private int price;

    public GenericPlacementInstance(Vector spawnPoint, int modelId, int price) {
        super(modelId, spawnPoint);
        this.price = price;
    }

    @Override
    public void onPlacementDone(Player player, Vector position, Vector rotation) {
        Account account = WorldManager.getPlayerAccount(player);
        Inventory inventory = InventoryManager.getMainInventory(player);
        if(price > inventory.getCashAmount()) {
            UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, I18n.t(account.getLang(), "toast.house.no_enought_money_on_me"));
            return;
        }
        inventory.removeItem(inventory.getItemByType(ItemTemplateEnum.CASH.id), price);
        GenericDeliveryPackage genericDeliveryPackage = new GenericDeliveryPackage(player, position, rotation, this.getModelId());
        genericDeliveryPackage.spawn();
    }
}
