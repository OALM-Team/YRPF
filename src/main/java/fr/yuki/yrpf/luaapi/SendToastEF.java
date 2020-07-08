package fr.yuki.yrpf.luaapi;

import fr.yuki.yrpf.manager.UIStateManager;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

public class SendToastEF implements ExportFunction {

    @Override
    public Object call(Object[] objects) {
        Player player = Onset.getPlayer(Integer.parseInt(objects[0].toString()));
        if(player == null) return null;
        String type = objects[1].toString();
        String text = objects[2].toString();
        UIStateManager.sendNotification(player, type, text);

        return null;
    }
}
