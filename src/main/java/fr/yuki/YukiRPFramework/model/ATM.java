package fr.yuki.YukiRPFramework.model;

import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Pickup;
import net.onfirenetwork.onsetjava.entity.Player;

public class ATM {
    private int id;
    private float x;
    private float y;
    private float z;
    private Pickup pickup;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public Pickup getPickup() {
        return pickup;
    }

    public void setPickup(Pickup pickup) {
        this.pickup = pickup;
    }

    public boolean isNear(Player player) {
        if(player.getLocation().distance(new Vector(this.x, this.y, this.z)) < 150) {
            return true;
        }
        return false;
    }
}
