package fr.yuki.YukiRPFramework.commands;

import fr.yuki.YukiRPFramework.manager.GarageManager;
import fr.yuki.YukiRPFramework.manager.VehicleManager;
import fr.yuki.YukiRPFramework.manager.WorldManager;
import fr.yuki.YukiRPFramework.model.VehicleGarage;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.entity.Vehicle;
import net.onfirenetwork.onsetjava.plugin.CommandExecutor;

import java.util.stream.Collectors;

public class DVCommand implements CommandExecutor {
    @Override
    public boolean onCommand(Player player, String s, String[] strings) {
        if(WorldManager.getPlayerAccount(player).getAdminLevel() == 0) return false;
        Vehicle vehicle = VehicleManager.getNearestVehicle(player.getLocation());
        if(vehicle == null) return true;
        VehicleGarage vehicleGarage = GarageManager.getVehicleGarages().stream()
                .filter(x -> x.getUuid().equals(vehicle.getPropertyString("uuid")))
                .findFirst().orElse(null);
        if(vehicleGarage == null);
        if(!vehicleGarage.isRental()) vehicleGarage.setGarageId(1);
        vehicle.destroy();
        return true;
    }
}
