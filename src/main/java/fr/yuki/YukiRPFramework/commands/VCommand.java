package fr.yuki.YukiRPFramework.commands;

import fr.yuki.YukiRPFramework.manager.VehicleManager;
import fr.yuki.YukiRPFramework.manager.WorldManager;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.entity.Vehicle;
import net.onfirenetwork.onsetjava.plugin.CommandExecutor;

public class VCommand implements CommandExecutor {
    @Override
    public boolean onCommand(Player player, String s, String[] args) {
        if(WorldManager.getPlayerAccount(player).getAdminLevel() == 0) return false;
        if(WorldManager.getPlayerAccount(player).getCommandLevel() < 3) {
            player.sendMessage("You don't have the level required for this command");
            return true;
        }
        int modelId = Integer.parseInt(args[0]);
        VehicleManager.createVehicle(modelId, player.getLocation(),
                player.getLocationAndHeading().getHeading(), player, null, false);
        return true;
    }
}
