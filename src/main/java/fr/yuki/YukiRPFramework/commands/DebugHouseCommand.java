package fr.yuki.YukiRPFramework.commands;

import fr.yuki.YukiRPFramework.character.CharacterState;
import fr.yuki.YukiRPFramework.manager.CharacterManager;
import fr.yuki.YukiRPFramework.manager.HouseManager;
import fr.yuki.YukiRPFramework.manager.WorldManager;
import fr.yuki.YukiRPFramework.modding.Line3D;
import fr.yuki.YukiRPFramework.model.Account;
import fr.yuki.YukiRPFramework.model.House;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Door;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.CommandExecutor;

public class DebugHouseCommand implements CommandExecutor {
    @Override
    public boolean onCommand(Player player, String s, String[] args) {
        if(WorldManager.getPlayerAccount(player).getAdminLevel() == 0) return false;
        if(WorldManager.getPlayerAccount(player).getCommandLevel() < 3) {
            player.sendMessage("You don't have the level required for this command");
            return true;
        }
        CharacterState state = CharacterManager.getCharacterStateByPlayer(player);
        Vector firstLoc = state.getLastLocationsRequest().get(state.getLastLocationsRequest().size() - 1);
        Vector secondLoc = state.getLastLocationsRequest().get(state.getLastLocationsRequest().size() - 2);

        Line3D line3D = new Line3D(firstLoc.getX(), firstLoc.getY(), firstLoc.getZ(),
                secondLoc.getX(), secondLoc.getY(), secondLoc.getZ(), 5);
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
            } else {
                door.close();
            }
        }

        return true;
    }
}
