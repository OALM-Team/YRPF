package fr.yuki.yrpf.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import eu.bebendorf.ajorm.Model;
import eu.bebendorf.ajorm.annotation.Column;
import eu.bebendorf.ajorm.annotation.Dates;
import eu.bebendorf.ajorm.annotation.Table;
import lombok.Getter;
import lombok.Setter;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Vehicle;

import java.sql.Timestamp;
import java.util.ArrayList;

@Getter @Setter @Dates @Table("tbl_vehicle_garage")
public class VehicleGarage extends Model {
    @Column(column = "id_vehicle_garage")
    private int vehicleGarageId;
    @Column
    private String uuid;
    @Column
    private int owner;
    @Column(column = "id_garage")
    private int garageId;
    @Column(column = "id_last_garage")
    private int garageLastId;
    @Column
    private int modelId;
    @Column
    private String damage;
    @Column
    private double health;
    @Column
    private String licencePlate;
    @Column
    private String color;
    @Column
    private Timestamp createdAt;
    @Column
    private Timestamp updatedAt;

    private boolean rental;

    public void save() {
        if(!rental) super.save();
    }

    public void destroy() {
        for(Vehicle vehicle : Onset.getVehicles()) {
            if(vehicle.getPropertyString("uuid").equals(this.getUuid())) {
                vehicle.destroy();
                return;
            }
        }
    }

    public Vehicle getVehicle() {
        for(Vehicle vehicle : Onset.getVehicles()) {
            if(vehicle.getPropertyString("uuid").equals(this.getUuid())) {
                return vehicle;
            }
        }
        return null;
    }

    public void applyDamages(Vehicle vehicle) {
        ArrayList<Double> damages = new Gson().fromJson(this.damage, new TypeToken<ArrayList<Double>>(){}.getType());
        for(int i = 0; i < damages.size(); i++) {
            vehicle.setDamage(i + 1, damages.get(i));
        }
        vehicle.setHealth(this.health);
    }

    public void computeDamages(Vehicle vehicle) {
        this.health = vehicle.getHealth();
        ArrayList<Double> damages = new ArrayList<>();
        for(int i = 0; i < 8; i++) {
            damages.add(vehicle.getDamage(i + 1));
        }
        this.damage = new Gson().toJson(damages);
    }

}
