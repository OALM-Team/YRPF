package fr.yuki.YukiRPFramework.commands;

import fr.yuki.YukiRPFramework.dao.HouseDAO;
import fr.yuki.YukiRPFramework.manager.HouseManager;
import fr.yuki.YukiRPFramework.manager.WorldManager;
import fr.yuki.YukiRPFramework.model.House;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.CommandExecutor;

import java.sql.SQLException;

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
        try {
            HouseDAO.saveHouse(house);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        player.sendMessage("House props set");
        return true;
    }
}
