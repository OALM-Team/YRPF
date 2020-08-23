package fr.yuki.yrpf.luaapi.inventory;

import fr.yuki.yrpf.inventory.Inventory;
import fr.yuki.yrpf.inventory.InventoryItem;
import fr.yuki.yrpf.manager.InventoryManager;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

import java.util.HashMap;
import java.util.UUID;

public class AddChestItemEF implements ExportFunction {
    @Override
    public Object call(Object[] objects) {
        Inventory inventory = InventoryManager.getInventoryById(Integer.parseInt(objects[0].toString()));
        if(inventory == null) return false;
        InventoryItem inventoryItem = new InventoryItem();
        inventoryItem.setId(UUID.randomUUID().toString());
        inventoryItem.setTemplateId(objects[1].toString());
        inventoryItem.setAmount(Integer.parseInt(objects[2].toString()));
        inventoryItem.setExtraProperties(new HashMap<>());
        inventory.addItem(inventoryItem);
        return true;
    }
}
