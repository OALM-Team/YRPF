package fr.yuki.yrpf.luaapi.items;

import fr.yuki.yrpf.inventory.Inventory;
import fr.yuki.yrpf.inventory.InventoryItem;
import fr.yuki.yrpf.manager.InventoryManager;
import fr.yuki.yrpf.manager.WorldManager;
import fr.yuki.yrpf.model.Account;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

public class RemoveItemEF implements ExportFunction {
    @Override
    public Object call(Object[] objects) {
        Player player = Onset.getPlayer(Integer.parseInt(objects[0].toString()));
        if(player == null) return false;
        Account account = WorldManager.getPlayerAccount(player);
        if(account == null) return false;
        Inventory inventory = InventoryManager.getMainInventory(player);
        InventoryItem inventoryItem = inventory.getItemByType(objects[1].toString());
        if(inventoryItem == null) return false;
        int quantity = Integer.parseInt(objects[2].toString());
        if(quantity <= 0 || quantity > inventoryItem.getAmount()) return false;
        inventory.removeItem(inventoryItem, quantity);
        return true;
    }
}
