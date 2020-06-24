package fr.yuki.YukiRPFramework.LuaAPI;

import fr.yuki.YukiRPFramework.manager.WorldManager;
import fr.yuki.YukiRPFramework.model.Account;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

public class GetAdminLevelEF implements ExportFunction {
    @Override
    public Object call(Object[] objects) {
        Account account = WorldManager.getPlayerAccount(Onset.getPlayer(Integer.parseInt(objects[0].toString())));
        if(account == null) return -1;
        return account.getAdminLevel();
    }
}
