package fr.yuki.yrpf.net.payload;

public class UpdateChestWeightPayload {
    private String type;
    private float currentWeight;
    private float maxWeight;

    public UpdateChestWeightPayload(float currentWeight, float maxWeight) {
        this.type = "SET_CHEST_WEIGHT";
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
