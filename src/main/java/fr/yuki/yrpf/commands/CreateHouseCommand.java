package fr.yuki.yrpf.commands;

import fr.yuki.yrpf.character.CharacterState;
import fr.yuki.yrpf.manager.CharacterManager;
import fr.yuki.yrpf.manager.HouseManager;
import fr.yuki.yrpf.manager.WorldManager;
import fr.yuki.yrpf.model.Account;
import fr.yuki.yrpf.model.House;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.CommandExecutor;

public class CreateHouseCommand implements CommandExecutor {
    @Override
    public boolean onCommand(Player player, String s, String[] strings) {
        if(WorldManager.getPlayerAccount(player).getAdminLevel() == 0) return false;
        if(WorldManager.getPlayerAccount(player).getCommandLevel() < 3) {
            player.sendMessage("You don't have the level required for this command");
            return true;
        }
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
        house.save();
        Onset.print("House created id: " + house.getId());
        player.sendMessage("House created id: " + house.getId());

        return true;
    }
}
