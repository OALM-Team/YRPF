package fr.yuki.YukiRPFramework.commands;

import fr.yuki.YukiRPFramework.manager.InventoryManager;
import fr.yuki.YukiRPFramework.model.ItemTemplate;
import net.onfirenetwork.onsetjava.data.Location;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.CommandExecutor;

public class LocCommand implements CommandExecutor {
    @Override
    public boolean onCommand(Player player, String s, String[] args) {
        Location loc = player.getLocationAndHeading();
        player.sendMessage("Current loc x=" + loc.getX() + ", y=" + loc.getY() + ", z=" + loc.getZ() + ", h=" + loc.getHeading());
        return true;
    }
}
