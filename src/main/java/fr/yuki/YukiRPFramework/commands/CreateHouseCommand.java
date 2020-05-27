package fr.yuki.YukiRPFramework.commands;

import fr.yuki.YukiRPFramework.character.CharacterState;
import fr.yuki.YukiRPFramework.manager.CharacterManager;
import fr.yuki.YukiRPFramework.manager.HouseManager;
import fr.yuki.YukiRPFramework.manager.WorldManager;
import fr.yuki.YukiRPFramework.model.Account;
import fr.yuki.YukiRPFramework.model.House;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.CommandExecutor;

public class CreateHouseCommand implements CommandExecutor {
    @Override
    public boolean onCommand(Player player, String s, String[] strings) {
        if(WorldManager.getPlayerAccount(player).getAdminLevel() == 0) return false;
        CharacterState state = CharacterManager.getCharacterStateByPlayer(player);
        if(state.getCurrentDisplayedLine3D() == null) return true;

        Account account = WorldManager.getPlayerAccount(player);
        House house = new House();
        house.setAccountId(account.getId());
        house.setPrice(100);
        house.setName("House");
        house.setSx(state.getCurrentDisplayedLine3D().getsX());
        house.setSy(state.getCurrentDisplayedLine3D().getsY());
        house.setSz(state.getCurrentDisplayedLine3D().getsZ());
        house.setEx(state.getCurrentDisplayedLine3D().geteX());
        house.setEy(state.getCurrentDisplayedLine3D().geteY());
        house.setEz(state.getCurrentDisplayedLine3D().geteZ());
        HouseManager.getHouses().add(house);
        return true;
    }
}
