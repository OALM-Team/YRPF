package fr.yuki.YukiRPFramework.commands;

import fr.yuki.YukiRPFramework.character.CharacterState;
import fr.yuki.YukiRPFramework.dao.HouseDAO;
import fr.yuki.YukiRPFramework.manager.CharacterManager;
import fr.yuki.YukiRPFramework.manager.HouseManager;
import fr.yuki.YukiRPFramework.manager.WorldManager;
import fr.yuki.YukiRPFramework.model.Account;
import fr.yuki.YukiRPFramework.model.House;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.CommandExecutor;

import java.sql.SQLException;

public class CreateHouseCommand implements CommandExecutor {
    @Override
    public boolean onCommand(Player player, String s, String[] strings) {
        if(WorldManager.getPlayerAccount(player).getAdminLevel() == 0) return false;
        CharacterState state = CharacterManager.getCharacterStateByPlayer(player);
        //if(state.getCurrentDisplayedLine3D() == null) return true;

        Vector firstLoc = state.getLastLocationsRequest().get(state.getLastLocationsRequest().size() - 1);
        Vector secondLoc = state.getLastLocationsRequest().get(state.getLastLocationsRequest().size() - 2);

        Account account = WorldManager.getPlayerAccount(player);
        House house = new House();
        house.setAccountId(account.getId());
        house.setPrice(2500);
        house.setName("Maison");
        house.setSx(firstLoc.getX());
        house.setSy(firstLoc.getY());
        house.setSz(firstLoc.getZ());
        house.setEx(secondLoc.getX());
        house.setEy(secondLoc.getY());
        house.setEz(secondLoc.getZ());
        HouseManager.getHouses().add(house);
        try {
            HouseDAO.insertHouse(house);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Onset.print("House created id: " + house.getId());
        player.sendMessage("House created id: " + house.getId());

        return true;
    }
}
