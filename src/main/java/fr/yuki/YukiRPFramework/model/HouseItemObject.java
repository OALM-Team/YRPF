package fr.yuki.YukiRPFramework.model;

import fr.yuki.YukiRPFramework.house.itembehavior.ATMBehavior;
import fr.yuki.YukiRPFramework.house.itembehavior.ItemBehavior;
import fr.yuki.YukiRPFramework.house.itembehavior.RadioBehavior;
import fr.yuki.YukiRPFramework.manager.ModdingManager;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.WorldObject;

public class HouseItemObject {
    private int id;
    private int modelId;
    private int functionId;
    private double x;
    private double y;
    private double z;
    private double rx;
    private double ry;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public int getFunctionId() {
        return functionId;
    }

    public void setFunctionId(int functionId) {
        this.functionId = functionId;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getRx() {
        return rx;
    }

    public void setRx(double rx) {
        this.rx = rx;
    }

    public double getRy() {
        return ry;
    }

    public void setRy(double ry) {
        this.ry = ry;
    }

    public double getRz() {
        return rz;
    }

    public void setRz(double rz) {
        this.rz = rz;
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

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }
}
