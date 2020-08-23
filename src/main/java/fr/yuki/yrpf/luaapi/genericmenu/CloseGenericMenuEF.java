package fr.yuki.yrpf.luaapi.genericmenu;

import fr.yuki.yrpf.manager.CharacterManager;
import fr.yuki.yrpf.ui.GenericMenu;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

public class CloseGenericMenuEF implements ExportFunction {
    @Override
    public Object call(Object[] objects) {
        Player player = Onset.getPlayer(Integer.parseInt(objects[0].toString()));
        if( CharacterManager.getCharacterStateByPlayer(player).getCurrentGenericMenu() == null) return false;
        CharacterManager.getCharacterStateByPlayer(player).getCurrentGenericMenu().hide();
        return true;
    }
}
