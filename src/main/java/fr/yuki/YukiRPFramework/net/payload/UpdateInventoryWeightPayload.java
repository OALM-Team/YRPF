package fr.yuki.YukiRPFramework.net.payload;

public class UpdateInventoryWeightPayload {
    private String type;
    private float currentWeight;
    private float maxWeight;

    public UpdateInventoryWeightPayload(float currentWeight, float maxWeight) {
        this.type = "UPDATE_INVENTORY_WEIGHT";
        this.currentWeight = currentWeight;
        this.maxWeight = maxWeight;
    }

    public String getType() {
        return type;
    }

    public float getCurrentWeight() {
        return currentWeight;
    }

    public float getMaxWeight() {
        return maxWeight;
    }
}
