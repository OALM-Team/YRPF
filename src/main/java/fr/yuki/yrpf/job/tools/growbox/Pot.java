package fr.yuki.yrpf.job.tools.growbox;

import fr.yuki.yrpf.job.tools.GrowBox;
import fr.yuki.yrpf.vehicle.storeLayout.StoreLayoutTransform;
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
    private boolean seed;
    private int currentRate;
    private WorldObject seedObject;

    public Pot(GrowBox growBox) {
        this.uuid = UUID.randomUUID().toString();
        this.growBox = growBox;
        this.water = 0;
        this.state = 0;
        this.currentRate = 1;
        this.seed = false;
    }

    public void spawnWorldObject(StoreLayoutTransform transform) {
        this.worldObject = Onset.getServer().createObject(new Vector(0, 0,0), 554);
        this.worldObject.attach(this.growBox.getJobTool().getWorldObject(), transform.getPosition(), transform.getRotation());
        this.worldObject.setScale(transform.getScale());
        this.spawnSeed();
    }

    public void destroyWorldObject() {
        if(this.worldObject != null) {
            this.worldObject.destroy();
            this.worldObject = null;
        }
        if(this.seedObject != null) {
            this.seedObject.destroy();
            this.seedObject = null;
        }
    }

    public void spawnSeed() {
        if(this.seedObject != null) return;
        this.seedObject = Onset.getServer().createObject(new Vector(0, 0, 0), 63);
        this.seedObject.attach(this.worldObject, new Vector(0, 0, 18), new Vector(0, 0, 0), "None");
        double scaleRate = ((double)this.state / 100d);
        this.seedObject.setScale(new Vector(scaleRate, scaleRate, scaleRate));
    }

    public void removeSeed() {
        if(this.seedObject == null) return;
        this.seed = false;
        this.state = 0;
        this.seedObject.destroy();
        this.seedObject = null;
    }

    public boolean grow() {
        if(this.water <= 0) return false;
        if(!this.seed) return false;
        if(this.state >= 100) return false;
        if(this.seedObject == null) return false;
        this.water -= 2;
        if(this.water < 0) this.water = 0;
        this.state += this.currentRate;
        if(this.state > 100) this.state = 100;

        double scaleRate = ((double)this.state / 100d);
        this.seedObject.setScale(new Vector(scaleRate, scaleRate, scaleRate));
        return true;
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

    public boolean isSeed() {
        return seed;
    }

    public void setSeed(boolean seed) {
        this.seed = seed;
    }

    public int getCurrentRate() {
        return currentRate;
    }

    public void setCurrentRate(int currentRate) {
        this.currentRate = currentRate;
    }
}
