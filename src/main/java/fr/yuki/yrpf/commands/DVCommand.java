package fr.yuki.yrpf.commands;

import fr.yuki.yrpf.manager.GarageManager;
import fr.yuki.yrpf.manager.VehicleManager;
import fr.yuki.yrpf.manager.WorldManager;
import fr.yuki.yrpf.model.VehicleGarage;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.entity.Vehicle;
import net.onfirenetwork.onsetjava.plugin.CommandExecutor;

public class DVCommand implements CommandExecutor {
    @Override
    public boolean onCommand(Player player, String s, String[] strings) {
        if(WorldManager.getPlayerAccount(player).getAdminLevel() == 0) return false;
        Vehicle vehicle = VehicleManager.getNearestVehicle(player.getLocation());
        if(vehicle == null) return true;
        VehicleGarage vehicleGarage = GarageManager.getVehicleGarages().stream()
                .filter(x -> x.getUuid().equals(vehicle.getPropertyString("uuid")))
                .findFirst().orElse(null);
        if(vehicleGarage == null) return true;
        if(!vehicleGarage.isRental()) {
            vehicleGarage.setGarageId(vehicleGarage.getGarageLastId());
            vehicleGarage.save();
        }
        if(vehicle.getDriver() != null) vehicle.getDriver().exitVehicle();

        Onset.delay(1000, () -> {
            vehicle.destroy();
        });
        return true;
    }
}
