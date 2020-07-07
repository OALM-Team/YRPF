package fr.yuki.yrpf.model;

import fr.yuki.yrpf.job.WearableWorldObject;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.NPC;
import net.onfirenetwork.onsetjava.entity.Player;

import java.util.ArrayList;

public class JobNPC {
    private int id;
    private String jobId;
    private String name;
    private double x;
    private double y;
    private double z;
    private double h;
    private int npcClothing;
    private ArrayList<JobNPCListItem> buyList;
    private ArrayList<JobNPCListItem> sellList;
    private NPC npc;

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

    public ArrayList<JobNPCListItem> getBuyList() {
        return buyList;
    }

    public void setBuyList(ArrayList<JobNPCListItem> buyList) {
        this.buyList = buyList;
    }

    public ArrayList<JobNPCListItem> getSellList() {
        return sellList;
    }

    public void setSellList(ArrayList<JobNPCListItem> sellList) {
        this.sellList = sellList;
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

    public JobNPCListItem getBuyItemByWearableItem(WearableWorldObject wearableWorldObject) {
        JobNPCListItem jobNPCListItem = null;
        for(JobNPCListItem item : this.buyList) {
            if(item.getType().toLowerCase().equals("worlditem") && item.getItemId() == wearableWorldObject.getModelId()) {
                return item;
            }
        }
        return null;
    }

    public boolean isNear(Player player) {
        return new Vector(x,y,z).distance(player.getLocation()) < 200;
    }

    public NPC getNpc() {
        return npc;
    }

    public void setNpc(NPC npc) {
        this.npc = npc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
