package fr.yuki.yrpf.luaapi.vehicle;

import fr.yuki.yrpf.vehicle.ChestSize;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

public class SetVehicleItemChestSizeEF implements ExportFunction {
    @Override
    public Object call(Object[] objects) {
        ChestSize.getCustomChestSize().put(Integer.parseInt(objects[0].toString()), Integer.parseInt(objects[1].toString()));
        return null;
    }
}
