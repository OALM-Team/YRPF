package fr.yuki.yrpf.luaapi.player;

import fr.yuki.yrpf.manager.WorldManager;
import fr.yuki.yrpf.model.Account;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

public class AddBankMoneyEF implements ExportFunction {
    @Override
    public Object call(Object[] objects) {
        Account account = WorldManager.getPlayerAccount(Onset.getPlayer(Integer.parseInt(objects[0].toString())));
        if(account == null) return false;
        account.setBankMoney(account.getBankMoney() + Integer.parseInt(objects[1].toString()));
        if(account.getBankMoney() < 0) {
            account.setBankMoney(0);
        }
        account.save();
        return true;
    }
}
