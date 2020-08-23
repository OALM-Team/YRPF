package fr.yuki.yrpf.luaapi.genericmenu;

import fr.yuki.yrpf.manager.CharacterManager;
import fr.yuki.yrpf.ui.GenericMenu;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

public class SetMenuImageEF implements ExportFunction {
    @Override
    public Object call(Object[] objects) {
        GenericMenu genericMenu = CreateGenericMenuEF.getGenericMenuHashMap().get(Integer.parseInt(objects[0].toString()));
        genericMenu.setImageUrl("../../../../" + objects[1].toString());
        return true;
    }
}
