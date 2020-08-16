package fr.yuki.yrpf.luaapi.house;

import fr.yuki.yrpf.manager.HouseManager;
import fr.yuki.yrpf.model.House;
import fr.yuki.yrpf.model.HouseItemObject;
import fr.yuki.yrpf.model.ItemShopObject;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

public class SetHouseObjectBehaviorEF implements ExportFunction {
    @Override
    public Object call(Object[] objects) {
        House house = HouseManager.getHouses().stream().filter(x -> x.getId() == Integer.getInteger(objects[0].toString()))
                .findFirst().orElse(null);
        if(house == null) return false;
        HouseItemObject houseItemObject = house.getHouseItemObjects().stream().filter(x -> x.getId() == Integer.parseInt(objects[1].toString()))
                .findFirst().orElse(null);
        houseItemObject.setFunctionId(Integer.parseInt(objects[2].toString()));
        houseItemObject.reapplyFunctionId();
        return true;
    }
}
