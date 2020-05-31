package fr.yuki.YukiRPFramework.commands;

import fr.yuki.YukiRPFramework.dao.HouseDAO;
import fr.yuki.YukiRPFramework.manager.HouseManager;
import fr.yuki.YukiRPFramework.manager.WorldManager;
import fr.yuki.YukiRPFramework.model.House;
import fr.yuki.YukiRPFramework.model.HouseItemObject;
import net.onfirenetwork.onsetjava.entity.Door;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.CommandExecutor;

import java.sql.SQLException;

public class FreeHouseCommand implements CommandExecutor {
    @Override
    public boolean onCommand(Player player, String s, String[] args) {
        if(WorldManager.getPlayerAccount(player).getAdminLevel() == 0) return false;
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
        }
        try {
            HouseDAO.saveHouse(house);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        player.sendMessage("This house is free now");
        return true;
    }
}
