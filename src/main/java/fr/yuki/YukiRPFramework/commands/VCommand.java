package fr.yuki.YukiRPFramework.commands;

import fr.yuki.YukiRPFramework.manager.VehicleManager;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.entity.Vehicle;
import net.onfirenetwork.onsetjava.plugin.CommandExecutor;

public class VCommand implements CommandExecutor {
    @Override
    public boolean onCommand(Player player, String s, String[] args) {
        int modelId = Integer.parseInt(args[0]);
        VehicleManager.createVehicle(modelId, player.getLocation(),
                player.getLocationAndHeading().getHeading(), player, null);
        return true;
    }
}
