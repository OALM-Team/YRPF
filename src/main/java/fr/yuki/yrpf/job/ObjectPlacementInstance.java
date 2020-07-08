package fr.yuki.yrpf.job;

import fr.yuki.yrpf.manager.ModdingManager;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.entity.WorldObject;

import java.util.UUID;

public abstract class ObjectPlacementInstance {
    private String uuid;
    private WorldObject worldObject;
    private int modelId;
    private Vector spawnPoint;

    public ObjectPlacementInstance(int modelId, Vector spawnPoint) {
        this.uuid = UUID.randomUUID().toString();
        this.modelId = modelId;
        this.spawnPoint = spawnPoint;
    }

    public void spawn() {
        this.worldObject = Onset.getServer().createObject(this.spawnPoint, this.modelId);
        if(ModdingManager.isCustomModelId(this.modelId)){
            ModdingManager.assignCustomModel(this.worldObject, this.modelId);
        }
    }

    public void setEditableBy(Player player) {
        this.worldObject.setProperty("editable", 1, true);
        this.worldObject.setProperty("editable_by", player.getId(), true);
        this.worldObject.setProperty("no_collision", 1, true);
        this.worldObject.setProperty("uuid", this.uuid, true);
    }

    public abstract void onPlacementDone(Player player, Vector position, Vector rotation);

    public void destroy() {
        this.worldObject.destroy();
    }

    public String getUuid() {
        return uuid;
    }

    public int getModelId() {
        return modelId;
    }
}
