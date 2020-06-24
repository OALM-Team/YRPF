package fr.yuki.YukiRPFramework.commands;

import fr.yuki.YukiRPFramework.manager.TimeManager;
import fr.yuki.YukiRPFramework.manager.WorldManager;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.CommandExecutor;

public class SetTimeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(Player player, String s, String[] args) {
        if(WorldManager.getPlayerAccount(player).getAdminLevel() == 0) return false;
        TimeManager.setCurrentHour(Integer.parseInt(args[0]));
        return true;
    }
}
