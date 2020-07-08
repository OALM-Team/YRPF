package fr.yuki.yrpf.luaapi;

import fr.yuki.yrpf.manager.InventoryManager;
import fr.yuki.yrpf.manager.WorldManager;
import fr.yuki.yrpf.model.Account;
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
