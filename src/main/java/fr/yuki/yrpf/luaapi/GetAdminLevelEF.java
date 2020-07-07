package fr.yuki.yrpf.luaapi;

import fr.yuki.yrpf.manager.WorldManager;
import fr.yuki.yrpf.model.Account;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

public class GetAdminLevelEF implements ExportFunction {
    @Override
    public Object call(Object[] objects) {
        Account account = WorldManager.getPlayerAccount(Onset.getPlayer(Integer.parseInt(objects[0].toString())));
        if(account == null) return -1;
        return account.getAdminLevel();
    }
}
