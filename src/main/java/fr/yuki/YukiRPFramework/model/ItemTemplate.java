package fr.yuki.YukiRPFramework.model;

public class ItemTemplate {
    private int id;
    private String name;
    private String description;
    private float weight;
    private String pictureName;
    private int itemType;
    private int modelId;
    private double modelScale;
    private int foodValue;
    private int drinkValue;
    private int weaponId;
    private int ammoPerRecharge;
    private int maskId;
    private int bagId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public double getModelScale() {
        return modelScale;
    }

    public void setModelScale(double modelScale) {
        this.modelScale = modelScale;
    }

    public int getFoodValue() {
        return foodValue;
    }

    public void setFoodValue(int foodValue) {
        this.foodValue = foodValue;
    }

    public int getDrinkValue() {
        return drinkValue;
    }

    public void setDrinkValue(int drinkValue) {
        this.drinkValue = drinkValue;
    }

    public int getWeaponId() {
        return weaponId;
    }

    public void setWeaponId(int weaponId) {
        this.weaponId = weaponId;
    }

    public int getAmmoPerRecharge() {
        return ammoPerRecharge;
    }

    public void setAmmoPerRecharge(int ammoPerRecharge) {
        this.ammoPerRecharge = ammoPerRecharge;
    }

    public int getMaskId() {
        return maskId;
    }

    public void setMaskId(int maskId) {
        this.maskId = maskId;
    }

    public int getBagId() {
        return bagId;
    }

    public void setBagId(int bagId) {
        this.bagId = bagId;
    }
}
