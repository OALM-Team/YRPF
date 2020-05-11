package fr.yuki.YukiRPFramework.commands;

import fr.yuki.YukiRPFramework.manager.GarageManager;
import fr.yuki.YukiRPFramework.manager.VehicleManager;
import fr.yuki.YukiRPFramework.manager.WorldManager;
import fr.yuki.YukiRPFramework.model.VehicleGarage;
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
        if(!vehicleGarage.isRental()) vehicleGarage.setGarageId(1);
        if(vehicle.getDriver() != null) vehicle.getDriver().exitVehicle();

        Onset.delay(1000, () -> {
            vehicle.destroy();
        });
        return true;
    }
}
