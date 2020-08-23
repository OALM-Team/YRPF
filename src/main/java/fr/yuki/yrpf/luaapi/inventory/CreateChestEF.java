package fr.yuki.yrpf.luaapi.inventory;

import fr.yuki.yrpf.inventory.Inventory;
import fr.yuki.yrpf.manager.InventoryManager;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

public class CreateChestEF implements ExportFunction {

    @Override
    public Object call(Object[] objects) {
        Inventory inventory = new Inventory();
        inventory.setInventoryType(1);
        inventory.setInventoryType(0);
        inventory.setCharacterId(-1);
        inventory.setVehicleId(-1);
        inventory.setHouseItemId(-1);
        inventory.save();
        InventoryManager.getInventories().put(inventory.getId(), inventory);
        inventory.setMaxWeight(Integer.parseInt(objects[0].toString()));
        inventory.parseContent();
        Onset.print("Create new chest from lua id="+inventory.getId());
        return inventory.getId();
    }
}
