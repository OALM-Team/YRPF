package fr.yuki.yrpf.commands;

import fr.yuki.yrpf.manager.HouseManager;
import fr.yuki.yrpf.manager.WorldManager;
import fr.yuki.yrpf.model.House;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.CommandExecutor;

public class SetHousePropsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(Player player, String s, String[] args) {
        if(WorldManager.getPlayerAccount(player).getAdminLevel() == 0) return false;
        if(WorldManager.getPlayerAccount(player).getCommandLevel() < 3) {
            player.sendMessage("You don't have the level required for this command");
            return true;
        }
        House house = HouseManager.getHouseAtLocation(player.getLocation());
        if(house == null) {
            house = HouseManager.getHouseAtLocation(WorldManager.getNearestDoor(player.getLocation()).getLocation());
        }
        if(house == null) return true;

        house.setPrice(Integer.parseInt(args[0]));
        house.setName(args[1]);
        house.save();

        player.sendMessage("House props set");
        return true;
    }
}
