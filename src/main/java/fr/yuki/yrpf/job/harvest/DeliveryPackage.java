package fr.yuki.yrpf.job.harvest;

import fr.yuki.yrpf.character.CharacterLoopAnimation;
import fr.yuki.yrpf.character.CharacterToolAnimation;
import fr.yuki.yrpf.enums.ItemTemplateEnum;
import fr.yuki.yrpf.job.JobSpawnPosition;
import fr.yuki.yrpf.job.WearableWorldObject;
import fr.yuki.yrpf.job.WorldHarvestObject;
import fr.yuki.yrpf.job.customGoal.DeliveryPointGoal;
import fr.yuki.yrpf.manager.JobManager;
import fr.yuki.yrpf.utils.Basic;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.enums.Animation;

import java.util.ArrayList;

public class DeliveryPackage implements HarvestableObject {
    @Override
    public String getName() {
        return "Colis";
    }

    @Override
    public int getXp() {
        return 0;
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
    public Vector getScale() {
        return new Vector(1, 1, 1);
    }

    @Override
    public boolean needItemToHarvest() {
        return false;
    }

    @Override
    public int getModelId() {
        return 508;
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
                new CharacterToolAnimation(this.getModelId(), new Vector(-40,35,0), new Vector(-90,0,0),
                        new Vector(0.6,0.6,0.6), "hand_r"),
                new Vector(worldHarvestObject.getJobSpawnPosition().getX(), worldHarvestObject.getJobSpawnPosition().getY(),
                        worldHarvestObject.getJobSpawnPosition().getZ()));
        JobManager.getWearableWorldObjects().add(wearableWorldObject);

        // Assign a random delivery location place
        ArrayList<JobSpawnPosition> housesDeliveryLocation = JobManager.getDeliveryPointConfig().getPoints().get("houses");
        JobSpawnPosition deliveryPointPosition = housesDeliveryLocation.get(Basic.randomNumber(0, housesDeliveryLocation.size() -1));
        DeliveryPointGoal deliveryPointGoal = new DeliveryPointGoal(deliveryPointPosition, wearableWorldObject);
        wearableWorldObject.setDeliveryPointGoal(deliveryPointGoal);

        JobManager.handleWearObjectRequest(player, wearableWorldObject.getUuid());
    }
}
