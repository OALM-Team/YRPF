package fr.yuki.yrpf.luaapi.vehicle;

import fr.yuki.yrpf.manager.VehicleManager;
import fr.yuki.yrpf.vehicle.storeLayout.StoreLayoutTransform;
import fr.yuki.yrpf.vehicle.storeLayout.VehicleStoreLayout;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

public class AddVehicleStoreLayoutItemPlacementEF implements ExportFunction {
    @Override
    public Object call(Object[] objects) {
        VehicleStoreLayout vehicleStoreLayout = VehicleManager.getVehicleStoreLayouts().stream()
                .filter(x -> x.isAdaptedForModel(Integer.parseInt(objects[0].toString()))).findFirst().orElse(null);
        if(vehicleStoreLayout == null) return false;
        int modelId = Integer.parseInt(objects[1].toString());
        int index = Integer.parseInt(objects[2].toString());
        StoreLayoutTransform storeLayoutTransform = new StoreLayoutTransform(
                index,
                new Vector(Double.parseDouble(objects[3].toString()), Double.parseDouble(objects[4].toString()),
                        Double.parseDouble(objects[5].toString())),
                new Vector(Double.parseDouble(objects[6].toString()), Double.parseDouble(objects[7].toString()),
                        Double.parseDouble(objects[8].toString())),
                new Vector(Double.parseDouble(objects[9].toString()), Double.parseDouble(objects[10].toString()),
                        Double.parseDouble(objects[11].toString()))
        );
        vehicleStoreLayout.addStoreTransform(modelId, storeLayoutTransform);
        return true;
    }
}
