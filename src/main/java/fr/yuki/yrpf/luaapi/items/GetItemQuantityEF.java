package fr.yuki.yrpf.luaapi.items;

import fr.yuki.yrpf.inventory.Inventory;
import fr.yuki.yrpf.inventory.InventoryItem;
import fr.yuki.yrpf.manager.InventoryManager;
import fr.yuki.yrpf.manager.WorldManager;
import fr.yuki.yrpf.model.Account;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

public class GetItemQuantityEF implements ExportFunction {
    @Override
    public Object call(Object[] objects) {
        Player player = Onset.getPlayer(Integer.parseInt(objects[0].toString()));
        if(player == null) return -1;
        Account account = WorldManager.getPlayerAccount(player);
        if(account == null) return -1;
        Inventory inventory = InventoryManager.getMainInventory(player);
        InventoryItem inventoryItem = inventory.getItemByType(objects[1].toString());
        if(inventoryItem == null) return -1;
        return inventoryItem.getAmount();
    }
}
