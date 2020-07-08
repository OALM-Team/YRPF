package fr.yuki.yrpf.job.tools;

import fr.yuki.yrpf.character.CharacterState;
import fr.yuki.yrpf.enums.ToastTypeEnum;
import fr.yuki.yrpf.i18n.I18n;
import fr.yuki.yrpf.job.WearableWorldObject;
import fr.yuki.yrpf.manager.CharacterManager;
import fr.yuki.yrpf.manager.UIStateManager;
import fr.yuki.yrpf.manager.WorldManager;
import fr.yuki.yrpf.modding.LoopSound3D;
import fr.yuki.yrpf.model.Account;
import fr.yuki.yrpf.model.JobTool;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;

public class Generator implements JobToolHandler {
    private final JobTool jobTool;
    private final LoopSound3D loopSound3D;
    private boolean isOn;
    private int fuel;

    public Generator(JobTool jobTool) {
        this.jobTool = jobTool;
        this.fuel = 0;
        this.isOn = false;
        this.loopSound3D = new LoopSound3D("sounds/power_generator.mp3",
                new Vector(this.jobTool.getX(), this.jobTool.getY(), this.jobTool.getZ()), 2000, 0.05,
                1000*90);
    }

    @Override
    public boolean canInteract(Player player) {
        CharacterState state = CharacterManager.getCharacterStateByPlayer(player);
        if(state.getWearableWorldObject() == null) return false;
        return true;
    }

    @Override
    public boolean onUnwear(Player player, WearableWorldObject wearableWorldObject) {
        return false;
    }

    @Override
    public boolean hasLevelRequired(Player player) {
        return true;
    }

    @Override
    public boolean onUse(Player player) {
        Account account = WorldManager.getPlayerAccount(player);
        if(!this.isOn) {
            if(this.fuel > 0)
                this.setOn(true);
            else
                UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, I18n.t(account.getLang(), "toast.generator.no_fuel"));
        } else {
            this.setOn(false);
        }
        return true;
    }

    @Override
    public boolean canBeUse() {
        return true;
    }

    public void tickFuel() {
        if(!this.isOn) return;

        this.fuel -= 1;
        if(this.fuel <= 0) this.setOn(false);
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
        if(isOn) {
            this.loopSound3D.start();
        } else {
            this.loopSound3D.stop();
        }
    }

    public int getFuel() {
        return fuel;
    }

    public void setFuel(int fuel) {
        this.fuel = fuel;
    }
}
