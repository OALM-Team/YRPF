package fr.yuki.YukiRPFramework.model;

import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;

public class JobVehicleRental {
    private int id;
    private String jobId;
    private String name;
    private int vehicleModelId;
    private int cost;
    private double x;
    private double y;
    private double z;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public int getVehicleModelId() {
        return vehicleModelId;
    }

    public void setVehicleModelId(int vehicleModelId) {
        this.vehicleModelId = vehicleModelId;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return new Vector(this.x, this.y, this.z).distance(player.getLocation()) < 350;
    }
}
