package fr.yuki.YukiRPFramework.job;

import fr.yuki.YukiRPFramework.character.CharacterLoopAnimation;
import fr.yuki.YukiRPFramework.enums.ToastTypeEnum;
import fr.yuki.YukiRPFramework.job.harvest.HarvestableObject;
import fr.yuki.YukiRPFramework.manager.CharacterManager;
import fr.yuki.YukiRPFramework.manager.JobManager;
import fr.yuki.YukiRPFramework.manager.ModdingManager;
import fr.yuki.YukiRPFramework.manager.UIStateManager;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.entity.WorldObject;
import net.onfirenetwork.onsetjava.enums.Animation;

import java.util.UUID;

public class WorldHarvestObject {
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
        this.uuid = UUID.randomUUID().toString();
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
        if(!this.harvestableObject.checkRequirements(player)) {
            UIStateManager.sendNotification(player, ToastTypeEnum.ERROR,
                    "Toutes les conditions ne sont pas respectées pour récolter cette ressource");
            return;
        }
        if(!this.available) {
            UIStateManager.sendNotification(player, ToastTypeEnum.ERROR,
                    "Cette ressource est occupée par un autre joueur");
            return;
        }
        Onset.print("Harvest " + this.harvestableObject.getName() + " with a duration of " + this.harvestableObject.getBaseHarvestTime() + "ms");
        this.setAvailable(false);
        CharacterManager.setCharacterFreeze(player, true);
        CharacterLoopAnimation characterLoopAnimation = harvestableObject.getCharacterLoopHarvestAnimation(player);
        characterLoopAnimation.start();
        Onset.delay(this.harvestableObject.getBaseHarvestTime(), () -> {
            characterLoopAnimation.stop();
            CharacterManager.setCharacterFreeze(player, false);

            UIStateManager.sendNotification(player, ToastTypeEnum.SUCCESS,
                    "Vous avez récolté " + this.harvestableObject.getName() + " en " + (this.harvestableObject.getBaseHarvestTime() / 1000) + "s");

            JobManager.addExp(player, job.getJobType(), this.harvestableObject.getXp());
            this.harvestableObject.onHarvestDone(player, this);

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

    public JobSpawnPosition getJobSpawnPosition() {
        return jobSpawnPosition;
    }
}
