package fr.yuki.YukiRPFramework.model;

import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;

import java.util.ArrayList;

public class VehicleSeller {
    private int id;
    private String name;
    private int npcClothing;
    private double x;
    private double y;
    private double z;
    private double h;
    private ArrayList<SellListItem> sellList;

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

    public int getNpcClothing() {
        return npcClothing;
    }

    public void setNpcClothing(int npcClothing) {
        this.npcClothing = npcClothing;
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

    public ArrayList<SellListItem> getSellList() {
        return sellList;
    }

    public void setSellList(ArrayList<SellListItem> sellList) {
        this.sellList = sellList;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public boolean isNear(Player player) {
        if(player.getLocation().distance(new Vector(this.x, this.y, this.z)) < 150) {
            return true;
        }
        return false;
    }
}
