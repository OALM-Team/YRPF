package fr.yuki.yrpf.modding;

import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Door;
import net.onfirenetwork.onsetjava.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class Line3D {
    private String id;
    private double sX;
    private double sY;
    private double sZ;
    private double eX;
    private double eY;
    private double eZ;
    private int thickness;

    public Line3D(double sX, double sY, double sZ, double eX, double eY, double eZ, int thickness) {
        this.id = UUID.randomUUID().toString();
        this.sX = sX;
        this.sY = sY;
        this.sZ = sZ;
        this.eX = eX;
        this.eY = eY;
        this.eZ = eZ;
        this.thickness = thickness;
    }

    public void show(Player player) {
        player.callRemoteEvent("World:AddLine3D", this.id, this.sX, this.sY, this.sZ, this.eX, this.eY, this.eZ, this.thickness);
    }

    public void hide(Player player) {
        player.callRemoteEvent("World:DeleteLine3D", this.id);
    }

    public boolean isInside(Vector origin) {
        //double szMax = this.sZ + height;
        //double ezMax = this.eZ + height;
        return (origin.getX() < this.sX && origin.getX() > this.eX && origin.getY() < this.sY && origin.getY() > this.eY);
    }

    public ArrayList<Door> getDoorsInside() {
        ArrayList<Door> doors = new ArrayList<>();
        for(Door d : Onset.getDoors()) {
            if((d.getLocation().getX() < this.sX && d.getLocation().getX() > this.eX &&
                    d.getLocation().getY() < this.sY && d.getLocation().getY() > this.eY)) {
                doors.add(d);
            }
        }
        return doors;
    }

    public double getsX() {
        return sX;
    }

    public double getsY() {
        return sY;
    }

    public double getsZ() {
        return sZ;
    }

    public double geteX() {
        return eX;
    }

    public double geteY() {
        return eY;
    }

    public double geteZ() {
        return eZ;
    }
}
