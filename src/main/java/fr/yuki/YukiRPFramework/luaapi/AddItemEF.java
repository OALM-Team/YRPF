package fr.yuki.YukiRPFramework.luaapi;

import fr.yuki.YukiRPFramework.inventory.Inventory;
import fr.yuki.YukiRPFramework.manager.InventoryManager;
import fr.yuki.YukiRPFramework.manager.WorldManager;
import fr.yuki.YukiRPFramework.model.Account;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

public class AddItemEF implements ExportFunction {
    @Override
    public Object call(Object[] objects) {
        Player player = Onset.getPlayer(Integer.parseInt(objects[0].toString()));
        if(player == null) return false;
        Account account = WorldManager.getPlayerAccount(player);
        if(account == null) return false;
        InventoryManager.addItemToPlayer(player, objects[1].toString(), Integer.parseInt(objects[2].toString()), true);
        return true;
    }
}
