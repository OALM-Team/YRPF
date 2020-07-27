package fr.yuki.yrpf.luaapi.genericmenu;

import fr.yuki.yrpf.ui.GenericMenu;
import lombok.Getter;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

import java.util.HashMap;

public class CreateGenericMenuEF implements ExportFunction {
    private static int id = 0;
    @Getter
    private static HashMap<Integer, GenericMenu> genericMenuHashMap = new HashMap<>();

    @Override
    public Object call(Object[] objects) {
        Player player = Onset.getPlayer(Integer.parseInt(objects[0].toString()));
        if(player == null) return -1;
        GenericMenu genericMenu = new GenericMenu(player);
        int menuId = ++id;
        genericMenuHashMap.put(menuId, genericMenu);
        return String.valueOf(menuId);
    }
}
