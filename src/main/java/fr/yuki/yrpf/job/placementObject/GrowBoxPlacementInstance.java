package fr.yuki.yrpf.job.placementObject;

import fr.yuki.yrpf.enums.ItemTemplateEnum;
import fr.yuki.yrpf.inventory.Inventory;
import fr.yuki.yrpf.inventory.InventoryItem;
import fr.yuki.yrpf.job.ObjectPlacementInstance;
import fr.yuki.yrpf.job.deliveryPackage.GrowBoxDeliveryPackage;
import fr.yuki.yrpf.manager.InventoryManager;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;

public class GrowBoxPlacementInstance extends ObjectPlacementInstance {
    public GrowBoxPlacementInstance(int modelId, Vector spawnPoint) {
        super(modelId, spawnPoint);
    }

    @Override
    public void onPlacementDone(Player player, Vector position, Vector rotation) {
        Inventory inventory = InventoryManager.getMainInventory(player);
        InventoryItem inventoryItem = inventory.getItemByType(ItemTemplateEnum.TICKET_DELIVERY_GROW_BOX.id);
        if(inventoryItem == null) {
            Onset.print("Item not found");
            return;
        }
        if(inventoryItem.getAmount() <= 0) {
            Onset.print("Not amount: " + inventoryItem.getAmount());
            return;
        }

        inventory.removeItem(inventoryItem, 1);

        GrowBoxDeliveryPackage growBoxDeliveryPackage = new GrowBoxDeliveryPackage(player, position, rotation);
        growBoxDeliveryPackage.spawn();
    }
}
