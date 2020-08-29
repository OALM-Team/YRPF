package fr.yuki.yrpf.luaapi.house;

import fr.yuki.yrpf.manager.HouseManager;
import fr.yuki.yrpf.model.House;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

public class CanBuildHereEF implements ExportFunction {
    @Override
    public Object call(Object[] objects) {
        Player player = Onset.getPlayer(Integer.parseInt(objects[0].toString()));
        House house = HouseManager.getHouseAtLocation(player.getLocation());
        if(house == null) return false;
        return HouseManager.canBuildInHouse(player, house);
    }
}
