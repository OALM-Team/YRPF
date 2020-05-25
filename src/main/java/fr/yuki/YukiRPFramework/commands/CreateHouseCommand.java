package fr.yuki.YukiRPFramework.commands;

import fr.yuki.YukiRPFramework.character.CharacterState;
import fr.yuki.YukiRPFramework.manager.CharacterManager;
import fr.yuki.YukiRPFramework.manager.HouseManager;
import fr.yuki.YukiRPFramework.manager.WorldManager;
import fr.yuki.YukiRPFramework.modding.Line3D;
import fr.yuki.YukiRPFramework.model.Account;
import fr.yuki.YukiRPFramework.model.House;
import net.onfirenetwork.onsetjava.entity.Door;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.CommandExecutor;

public class CreateHouseCommand implements CommandExecutor {
    @Override
    public boolean onCommand(Player player, String s, String[] args) {
        Line3D line3D = new Line3D(Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]),
                Double.parseDouble(args[3]), Double.parseDouble(args[4]), Double.parseDouble(args[5]), Integer.parseInt(args[6]));
        CharacterState state = CharacterManager.getCharacterStateByPlayer(player);
        if(state.getCurrentDisplayedLine3D() != null) {
            state.getCurrentDisplayedLine3D().hide(player);
        }
        state.setCurrentDisplayedLine3D(line3D);
        line3D.show(player);
        player.sendMessage("IsInside : " + line3D.isInside(player.getLocation()));
        for(Door door : line3D.getDoorsInside()) {
            player.sendMessage("DoorId: " + door.getId());
            if(!door.isOpen()) {
                door.open();
            }
        }

        Account account = WorldManager.getPlayerAccount(player);
        House house = new House();
        house.setAccountId(account.getId());
        house.setPrice(100);
        house.setName("House");
        house.setSx(line3D.getsX());
        house.setSy(line3D.getsY());
        house.setSz(line3D.getsZ());
        house.setEx(line3D.geteX());
        house.setEy(line3D.geteY());
        house.setEz(line3D.geteZ());
        HouseManager.getHouses().add(house);

        return true;
    }
}
