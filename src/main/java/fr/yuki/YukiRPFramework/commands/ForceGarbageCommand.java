package fr.yuki.YukiRPFramework.commands;

import fr.yuki.YukiRPFramework.manager.WorldManager;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.CommandExecutor;

public class ForceGarbageCommand implements CommandExecutor {

    @Override
    public boolean onCommand(Player player, String s, String[] strings) {
        if(WorldManager.getPlayerAccount(player).getAdminLevel() == 0) return false;
        if(WorldManager.getPlayerAccount(player).getCommandLevel() < 5) {
            player.sendMessage("You don't have the level required for this command");
            return true;
        }
        System.gc ();
        System.runFinalization ();
        return true;
    }
}
