package fr.yuki.YukiRPFramework.model;

import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;

public class Garage {
    private int id;
    private String name;
    private int costToUse;
    private double x;
    private double y;
    private double z;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCostToUse() {
        return costToUse;
    }

    public void setCostToUse(int costToUse) {
        this.costToUse = costToUse;
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
        if(player.getLocation().distance(new Vector(this.x, this.y, this.z)) < 200) {
            return true;
        }
        return false;
    }
}
