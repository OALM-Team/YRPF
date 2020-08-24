package fr.yuki.yrpf.luaapi.worldui;

import fr.yuki.yrpf.manager.WorldUIManager;
import fr.yuki.yrpf.world.WImageContainerWUI;
import fr.yuki.yrpf.world.WProgressBarWUI;
import fr.yuki.yrpf.world.WorldUI;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

public class SetImageWUIEF implements ExportFunction {
    @Override
    public Object call(Object[] objects) {
        WorldUI worldUI = WorldUIManager.findWorldUIByID(Integer.parseInt(objects[0].toString()));
        if(worldUI == null) return false;
        if(!worldUI.getUiType().equals("wImageContainer")) {
            return false;
        }
        WImageContainerWUI wImageContainerWUI = (WImageContainerWUI)worldUI;
        wImageContainerWUI.setImageUrl(objects[1].toString());
        return true;
    }
}
