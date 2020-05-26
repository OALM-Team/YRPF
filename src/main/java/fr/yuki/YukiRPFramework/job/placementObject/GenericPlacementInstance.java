package fr.yuki.YukiRPFramework.job.placementObject;

import fr.yuki.YukiRPFramework.enums.ItemTemplateEnum;
import fr.yuki.YukiRPFramework.inventory.Inventory;
import fr.yuki.YukiRPFramework.inventory.InventoryItem;
import fr.yuki.YukiRPFramework.job.ObjectPlacementInstance;
import fr.yuki.YukiRPFramework.job.deliveryPackage.GeneratorDeliveryPackage;
import fr.yuki.YukiRPFramework.job.deliveryPackage.GenericDeliveryPackage;
import fr.yuki.YukiRPFramework.manager.InventoryManager;
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
        GenericDeliveryPackage genericDeliveryPackage = new GenericDeliveryPackage(player, position, rotation, this.getModelId());
        genericDeliveryPackage.spawn();
    }
}
