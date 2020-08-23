package fr.yuki.yrpf.luaapi.worldui;

import fr.yuki.yrpf.manager.WorldManager;
import fr.yuki.yrpf.manager.WorldUIManager;
import fr.yuki.yrpf.world.WorldUI;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

public class DestroyWUIEF implements ExportFunction {
    @Override
    public Object call(Object[] objects) {
        WorldUI worldUI = WorldUIManager.findWorldUIByID(Integer.parseInt(objects[0].toString()));
        if(worldUI == null) return false;
        worldUI.destroy();
        WorldUIManager.getWorldUIS().remove(worldUI);
        return true;
    }
}
