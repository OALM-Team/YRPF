package fr.yuki.YukiRPFramework.job.tools;

import fr.yuki.YukiRPFramework.character.CharacterState;
import fr.yuki.YukiRPFramework.character.CharacterToolAnimation;
import fr.yuki.YukiRPFramework.enums.ItemTemplateEnum;
import fr.yuki.YukiRPFramework.enums.JobEnum;
import fr.yuki.YukiRPFramework.enums.ToastTypeEnum;
import fr.yuki.YukiRPFramework.i18n.I18n;
import fr.yuki.YukiRPFramework.job.WearableWorldObject;
import fr.yuki.YukiRPFramework.manager.*;
import fr.yuki.YukiRPFramework.model.JobTool;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.entity.WorldObject;
import net.onfirenetwork.onsetjava.enums.Animation;

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
        JobManager.addExp(player, JobEnum.GARBAGE, 20);

        InventoryManager.addItemToPlayer(player, ItemTemplateEnum.CASH.id, this.jobTool.getReward());
        SoundManager.playSound3D("sounds/cash_register.mp3", player.getLocation(), 200, 0.3);
        CharacterManager.getCharacterStateByPlayer(player).getWearableWorldObject().requestUnwear(player, true);
        UIStateManager.sendNotification(player, ToastTypeEnum.SUCCESS, "Vous avez vendu votre ressource pour " + this.jobTool.getReward() + "$");
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
