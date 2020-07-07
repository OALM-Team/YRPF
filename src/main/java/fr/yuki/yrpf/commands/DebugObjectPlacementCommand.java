package fr.yuki.yrpf.commands;

import fr.yuki.yrpf.character.CharacterState;
import fr.yuki.yrpf.job.ObjectPlacementInstance;
import fr.yuki.yrpf.job.placementObject.GenericPlacementInstance;
import fr.yuki.yrpf.manager.CharacterManager;
import fr.yuki.yrpf.manager.WorldManager;
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
