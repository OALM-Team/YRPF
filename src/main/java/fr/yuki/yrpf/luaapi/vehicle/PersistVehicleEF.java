package fr.yuki.yrpf.luaapi.vehicle;

import fr.yuki.yrpf.enums.ToastTypeEnum;
import fr.yuki.yrpf.i18n.I18n;
import fr.yuki.yrpf.manager.GarageManager;
import fr.yuki.yrpf.manager.UIStateManager;
import fr.yuki.yrpf.model.VehicleGarage;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Vehicle;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

public class PersistVehicleEF implements ExportFunction {
    @Override
    public Object call(Object[] objects) {
        Vehicle vehicle = Onset.getVehicle(Integer.parseInt(objects[0].toString()));
        if(vehicle == null) return false;
        VehicleGarage vehicleGarage = GarageManager.getVehicleGarages().stream().filter(x -> x.getUuid().equals(vehicle.getPropertyString("uuid")))
                .findFirst().orElse(null);
        if(vehicleGarage == null || vehicleGarage.isRental()) {
            return false;
        }
        vehicleGarage.computeDamages(vehicle);
        vehicleGarage.setColor("#" + Integer.toHexString(vehicle.getColor().getRed()) + Integer.toHexString(vehicle.getColor().getGreen()) + Integer.toHexString(vehicle.getColor().getBlue()));
        vehicleGarage.setLicencePlate(vehicle.getLicensePlate());
        vehicleGarage.save();
        return null;
    }
}
