package fr.yuki.yrpf.job;

import fr.yuki.yrpf.character.CharacterJobLevel;
import fr.yuki.yrpf.character.CharacterLoopAnimation;
import fr.yuki.yrpf.enums.ToastTypeEnum;
import fr.yuki.yrpf.i18n.I18n;
import fr.yuki.yrpf.job.harvest.HarvestableObject;
import fr.yuki.yrpf.manager.*;
import fr.yuki.yrpf.model.Account;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.entity.WorldObject;

import java.util.ArrayList;
import java.util.UUID;

public class WorldHarvestObject {
    private static int _id = 10000;
    private String uuid;
    private JobSpawn spawn;
    private JobSpawnPosition jobSpawnPosition;
    private HarvestableObject harvestableObject;
    private WorldObject worldObject;
    private boolean available;
    private Job job;

    public WorldHarvestObject(JobSpawn spawn, JobSpawnPosition jobSpawnPosition,
                              HarvestableObject harvestableObject, WorldObject worldObject, Job job) {
        this.job = job;
        this.uuid = UUID.randomUUID().toString() + "-" + (--_id);
        this.spawn = spawn;
        this.jobSpawnPosition = jobSpawnPosition;
        this.harvestableObject = harvestableObject;
        this.worldObject = worldObject;
        this.setAvailable(true);
    }

    /**
     * Check if this resource is near the player
     * @param player The player
     * @return Is near
     */
    public boolean isNear(Player player) {
        if(this.worldObject.getLocation().distance(player.getLocation()) <= this.harvestableObject.distanceToInteract()) {
            return true;
        }
        return false;
    }

    /**
     * Harvest the resource
     * @param player The player
     */
    public void harvest(Player player) {
        Account account = WorldManager.getPlayerAccount(player);
        if(!this.harvestableObject.checkRequirements(player)) {
            UIStateManager.sendNotification(player, ToastTypeEnum.ERROR,
                    I18n.t(account.getLang(), "toast.job.requirement_unrespected"));
            return;
        }
        if(!this.available) {
            UIStateManager.sendNotification(player, ToastTypeEnum.ERROR,
                    I18n.t(account.getLang(), "toast.job.already_used"));
            return;
        }

        // Check resources level
        ArrayList<CharacterJobLevel> characterJobLevels = account.decodeCharacterJob();
        CharacterJobLevel characterJobLevel = characterJobLevels.stream().filter(x -> x.getJobId().equals(this.job.getJobType()))
                .findFirst().orElse(null);
        if(characterJobLevel == null) return;
        if(characterJobLevel.getJobLevel().getLevel() < this.harvestableObject.getLevelRequired()) {
            UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, I18n.t(account.getLang(), "toast.tool.no_level_required"));
            return;
        }

        Onset.print("Harvest " + this.harvestableObject.getName() + " with a duration of " + this.harvestableObject.getBaseHarvestTime() + "ms");
        this.setAvailable(false);
        CharacterManager.setCharacterFreeze(player, true);
        CharacterLoopAnimation characterLoopAnimation = harvestableObject.getCharacterLoopHarvestAnimation(player);
        characterLoopAnimation.start();
        Onset.delay(this.harvestableObject.getBaseHarvestTime(), () -> {
            try {
                characterLoopAnimation.stop();
                CharacterManager.setCharacterFreeze(player, false);

                //UIStateManager.sendNotification(player, ToastTypeEnum.SUCCESS,
                //        "Vous avez récolté " + this.harvestableObject.getName() + " en " + (this.harvestableObject.getBaseHarvestTime() / 1000) + "s");

                JobManager.addExp(player, job.getJobType(), this.harvestableObject.getXp());
                this.harvestableObject.onHarvestDone(player, this);

            }catch (Exception ex) {
                Onset.print("Error when harvesting the object: " + ex.toString());
            }
            this.delete();
        });
    }

    /**
     * Delete the resource
     */
    public void delete() {
        this.worldObject.destroy();
        this.job.getWorldHarvestObjects().remove(this);
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getUuid() {
        return uuid;
    }

    public HarvestableObject getHarvestableObject() {
        return harvestableObject;
    }

    public Job getJob() {
        return job;
    }

    public JobSpawnPosition getJobSpawnPosition() {
        return jobSpawnPosition;
    }
}
