package fr.yuki.yrpf.luaapi.inventory;

import com.google.gson.Gson;
import fr.yuki.yrpf.inventory.Inventory;
import fr.yuki.yrpf.manager.InventoryManager;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

public class GetChestItemsEF implements ExportFunction {
    @Override
    public Object call(Object[] objects) {
        Inventory inventory = InventoryManager.getInventoryById(Integer.parseInt(objects[0].toString()));
        if(inventory == null) return false;
        return new Gson().toJson(inventory.getInventoryItems());
    }
}
