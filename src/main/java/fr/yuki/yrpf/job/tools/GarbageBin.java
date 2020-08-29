package fr.yuki.yrpf.job.tools;

import fr.yuki.yrpf.character.CharacterState;
import fr.yuki.yrpf.enums.ItemTemplateEnum;
import fr.yuki.yrpf.enums.JobEnum;
import fr.yuki.yrpf.enums.ToastTypeEnum;
import fr.yuki.yrpf.job.WearableWorldObject;
import fr.yuki.yrpf.manager.*;
import fr.yuki.yrpf.model.JobTool;
import net.onfirenetwork.onsetjava.entity.Player;

public class GarbageBin implements JobToolHandler {
    private JobTool jobTool;

    public GarbageBin(JobTool jobTool) {
        this.jobTool = jobTool;
    }

    @Override
    public boolean canInteract(Player player) {
        CharacterState state = CharacterManager.getCharacterStateByPlayer(player);
        if(state.getWearableWorldObject() == null) return false;
        if(state.getWearableWorldObject().getModelId() != 514) return false;
        return true;
    }

    @Override
    public boolean onUnwear(Player player, WearableWorldObject wearableWorldObject) {
        JobManager.addExp(player, JobEnum.GARBAGE.name(), 20);

        int reward = this.jobTool.getReward();
        if(JobManager.getJobLevelForPlayer(player, this.jobTool.getJobType()) == 2) {
            reward = (int)Math.floor(reward * 1.5d);
        }

        InventoryManager.addItemToPlayer(player, ItemTemplateEnum.CASH.id, reward, true);
        SoundManager.playSound3D("sounds/cash_register.mp3", player.getLocation(), 200, 1);
        CharacterManager.getCharacterStateByPlayer(player).getWearableWorldObject().requestUnwear(player, true,1 ,1);

        UIStateManager.sendNotification(player, ToastTypeEnum.SUCCESS, "Vous avez vendu votre ressource pour " + reward + "$");
        return false;
    }

    @Override
    public boolean hasLevelRequired(Player player) {
        return true;
    }

    @Override
    public boolean onUse(Player player) {
        return true;
    }

    @Override
    public boolean canBeUse() {
        return false;
    }
}
