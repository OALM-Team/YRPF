package fr.yuki.yrpf.job.tools;

import fr.yuki.yrpf.character.CharacterJobLevel;
import fr.yuki.yrpf.character.CharacterState;
import fr.yuki.yrpf.character.CharacterToolAnimation;
import fr.yuki.yrpf.enums.JobEnum;
import fr.yuki.yrpf.enums.ToastTypeEnum;
import fr.yuki.yrpf.i18n.I18n;
import fr.yuki.yrpf.job.WearableWorldObject;
import fr.yuki.yrpf.manager.*;
import fr.yuki.yrpf.model.Account;
import fr.yuki.yrpf.model.JobTool;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.entity.WorldObject;
import net.onfirenetwork.onsetjava.enums.Animation;

import java.util.ArrayList;

public class Sawmill implements JobToolHandler {
    private JobTool jobTool;
    private boolean isAvailable;

    public Sawmill(JobTool jobTool) {
        this.jobTool = jobTool;
        this.isAvailable = true;
    }

    @Override
    public boolean canInteract(Player player) {
        CharacterState state = CharacterManager.getCharacterStateByPlayer(player);
        if(state.getWearableWorldObject() == null) return false;
        if(state.getWearableWorldObject().getModelId() != 50001) return false;
        return true;
    }

    @Override
    public boolean onUnwear(Player player, WearableWorldObject wearableWorldObject) {
        Account account = WorldManager.getPlayerAccount(player);
        if(!this.isAvailable) {
            UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, I18n.t(account.getLang(), "toast.tool.already_used"));
            return false;
        }
        this.isAvailable = false;
        WorldObject worldObject = Onset.getServer().createObject(new Vector(0,0,0), wearableWorldObject.getModelId());
        if(ModdingManager.isCustomModelId(wearableWorldObject.getModelId()))
            ModdingManager.assignCustomModel(worldObject, wearableWorldObject.getModelId());
        SoundManager.playSound3D("sounds/saw_mill_cut.mp3", player.getLocation(), 1000, 0.8);
        JobManager.addExp(player, JobEnum.LUMBERJACK.name(), 20);
        worldObject.attach(this.jobTool.getWorldObject(), new Vector(0, 0,160), new Vector(0, 0, 0), "");
        Onset.delay(8000, () -> {
            worldObject.destroy();
            this.isAvailable = true;

            // Create the plank
            WearableWorldObject woodPlank = new WearableWorldObject(1574, true,
                    Animation.CARRY_SHOULDER_IDLE,
                    new CharacterToolAnimation(1574, new Vector(10, 10 ,20),
                            new Vector(0, 0, 90), new Vector(0.5, 0.5, 0.5), "hand_r"),
                    new Vector(this.jobTool.getX() + 100, this.jobTool.getY() + 100, this.jobTool.getZ() + 50));
            JobManager.getWearableWorldObjects().add(woodPlank);
        });
        return true;
    }

    @Override
    public boolean hasLevelRequired(Player player) {
        Account account = WorldManager.getPlayerAccount(player);
        ArrayList<CharacterJobLevel> characterJobLevels = account.decodeCharacterJob();
        CharacterJobLevel characterJobLevel = characterJobLevels.stream().filter(x -> x.getJobId().equals(JobEnum.LUMBERJACK.name()))
                .findFirst().orElse(null);
        if(characterJobLevel == null) return false;
        if(characterJobLevel.getJobLevel().getLevel() < 2) return false;
        return true;
    }

    @Override
    public boolean onUse(Player player) {
        return false;
    }

    @Override
    public boolean canBeUse() {
        return false;
    }
}
