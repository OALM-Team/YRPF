package fr.yuki.yrpf.job;

import java.util.UUID;

public class JobSpawnPosition {
    private final String uuid;
    private double x;
    private double y;
    private double z;

    public JobSpawnPosition() {
        this.uuid = UUID.randomUUID().toString();
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

    public String getUuid() {
        return uuid;
    }
}
