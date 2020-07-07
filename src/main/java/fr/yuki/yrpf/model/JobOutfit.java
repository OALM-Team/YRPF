package fr.yuki.yrpf.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;

import java.util.ArrayList;

public class JobOutfit {
    private int id;
    private String jobId;
    private int levelRequired;
    private String name;
    private String outfit;
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

    public int getLevelRequired() {
        return levelRequired;
    }

    public void setLevelRequired(int levelRequired) {
        this.levelRequired = levelRequired;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOutfit() {
        return outfit;
    }

    public void setOutfit(String outfit) {
        this.outfit = outfit;
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
        if(player.getLocation().distance(new Vector(this.x, this.y, this.z)) < 250) return true;
        return false;
    }

    public ArrayList<JobOutfitItem> decodeOutfit() {
        return new Gson().fromJson(this.outfit, new TypeToken<ArrayList<JobOutfitItem>>(){}.getType());
    }
}
