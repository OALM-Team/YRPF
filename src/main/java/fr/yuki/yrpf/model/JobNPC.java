package fr.yuki.yrpf.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import eu.bebendorf.ajorm.Model;
import eu.bebendorf.ajorm.annotation.Column;
import eu.bebendorf.ajorm.annotation.Table;
import fr.yuki.yrpf.job.WearableWorldObject;
import lombok.Getter;
import lombok.Setter;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.NPC;
import net.onfirenetwork.onsetjava.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @Table("tbl_job_npc")
public class JobNPC extends Model {
    @Column(column = "id_job_npc")
    private int id;
    @Column(column = "id_job")
    private String jobId;
    @Column
    private String name;
    @Column
    private double x;
    @Column
    private double y;
    @Column
    private double z;
    @Column
    private double h;
    @Column
    private int npcClothing;
    @Column(size = 0)
    private String buyList;
    @Column(size = 0)
    private String sellList;

    private List<JobNPCListItem> buyListCache;
    private List<JobNPCListItem> sellListCache;
    private NPC npc;

    public List<JobNPCListItem> getBuyList() {
        if(buyListCache == null){
            buyListCache = new Gson().fromJson(buyList, new TypeToken<ArrayList<JobNPCListItem>>(){}.getType());
        }
        return buyListCache;
    }

    public void setBuyList(List<JobNPCListItem> buyList) {
        this.buyList = new Gson().toJson(buyList);
        buyListCache = buyList;
    }

    public List<JobNPCListItem> getSellList() {
        if(sellListCache == null){
            sellListCache = new Gson().fromJson(sellList, new TypeToken<ArrayList<JobNPCListItem>>(){}.getType());
        }
        return sellListCache;
    }

    public void setSellList(List<JobNPCListItem> sellList) {
        this.sellList = new Gson().toJson(sellList);
        sellListCache = sellList;
    }

    public JobNPCListItem getBuyItemByWearableItem(WearableWorldObject wearableWorldObject) {
        JobNPCListItem jobNPCListItem = null;
        for(JobNPCListItem item : getBuyList()) {
            if(item.getType().toLowerCase().equals("worlditem") && item.getItemId() == wearableWorldObject.getModelId()) {
                return item;
            }
        }
        return null;
    }

    public boolean isNear(Player player) {
        return new Vector(x,y,z).distance(player.getLocation()) < 200;
    }
}
