package fr.yuki.YukiRPFramework.commands;

import fr.yuki.YukiRPFramework.manager.VehicleManager;
import fr.yuki.YukiRPFramework.manager.WorldManager;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.entity.Vehicle;
import net.onfirenetwork.onsetjava.plugin.CommandExecutor;

public class MoveCommand implements CommandExecutor {

    @Override
    public boolean onCommand(Player player, String s, String[] strings) {
        if(WorldManager.getPlayerAccount(player).getAdminLevel() == 0) return false;
        Vehicle vehicle = VehicleManager.getNearestVehicle(player.getLocation());
        vehicle.setLocation(new Vector(player.getLocation().getX() + 250, player.getLocation().getY() + 250, player.getLocation().getZ()));
        return true;
    }
}
