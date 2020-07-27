package fr.yuki.yrpf.modding;

import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.WorldObject;

public class WorldParticle {
    private WorldObject dummyObject;
    private String particleAsset;
    private Vector position;
    private Vector scale;
    private double radius;

    public WorldParticle(String particleAsset, Vector position, Vector scale, double radius) {
        this.particleAsset = particleAsset;
        this.position = position;
        this.scale = scale;
        this.radius = radius;
    }

    public void start() {
        this.createDummy();
    }

    private void createDummy() {
        this.dummyObject = Onset.getServer().createObject(new Vector(0, 0, 0), 1);
        this.dummyObject.setProperty("isParticle", 1, true);
        this.dummyObject.setProperty("particleAsset", this.particleAsset, true);
        this.dummyObject.setProperty("sx", this.scale.getX(), true);
        this.dummyObject.setProperty("sy", this.scale.getY(), true);
        this.dummyObject.setProperty("sz", this.scale.getZ(), true);
        this.dummyObject.setLocation(position);
        this.dummyObject.setStreamDistance(radius);
    }

    private void destroyDummy() {
        if(this.dummyObject == null) return;
        this.dummyObject.destroy();
        this.dummyObject = null;
    }
    public void stop() {
        this.destroyDummy();
    }
}
