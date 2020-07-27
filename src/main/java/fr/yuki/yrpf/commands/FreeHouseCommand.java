package fr.yuki.yrpf.commands;

import fr.yuki.yrpf.manager.HouseManager;
import fr.yuki.yrpf.manager.WorldManager;
import fr.yuki.yrpf.model.House;
import fr.yuki.yrpf.model.HouseItemObject;
import net.onfirenetwork.onsetjava.entity.Door;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.CommandExecutor;

public class FreeHouseCommand implements CommandExecutor {
    @Override
    public boolean onCommand(Player player, String s, String[] args) {
        if(WorldManager.getPlayerAccount(player).getAdminLevel() == 0) return false;
        if(WorldManager.getPlayerAccount(player).getCommandLevel() < 2) {
            player.sendMessage("You don't have the level required for this command");
            return true;
        }
        House house = HouseManager.getHouseAtLocation(player.getLocation());
        if(house == null) {
            house = HouseManager.getHouseAtLocation(WorldManager.getNearestDoor(player.getLocation()).getLocation());
        }
        if(house == null) return true;
        house.setAccountId(-1);
        for(Door door : house.getLine3D().getDoorsInside()) {
            door.close();
        }
        for(HouseItemObject houseItemObject : house.getHouseItemObjects()) {
            houseItemObject.destroy();
            houseItemObject.delete();
        }
        house.getHouseItemObjects().clear();
        house.save();

        player.sendMessage("This house is free now");
        return true;
    }
}
