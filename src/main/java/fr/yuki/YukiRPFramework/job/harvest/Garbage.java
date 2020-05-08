package fr.yuki.YukiRPFramework.job.harvest;

import fr.yuki.YukiRPFramework.character.CharacterLoopAnimation;
import fr.yuki.YukiRPFramework.character.CharacterToolAnimation;
import fr.yuki.YukiRPFramework.enums.ItemTemplateEnum;
import fr.yuki.YukiRPFramework.job.WearableWorldObject;
import fr.yuki.YukiRPFramework.job.WorldHarvestObject;
import fr.yuki.YukiRPFramework.manager.JobManager;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.enums.Animation;

import java.util.ArrayList;

public class Garbage implements HarvestableObject {
    @Override
    public String getName() {
        return "Sac poubelle";
    }

    @Override
    public int getXp() {
        return 15;
    }

    @Override
    public ArrayList<Vector> getSpawns() {
        return new ArrayList<>();
    }

    @Override
    public int getBaseHarvestTime() {
        return 1000;
    }

    @Override
    public int getLevelRequired() {
        return 1;
    }

    @Override
    public boolean needItemToHarvest() {
        return false;
    }

    @Override
    public int getModelId() {
        return 514;
    }

    @Override
    public ItemTemplateEnum getItemRequired() {
        return null;
    }

    @Override
    public int distanceToInteract() {
        return 200;
    }

    @Override
    public boolean checkRequirements(Player player) {
        return true;
    }

    @Override
    public CharacterLoopAnimation getCharacterLoopHarvestAnimation(Player player) {
        CharacterLoopAnimation characterLoopAnimation = new CharacterLoopAnimation(player, Animation.CARRY_SETDOWN, 1000, 1,
                "");
        return characterLoopAnimation;
    }

    @Override
    public void onHarvestDone(Player player, WorldHarvestObject worldHarvestObject) {
        WearableWorldObject wearableWorldObject = new WearableWorldObject(this.getModelId(),
                true, Animation.CARRY_IDLE,
                new CharacterToolAnimation(this.getModelId(), new Vector(-40,35,0), new Vector(-90,0,0), new Vector(1,1,1), "hand_r"),
                new Vector(worldHarvestObject.getJobSpawnPosition().getX(), worldHarvestObject.getJobSpawnPosition().getY(),
                        worldHarvestObject.getJobSpawnPosition().getZ()));
        JobManager.getWearableWorldObjects().add(wearableWorldObject);
        JobManager.handleWearObjectRequest(player, wearableWorldObject.getUuid());
    }
}
