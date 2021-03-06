package fr.yuki.yrpf.luaapi.job;

import fr.yuki.yrpf.manager.JobManager;
import fr.yuki.yrpf.manager.WorldManager;
import fr.yuki.yrpf.model.Account;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

public class AddJobExpEF implements ExportFunction {
    @Override
    public Object call(Object[] objects) {
        Player player = Onset.getPlayer(Integer.parseInt(objects[0].toString()));
        Account account = WorldManager.getPlayerAccount(player);
        if(account == null) return false;
        JobManager.addExp(player, objects[1].toString(), Integer.parseInt(objects[2].toString()));
        return true;
    }
}
