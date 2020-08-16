package fr.yuki.yrpf.luaapi.vehicle;

import fr.yuki.yrpf.manager.VehicleManager;
import fr.yuki.yrpf.vehicle.storeLayout.CustomStoreLayout;
import fr.yuki.yrpf.vehicle.storeLayout.VehicleStoreLayout;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

public class CreateVehicleStoreLayoutEF implements ExportFunction {
    @Override
    public Object call(Object[] objects) {
        VehicleStoreLayout vehicleStoreLayout = VehicleManager.getVehicleStoreLayouts().stream()
                .filter(x -> x.isAdaptedForModel(Integer.parseInt(objects[0].toString()))).findFirst().orElse(null);
        if(vehicleStoreLayout != null) return false;
        VehicleManager.getVehicleStoreLayouts().add(new CustomStoreLayout(Integer.parseInt(objects[0].toString())));

        return true;
    }
}
