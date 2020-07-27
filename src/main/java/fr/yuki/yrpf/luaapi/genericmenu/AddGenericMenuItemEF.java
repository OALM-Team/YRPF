package fr.yuki.yrpf.luaapi.genericmenu;

import fr.yuki.yrpf.ui.GenericMenu;
import fr.yuki.yrpf.ui.GenericMenuItem;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

public class AddGenericMenuItemEF implements ExportFunction {
    @Override
    public Object call(Object[] objects) {
        GenericMenu genericMenu = CreateGenericMenuEF.getGenericMenuHashMap().get(Integer.parseInt(objects[0].toString()));
        genericMenu.getItems().add(new GenericMenuItem(objects[1].toString(), objects[2].toString()));
        return null;
    }
}
