package fr.yuki.YukiRPFramework.character;

import fr.yuki.YukiRPFramework.job.ObjectPlacementInstance;
import fr.yuki.YukiRPFramework.job.WearableWorldObject;
import fr.yuki.YukiRPFramework.modding.Line3D;
import fr.yuki.YukiRPFramework.ui.UIState;
import net.onfirenetwork.onsetjava.entity.Player;

public class CharacterState {
    private WearableWorldObject wearableWorldObject;
    private boolean isDead = false;
    private boolean firstSpawn = true;
    private ObjectPlacementInstance currentObjectPlacementInstance = null;
    private boolean cuffed = false;
    private UIState uiState = null;
    private Line3D currentDisplayedLine3D = null;

    public CharacterState() {
        this.uiState = new UIState();
    }

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

    public boolean isCuffed() {
        return cuffed;
    }

    public void setCuffed(boolean cuffed) {
        this.cuffed = cuffed;
    }

    public boolean canInteract() {
        if(this.isDead) return false;
        if(this.cuffed) return false;
        if(this.getCurrentObjectPlacementInstance() != null) return false;
        return true;
    }

    public UIState getUiState() {
        return uiState;
    }

    public Line3D getCurrentDisplayedLine3D() {
        return currentDisplayedLine3D;
    }

    public void setCurrentDisplayedLine3D(Line3D currentDisplayedLine3D) {
        this.currentDisplayedLine3D = currentDisplayedLine3D;
    }
}
