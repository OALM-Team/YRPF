package fr.yuki.yrpf.luaapi.genericmenu;

import fr.yuki.yrpf.manager.CharacterManager;
import fr.yuki.yrpf.ui.GenericMenu;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

public class ShowGenericMenuEF implements ExportFunction {
    @Override
    public Object call(Object[] objects) {
        GenericMenu genericMenu = CreateGenericMenuEF.getGenericMenuHashMap().get(Integer.parseInt(objects[0].toString()));
        genericMenu.addCloseItem();
        genericMenu.show();
        CharacterManager.getCharacterStateByPlayer(genericMenu.getPlayer()).setCurrentGenericMenu(genericMenu);
        return null;
    }
}
