package fr.yuki.yrpf.luaapi.inventory;

import fr.yuki.yrpf.inventory.Inventory;
import fr.yuki.yrpf.inventory.InventoryItem;
import fr.yuki.yrpf.manager.InventoryManager;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

public class RemoveChestItemEF implements ExportFunction {
    @Override
    public Object call(Object[] objects) {
        Inventory inventory = InventoryManager.getInventoryById(Integer.parseInt(objects[0].toString()));
        if(inventory == null) return false;
        InventoryItem inventoryItem = inventory.getItemByType(objects[1].toString());
        if(inventoryItem == null) return false;
        inventory.removeItem(inventoryItem, Integer.parseInt(objects[2].toString()));
        return true;
    }
}
