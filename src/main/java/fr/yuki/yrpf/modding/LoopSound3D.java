package fr.yuki.yrpf.modding;

import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.WorldObject;

public class LoopSound3D {
    private WorldObject dummyObject;
    private String path;
    private Vector position;
    private int radius;
    private double volume;
    private int loopInterval;
    private boolean isActive;

    public LoopSound3D(String path, Vector position, int radius, double volume, int loopInterval) {
        this.path = path;
        this.position = position;
        this.radius = radius;
        this.volume = volume;
        this.loopInterval = loopInterval;
        this.isActive = false;
    }

    public void start() {
        this.isActive = true;
        this.tickRestart();
    }

    private void createDummy() {
        this.dummyObject = Onset.getServer().createObject(new Vector(0, 0, 0), 1);
        this.dummyObject.setProperty("ambiantSoundObject", 1, true);
        this.dummyObject.setProperty("ambiantSoundName", this.path, true);
        this.dummyObject.setProperty("ambiantSoundRadius", radius, true);
        this.dummyObject.setProperty("ambiantSoundVolume", volume, true);
        this.dummyObject.setLocation(position);
        this.dummyObject.setStreamDistance(radius);
    }

    private void destroyDummy() {
        if(this.dummyObject == null) return;
        this.dummyObject.destroy();
        this.dummyObject = null;
    }

    public void tickRestart() {
        this.destroyDummy();
        this.createDummy();
        Onset.delay(this.loopInterval, () -> {
            if(this.dummyObject == null || !this.isActive) return;
            this.tickRestart();
        });
    }

    public void stop() {
        this.destroyDummy();
        this.isActive = false;
    }

    public boolean isActive() {
        return isActive;
    }
}
