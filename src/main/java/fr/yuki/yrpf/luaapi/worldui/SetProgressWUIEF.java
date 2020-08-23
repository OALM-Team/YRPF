package fr.yuki.yrpf.luaapi.worldui;

import fr.yuki.yrpf.manager.WorldUIManager;
import fr.yuki.yrpf.world.WProgressBarWUI;
import fr.yuki.yrpf.world.WorldUI;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

public class SetProgressWUIEF implements ExportFunction {
    @Override
    public Object call(Object[] objects) {
        WorldUI worldUI = WorldUIManager.findWorldUIByID(Integer.parseInt(objects[0].toString()));
        if(worldUI == null) return false;
        if(!worldUI.getUiType().equals("wProgressBar")) {
            return false;
        }
        WProgressBarWUI wProgressBarWUI = (WProgressBarWUI)worldUI;
        wProgressBarWUI.setProgress(Integer.parseInt(objects[1].toString()));
        return true;
    }
}
