package fr.yuki.YukiRPFramework.job.tools.growbox;

import fr.yuki.YukiRPFramework.job.tools.GrowBox;
import fr.yuki.YukiRPFramework.vehicle.storeLayout.StoreLayoutTransform;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.WorldObject;

import java.util.UUID;

public class Pot {
    private String uuid;
    private GrowBox growBox;
    private WorldObject worldObject;
    private int water;
    private int state;

    public Pot(GrowBox growBox) {
        this.uuid = UUID.randomUUID().toString();
        this.growBox = growBox;
        this.water = 0;
        this.state = 0;
    }

    public void spawnWorldObject(StoreLayoutTransform transform) {
        this.worldObject = Onset.getServer().createObject(new Vector(0, 0,0), 554);
        this.worldObject.attach(this.growBox.getJobTool().getWorldObject(), transform.getPosition(), transform.getRotation());
        this.worldObject.setScale(transform.getScale());
    }

    public void destroyWorldObject() {
        if(this.worldObject != null) {
            this.worldObject.destroy();
            this.worldObject = null;
        }
    }

    public String getUuid() {
        return uuid;
    }

    public int getWater() {
        return water;
    }

    public int getState() {
        return state;
    }

    public void setWater(int water) {
        this.water = water;
    }

    public void setState(int state) {
        this.state = state;
    }
}
