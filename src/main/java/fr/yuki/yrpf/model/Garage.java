package fr.yuki.yrpf.model;

import eu.bebendorf.ajorm.Model;
import eu.bebendorf.ajorm.annotation.Column;
import eu.bebendorf.ajorm.annotation.Table;
import lombok.Getter;
import lombok.Setter;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;

@Getter @Setter @Table("tbl_garage")
public class Garage extends Model {
    @Column(column = "id_garage")
    private int id;
    @Column
    private String name;
    @Column
    private int costToUse;
    @Column
    private double x;
    @Column
    private double y;
    @Column
    private double z;

    public boolean isNear(Player player) {
        if(player.getLocation().distance(new Vector(this.x, this.y, this.z)) < 200) {
            return true;
        }
        return false;
    }
}
