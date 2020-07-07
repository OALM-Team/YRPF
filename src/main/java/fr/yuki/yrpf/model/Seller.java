package fr.yuki.yrpf.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.NPC;
import net.onfirenetwork.onsetjava.entity.Player;

import java.util.ArrayList;

public class Seller {
    private int id;
    private String name;
    private double x;
    private double y;
    private double z;
    private double h;
    private int npcClothing;
    private String itemList;
    private String jobRequired;
    private int jobLevelRequired;
    private NPC npc;

    public ArrayList<SellerItem> decodeItems() {
        return new Gson().fromJson(this.itemList,
                new TypeToken<ArrayList<SellerItem>>(){}.getType());
    }

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

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public int getNpcClothing() {
        return npcClothing;
    }

    public void setNpcClothing(int npcClothing) {
        this.npcClothing = npcClothing;
    }

    public String getItemList() {
        return itemList;
    }

    public void setItemList(String itemList) {
        this.itemList = itemList;
    }

    public NPC getNpc() {
        return npc;
    }

    public void setNpc(NPC npc) {
        this.npc = npc;
    }

    public String getJobRequired() {
        return jobRequired;
    }

    public void setJobRequired(String jobRequired) {
        this.jobRequired = jobRequired;
    }

    public int getJobLevelRequired() {
        return jobLevelRequired;
    }

    public void setJobLevelRequired(int jobLevelRequired) {
        this.jobLevelRequired = jobLevelRequired;
    }

    public boolean isNear(Player player) {
        if(player.getLocation().distance(new Vector(this.x, this.y, this.z)) < 250) {
            return true;
        }
        return false;
    }
}
