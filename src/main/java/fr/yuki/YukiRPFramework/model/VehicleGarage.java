package fr.yuki.YukiRPFramework.model;

import fr.yuki.YukiRPFramework.dao.VehicleGarageDAO;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Vehicle;

import java.sql.SQLException;
import java.util.Date;

public class VehicleGarage {
    private int vehicleGarageId;
    private String uuid;
    private int owner;
    private int garageId;
    private int garageLastId;
    private int modelId;
    private double damage;
    private String licencePlate;
    private String color;
    private Date createdAt;
    private Date updateAt;
    private boolean isRental;

    public int getVehicleGarageId() {
        return vehicleGarageId;
    }

    public void setVehicleGarageId(int vehicleGarageId) {
        this.vehicleGarageId = vehicleGarageId;
    }

    public int getGarageId() {
        return garageId;
    }

    public void setGarageId(int garageId) {
        this.garageId = garageId;
    }

    public int getGarageLastId() {
        return garageLastId;
    }

    public void setGarageLastId(int garageLastId) {
        this.garageLastId = garageLastId;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public void save() {
        try {
            if(!isRental) VehicleGarageDAO.saveVehicleGarage(this);
        } catch (Exception ex) {
            Onset.print("Can't save vehicle: " + ex.toString());
        }
    }

    public boolean isRental() {
        return isRental;
    }

    public void setRental(boolean rental) {
        isRental = rental;
    }

    public void destroy() {
        for(Vehicle vehicle : Onset.getVehicles()) {
            if(vehicle.getPropertyString("uuid").equals(this.getUuid())) {
                vehicle.destroy();
                return;
            }
        }
    }
}
