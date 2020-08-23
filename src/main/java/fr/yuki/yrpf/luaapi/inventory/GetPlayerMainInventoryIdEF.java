package fr.yuki.yrpf.luaapi.inventory;

import fr.yuki.yrpf.inventory.Inventory;
import fr.yuki.yrpf.manager.InventoryManager;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

public class GetPlayerMainInventoryIdEF implements ExportFunction {
    @Override
    public Object call(Object[] objects) {
        Player player = Onset.getPlayer(Integer.parseInt(objects[0].toString()));
        if(player == null) return -1;
        return InventoryManager.getMainInventory(player).getId();
    }
}
