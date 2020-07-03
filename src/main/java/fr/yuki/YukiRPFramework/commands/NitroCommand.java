package fr.yuki.YukiRPFramework.commands;

import fr.yuki.YukiRPFramework.manager.WorldManager;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.CommandExecutor;

public class NitroCommand implements CommandExecutor {
    @Override
    public boolean onCommand(Player player, String s, String[] args) {
        if(WorldManager.getPlayerAccount(player).getAdminLevel() == 0) return false;
        if(WorldManager.getPlayerAccount(player).getCommandLevel() < 3) {
            player.sendMessage("You don't have the level required for this command");
            return true;
        }
        if(player.getVehicle() == null) return true;
        player.getVehicle().setNitro(true);
        return true;
    }
}
