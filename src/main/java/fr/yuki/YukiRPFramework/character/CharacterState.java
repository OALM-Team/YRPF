package fr.yuki.YukiRPFramework.character;

import fr.yuki.YukiRPFramework.job.ObjectPlacementInstance;
import fr.yuki.YukiRPFramework.job.WearableWorldObject;
import net.onfirenetwork.onsetjava.entity.Player;

public class CharacterState {
    private WearableWorldObject wearableWorldObject;
    private boolean isDead = false;
    private boolean firstSpawn = true;
    private ObjectPlacementInstance currentObjectPlacementInstance = null;

    public CharacterState() {}

    public WearableWorldObject getWearableWorldObject() {
        return wearableWorldObject;
    }

    public void setWearableWorldObject(Player player, WearableWorldObject wearableWorldObject) {
        this.wearableWorldObject = wearableWorldObject;
        if(this.wearableWorldObject != null) {
            player.setProperty("wearId", this.wearableWorldObject.getUuid(), true);
        } else {
            player.setProperty("wearId", "", true);
        }
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public boolean isFirstSpawn() {
        return firstSpawn;
    }

    public void setFirstSpawn(boolean firstSpawn) {
        this.firstSpawn = firstSpawn;
    }

    public ObjectPlacementInstance getCurrentObjectPlacementInstance() {
        return currentObjectPlacementInstance;
    }

    public void setCurrentObjectPlacementInstance(ObjectPlacementInstance currentObjectPlacementInstance) {
        this.currentObjectPlacementInstance = currentObjectPlacementInstance;
    }
}
