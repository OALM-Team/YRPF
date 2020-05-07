package fr.yuki.YukiRPFramework.job.tools;

import fr.yuki.YukiRPFramework.character.CharacterState;
import fr.yuki.YukiRPFramework.job.WearableWorldObject;
import fr.yuki.YukiRPFramework.manager.CharacterManager;
import fr.yuki.YukiRPFramework.manager.SoundManager;
import fr.yuki.YukiRPFramework.model.JobTool;
import net.onfirenetwork.onsetjava.entity.Player;

public class Sawmill implements JobToolHandler {
    private JobTool jobTool;

    public Sawmill(JobTool jobTool) {
        this.jobTool = jobTool;
    }

    @Override
    public boolean canInteract(Player player) {
        CharacterState state = CharacterManager.getCharacterStateByPlayer(player);
        if(state.getWearableWorldObject() == null) return false;
        if(state.getWearableWorldObject().getModelId() != 50001) return false;
        return true;
    }

    @Override
    public void onUnwear(Player player, WearableWorldObject wearableWorldObject) {
        SoundManager.playSound3D("sounds/saw_mill_cut.mp3", player.getLocation(), 1000, 0.2);
    }
}
