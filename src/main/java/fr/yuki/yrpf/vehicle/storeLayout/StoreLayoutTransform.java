package fr.yuki.yrpf.vehicle.storeLayout;

import net.onfirenetwork.onsetjava.data.Vector;

public class StoreLayoutTransform {
    private int index;
    private Vector position;
    private Vector rotation;
    private Vector scale;

    public StoreLayoutTransform(int index, Vector position, Vector rotation, Vector scale) {
        this.index = index;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public Vector getPosition() {
        return position;
    }

    public Vector getRotation() {
        return rotation;
    }

    public Vector getScale() {
        return scale;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
