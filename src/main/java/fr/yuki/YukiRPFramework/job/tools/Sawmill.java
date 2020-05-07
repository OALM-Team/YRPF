package fr.yuki.YukiRPFramework.job.tools;

import fr.yuki.YukiRPFramework.character.CharacterState;
import fr.yuki.YukiRPFramework.character.CharacterToolAnimation;
import fr.yuki.YukiRPFramework.enums.ToastTypeEnum;
import fr.yuki.YukiRPFramework.job.WearableWorldObject;
import fr.yuki.YukiRPFramework.manager.*;
import fr.yuki.YukiRPFramework.model.JobTool;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.entity.WorldObject;
import net.onfirenetwork.onsetjava.enums.Animation;

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
        if(!this.isAvailable) {
            UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, "Cette outil est déjà en cours d'utilisation");
            return false;
        }
        this.isAvailable = false;
        WorldObject worldObject = Onset.getServer().createObject(new Vector(0,0,0), wearableWorldObject.getModelId());
        if(ModdingManager.isCustomModelId(wearableWorldObject.getModelId()))
            ModdingManager.assignCustomModel(worldObject, wearableWorldObject.getModelId());
        SoundManager.playSound3D("sounds/saw_mill_cut.mp3", player.getLocation(), 1000, 0.2);

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
}
