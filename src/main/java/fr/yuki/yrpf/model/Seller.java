package fr.yuki.yrpf.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import eu.bebendorf.ajorm.Model;
import eu.bebendorf.ajorm.annotation.Column;
import eu.bebendorf.ajorm.annotation.Table;
import lombok.Getter;
import lombok.Setter;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.NPC;
import net.onfirenetwork.onsetjava.entity.Player;

import java.util.ArrayList;

@Getter @Setter @Table("tbl_seller")
public class Seller extends Model {
    @Column(column = "id_seller")
    private int id;
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
    private String itemList;
    @Column
    private String jobRequired;
    @Column
    private int jobLevelRequired;

    private NPC npc;

    public ArrayList<SellerItem> decodeItems() {
        return new Gson().fromJson(this.itemList,
                new TypeToken<ArrayList<SellerItem>>(){}.getType());
    }

    public boolean isNear(Player player) {
        if(player.getLocation().distance(new Vector(this.x, this.y, this.z)) < 250) {
            return true;
        }
        return false;
    }
}
