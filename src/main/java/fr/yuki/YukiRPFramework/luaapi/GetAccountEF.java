package fr.yuki.YukiRPFramework.luaapi;

import com.google.gson.Gson;
import fr.yuki.YukiRPFramework.manager.WorldManager;
import fr.yuki.YukiRPFramework.model.Account;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

public class GetAccountEF implements ExportFunction {
    @Override
    public Object call(Object[] objects) {
        Account account = WorldManager.getPlayerAccount(Onset.getPlayer(Integer.parseInt(objects[0].toString())));
        if(account == null) return null;
        return new Gson().toJson(account);
    }
}
