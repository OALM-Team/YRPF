package fr.yuki.yrpf.model;

import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;

public class OutfitPoint {
    private int id;
    private double x;
    private double y;
    private double z;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public boolean isNear(Player player) {
        if(player.getLocation().distance(new Vector(this.x, this.y, this.z)) < 250) {
            return true;
        }
        return false;
    }
}
