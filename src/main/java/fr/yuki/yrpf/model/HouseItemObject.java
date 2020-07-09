package fr.yuki.yrpf.model;

import eu.bebendorf.ajorm.Model;
import eu.bebendorf.ajorm.annotation.Column;
import eu.bebendorf.ajorm.annotation.Table;
import fr.yuki.yrpf.house.itembehavior.ATMBehavior;
import fr.yuki.yrpf.house.itembehavior.HouseChestBehavior;
import fr.yuki.yrpf.house.itembehavior.ItemBehavior;
import fr.yuki.yrpf.house.itembehavior.RadioBehavior;
import fr.yuki.yrpf.manager.ModdingManager;
import lombok.Getter;
import lombok.Setter;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.WorldObject;

@Getter @Setter @Table("tbl_house_item")
public class HouseItemObject extends Model {
    @Column(column = "id_house_item")
    private int id;
    @Column
    private int modelId;
    @Column
    private int functionId;
    @Column
    private double x;
    @Column
    private double y;
    @Column
    private double z;
    @Column
    private double rx;
    @Column
    private double ry;
    @Column
    private double rz;

    private WorldObject worldObject;
    private House house;
    private ItemBehavior itemBehavior;

    public void spawn() {
        this.worldObject = Onset.getServer().createObject(this.getPosition(), this.getModelId());
        if(ModdingManager.isCustomModelId(this.getModelId())) ModdingManager.assignCustomModel(this.worldObject, this.getModelId());
        this.worldObject.setRotation(this.getRotation());
        this.worldObject.setProperty("houseItemId", this.id, true);
        this.worldObject.setStreamDistance(2500);

        if(this.functionId != -1) {
            switch (this.functionId) {
                case 1: // Radio
                    this.itemBehavior = new RadioBehavior(this);
                    break;

                case 2: // ATM
                    this.itemBehavior = new ATMBehavior(this);
                    break;

                case 3: // Chest
                    this.itemBehavior = new HouseChestBehavior(this);
                    break;
            }

            if(this.itemBehavior != null) {
                this.itemBehavior.onSpawn();
            }
        }
    }

    public void destroy() {
        this.worldObject.destroy();

        if(this.itemBehavior != null) {
            this.itemBehavior.onDestroy();
        }
    }

    public WorldObject getWorldObject() {
        return worldObject;
    }

    public Vector getPosition() {
        return new Vector(this.x, this.y, this.z);
    }

    public Vector getRotation() {
        return new Vector(this.rx, this.ry, this.rz);
    }

    public void setPosition(Vector position){
        x = position.getX();
        y = position.getY();
        z = position.getZ();
    }

    public void setRotation(Vector rotation){
        rx = rotation.getX();
        ry = rotation.getY();
        rz = rotation.getZ();
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }
}
