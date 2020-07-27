package fr.yuki.yrpf.luaapi;

import fr.yuki.yrpf.i18n.I18n;
import fr.yuki.yrpf.manager.WorldManager;
import fr.yuki.yrpf.model.Account;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

public class GetI18NKeyForPlayerEF implements ExportFunction {
    @Override
    public Object call(Object[] objects) {
        Player player = Onset.getPlayer(Integer.parseInt(objects[0].toString()));
        if(player == null) return "";
        Account account = WorldManager.getPlayerAccount(player);
        return I18n.t(account.getLang(), objects[1].toString(),
                objects.length > 2 ? objects[2].toString() : "",
                objects.length > 3 ? objects[3].toString() : "");
    }
}
