package fr.yuki.yrpf.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import eu.bebendorf.ajorm.Model;
import eu.bebendorf.ajorm.annotation.Column;
import eu.bebendorf.ajorm.annotation.Table;
import lombok.Getter;
import lombok.Setter;
import net.onfirenetwork.onsetjava.data.Location;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @Table("tbl_vehicle_seller")
public class VehicleSeller extends Model {
    @Column(column = "id_vehicle_seller")
    private int id;
    @Column
    private String name;
    @Column
    private int npcClothing;
    @Column
    private double x;
    @Column
    private double y;
    @Column
    private double z;
    @Column
    private double h;
    @Column
    private double sX;
    @Column
    private double sY;
    @Column
    private double sZ;
    @Column
    private double sH;
    @Column(size = 0)
    private String sellList;
    private List<SellListItem> sellListCache;

    public Location getLocation(){
        return new Location(x, y, z, h);
    }

    public Location getSpawnLocation(){
        return new Location(sX, sY, sZ, sH);
    }

    public List<SellListItem> getSellList(){
        if(sellListCache == null){
            sellListCache = new Gson().fromJson(sellList,
                    new TypeToken<ArrayList<SellListItem>>(){}.getType());
        }
        return sellListCache;
    }

    public void setSellList(List<SellListItem> sellList) {
        this.sellList = new Gson().toJson(sellList);
        sellListCache = sellList;
    }

    public boolean isNear(Player player) {
        if(player.getLocation().distance(new Vector(this.x, this.y, this.z)) < 250) {
            return true;
        }
        return false;
    }
}
