package fr.yuki.yrpf.job.placementObject;

import fr.yuki.yrpf.enums.ItemTemplateEnum;
import fr.yuki.yrpf.inventory.Inventory;
import fr.yuki.yrpf.inventory.InventoryItem;
import fr.yuki.yrpf.job.ObjectPlacementInstance;
import fr.yuki.yrpf.job.deliveryPackage.GeneratorDeliveryPackage;
import fr.yuki.yrpf.manager.InventoryManager;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;

public class GeneratorPlacementInstance extends ObjectPlacementInstance {
    public GeneratorPlacementInstance(Vector spawnPoint) {
        super(581, spawnPoint);
    }

    @Override
    public void onPlacementDone(Player player, Vector position, Vector rotation) {
        Inventory inventory = InventoryManager.getMainInventory(player);
        InventoryItem inventoryItem = inventory.getItemByType(ItemTemplateEnum.TICKET_DELIVERY_GENERATOR.id);
        if(inventoryItem == null) {
            return;
        }
        if(inventoryItem.getAmount() <= 0) {
            return;
        }

        inventory.removeItem(inventoryItem, 1);

        GeneratorDeliveryPackage generatorDeliveryPackage = new GeneratorDeliveryPackage(player, position, rotation);
        generatorDeliveryPackage.spawn();
    }
}
