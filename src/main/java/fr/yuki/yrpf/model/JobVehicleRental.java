package fr.yuki.yrpf.model;

import eu.bebendorf.ajorm.Model;
import eu.bebendorf.ajorm.annotation.Column;
import eu.bebendorf.ajorm.annotation.Table;
import lombok.Getter;
import lombok.Setter;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;

@Getter @Setter @Table("tbl_job_vehicle_rental")
public class JobVehicleRental extends Model {
    @Column(column = "id_job_vehicle_rental")
    private int id;
    @Column(column = "id_job")
    private String jobId;
    @Column
    private int levelRequired;
    @Column
    private String name;
    @Column
    private int vehicleModelId;
    @Column
    private int cost;
    @Column
    private double x;
    @Column
    private double y;
    @Column
    private double z;
    @Column
    private double spawnX;
    @Column
    private double spawnY;
    @Column
    private double spawnZ;
    @Column
    private String color;

    public boolean isNear(Player player) {
        return new Vector(this.x, this.y, this.z).distance(player.getLocation()) < 350;
    }
}
