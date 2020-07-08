package fr.yuki.yrpf.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import eu.bebendorf.ajorm.Model;
import eu.bebendorf.ajorm.annotation.Column;
import eu.bebendorf.ajorm.annotation.Table;
import lombok.Getter;
import lombok.Setter;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @Table("tbl_job_outfit")
public class JobOutfit extends Model {
    @Column(column = "id_job_outfit")
    private int id;
    @Column(column = "id_job")
    private String jobId;
    @Column
    private int levelRequired;
    @Column
    private String name;
    @Column
    private String outfit;
    @Column
    private double x;
    @Column
    private double y;
    @Column
    private double z;

    public boolean isNear(Player player) {
        if(player.getLocation().distance(new Vector(this.x, this.y, this.z)) < 250) return true;
        return false;
    }

    public List<JobOutfitItem> decodeOutfit() {
        return new Gson().fromJson(this.outfit, new TypeToken<ArrayList<JobOutfitItem>>(){}.getType());
    }
}
