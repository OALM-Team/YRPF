package fr.yuki.YukiRPFramework.commands;

import fr.yuki.YukiRPFramework.character.CharacterState;
import fr.yuki.YukiRPFramework.job.ObjectPlacementInstance;
import fr.yuki.YukiRPFramework.job.placementObject.GenericPlacementInstance;
import fr.yuki.YukiRPFramework.job.placementObject.GrowBoxPlacementInstance;
import fr.yuki.YukiRPFramework.manager.CharacterManager;
import fr.yuki.YukiRPFramework.manager.WorldManager;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.CommandExecutor;

public class DebugObjectPlacementCommand implements CommandExecutor {
    @Override
    public boolean onCommand(Player player, String s, String[] args) {
        if(WorldManager.getPlayerAccount(player).getAdminLevel() == 0) return false;
        if(WorldManager.getPlayerAccount(player).getCommandLevel() < 3) {
            player.sendMessage("You don't have the level required for this command");
            return true;
        }
        CharacterState characterState = CharacterManager.getCharacterStateByPlayer(player);
        ObjectPlacementInstance objectPlacementInstance = new GenericPlacementInstance(player.getLocation(), Integer.parseInt(args[0]), 0);
        characterState.setCurrentObjectPlacementInstance(objectPlacementInstance);
        objectPlacementInstance.spawn();
        objectPlacementInstance.setEditableBy(player);
        return true;
    }
}
