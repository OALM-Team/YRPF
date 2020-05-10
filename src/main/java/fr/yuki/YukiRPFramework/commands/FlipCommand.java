package fr.yuki.YukiRPFramework.commands;

import fr.yuki.YukiRPFramework.manager.VehicleManager;
import fr.yuki.YukiRPFramework.manager.WorldManager;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.entity.Vehicle;
import net.onfirenetwork.onsetjava.plugin.CommandExecutor;

public class FlipCommand implements CommandExecutor {
    @Override
    public boolean onCommand(Player player, String s, String[] strings) {
        if(WorldManager.getPlayerAccount(player).getAdminLevel() == 0) return false;
        Vehicle vehicle = VehicleManager.getNearestVehicle(player.getLocation());
        if(vehicle == null) return true;
        vehicle.setRotation(new Vector(0 , vehicle.getRotation().getY(),0));
        return true;
    }
}
