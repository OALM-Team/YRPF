package fr.yuki.yrpf.model;

import eu.bebendorf.ajorm.Model;
import eu.bebendorf.ajorm.annotation.Column;
import eu.bebendorf.ajorm.annotation.Table;
import lombok.Getter;
import lombok.Setter;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Pickup;
import net.onfirenetwork.onsetjava.entity.Player;

@Getter @Setter @Table("tbl_atm")
public class ATM extends Model {
    @Column(column = "id_atm")
    private int id;
    @Column
    private double x;
    @Column
    private double y;
    @Column
    private double z;
    private Pickup pickup;
    private boolean canBeRob = false;
    private double lastRobTime = 0;

    public boolean isNear(Player player) {
        if(player.getLocation().distance(new Vector(this.x, this.y, this.z)) < 150) {
            return true;
        }
        return false;
    }
}
