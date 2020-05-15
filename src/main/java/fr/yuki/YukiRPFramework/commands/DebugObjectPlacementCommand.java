package fr.yuki.YukiRPFramework.commands;

import fr.yuki.YukiRPFramework.character.CharacterState;
import fr.yuki.YukiRPFramework.job.ObjectPlacementInstance;
import fr.yuki.YukiRPFramework.job.placementObject.GrowBoxPlacementInstance;
import fr.yuki.YukiRPFramework.manager.CharacterManager;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.CommandExecutor;

public class DebugObjectPlacementCommand implements CommandExecutor {
    @Override
    public boolean onCommand(Player player, String s, String[] args) {
        CharacterState characterState = CharacterManager.getCharacterStateByPlayer(player);
        ObjectPlacementInstance objectPlacementInstance = new GrowBoxPlacementInstance(50007, player.getLocation());
        characterState.setCurrentObjectPlacementInstance(objectPlacementInstance);
        objectPlacementInstance.spawn();
        objectPlacementInstance.setEditableBy(player);
        return true;
    }
}
