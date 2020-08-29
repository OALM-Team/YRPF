package fr.yuki.yrpf.job.tools;

import fr.yuki.yrpf.character.CharacterJobLevel;
import fr.yuki.yrpf.character.CharacterState;
import fr.yuki.yrpf.character.CharacterToolAnimation;
import fr.yuki.yrpf.enums.JobEnum;
import fr.yuki.yrpf.enums.ToastTypeEnum;
import fr.yuki.yrpf.i18n.I18n;
import fr.yuki.yrpf.job.WearableWorldObject;
import fr.yuki.yrpf.manager.*;
import fr.yuki.yrpf.modding.WorldParticle;
import fr.yuki.yrpf.model.Account;
import fr.yuki.yrpf.model.JobTool;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.entity.WorldObject;
import net.onfirenetwork.onsetjava.enums.Animation;

import java.util.ArrayList;

public class OreFurnace implements JobToolHandler {
    private JobTool jobTool;

    public OreFurnace(JobTool jobTool) {
        this.jobTool = jobTool;
    }

    @Override
    public boolean canInteract(Player player) {
        CharacterState state = CharacterManager.getCharacterStateByPlayer(player);
        if(state.getWearableWorldObject() == null) return false;
        if(state.getWearableWorldObject().getModelId() != 156) return false;
        return true;
}

    @Override
    public boolean onUnwear(Player player, WearableWorldObject wearableWorldObject) {
        Account account = WorldManager.getPlayerAccount(player);
        CharacterManager.getCharacterStateByPlayer(player).getWearableWorldObject().requestUnwear(player, true, 1, 1);
        WorldParticle particle = new WorldParticle("/Game/Vehicle/VFX/PS_VehicleSmoke",
                new Vector(this.jobTool.getPosition().getX(), this.jobTool.getPosition().getY(), this.jobTool.getPosition().getZ() + 50),
                new Vector(0.7, 0.7, 0.7), 5000);
        particle.start();
        SoundManager.playSound3D("sounds/furnace.mp3", this.jobTool.getPosition(), 2000, 0.6);

        Onset.delay(20000, () -> {
            particle.stop();

            WearableWorldObject cooperBar = new WearableWorldObject(50068, true,
                    Animation.CARRY_IDLE,
                    new CharacterToolAnimation(50068, new Vector(-10, 35 ,0),
                            new Vector(-10, 90, -90), new Vector(0.3, 0.3, 0.3), "hand_r"),
                    new Vector(this.jobTool.getX() + 100, this.jobTool.getY() + 100, this.jobTool.getZ() + 50));
            JobManager.getWearableWorldObjects().add(cooperBar);
        });
        return false;
    }

    @Override
    public boolean hasLevelRequired(Player player) {
        Account account = WorldManager.getPlayerAccount(player);
        ArrayList<CharacterJobLevel> characterJobLevels = account.decodeCharacterJob();
        CharacterJobLevel characterJobLevel = characterJobLevels.stream().filter(x -> x.getJobId().equals(JobEnum.MINER.name()))
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
