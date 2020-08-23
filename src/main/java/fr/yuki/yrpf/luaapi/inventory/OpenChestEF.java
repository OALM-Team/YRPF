package fr.yuki.yrpf.luaapi.inventory;

import fr.yuki.yrpf.inventory.Inventory;
import fr.yuki.yrpf.manager.InventoryManager;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

public class OpenChestEF implements ExportFunction {
    @Override
    public Object call(Object[] objects) {
        Player player = Onset.getPlayer(Integer.parseInt(objects[0].toString()));
        Inventory inventory = InventoryManager.getInventoryById(Integer.parseInt(objects[1].toString()));
        if(inventory == null) return false;
        InventoryManager.openTransfertInventory(player, InventoryManager.getMainInventory(player), inventory);
        return true;
    }
}
