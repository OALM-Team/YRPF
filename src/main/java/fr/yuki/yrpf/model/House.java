package fr.yuki.yrpf.model;

import eu.bebendorf.ajorm.Model;
import eu.bebendorf.ajorm.annotation.Column;
import eu.bebendorf.ajorm.annotation.Table;
import fr.yuki.yrpf.modding.Line3D;
import lombok.Getter;
import lombok.Setter;
import net.onfirenetwork.onsetjava.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @Table("tbl_house")
public class House extends Model {
    @Column(column = "id_house")
    private int id;
    @Column(column = "id_account")
    private int accountId;
    @Column
    private int price;
    @Column
    private String name;
    @Column
    private double sx;
    @Column
    private double sy;
    @Column
    private double sz;
    @Column
    private double ex;
    @Column
    private double ey;
    @Column
    private double ez;

    private boolean locked = true;
    private List<Integer> allowedPlayers = new ArrayList<>();
    private List<HouseItemObject> houseItemObjects = new ArrayList<>();

    public HouseItemObject getNearbyHouseItem(Player player) {
        for(HouseItemObject houseItemObject : houseItemObjects) {
            if(houseItemObject.getPosition().distance(player.getLocation()) < 180) {
                return houseItemObject;
            }
        }
        return null;
    }

    public Line3D getLine3D() {
       return new Line3D(this.sx, this.sy, this.sz, this.ex, this.ey, this.ez, 5);
    }

    public List<HouseItemObject> getHouseItemObjects() {
        return houseItemObjects;
    }

    public void setHouseItemObjects(ArrayList<HouseItemObject> houseItemObjects) {
        this.houseItemObjects = houseItemObjects;
    }

    public List<Integer> getAllowedPlayers() {
        return allowedPlayers;
    }

    public void setAllowedPlayers(ArrayList<Integer> allowedPlayers) {
        this.allowedPlayers = allowedPlayers;
    }
}
