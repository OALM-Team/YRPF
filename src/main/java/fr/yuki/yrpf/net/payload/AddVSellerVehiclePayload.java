package fr.yuki.yrpf.net.payload;

public class AddVSellerVehiclePayload {
    private String type;
    private int modelId;
    private int price;
    private String name;
    private String description;

    public AddVSellerVehiclePayload(int modelId, int price, String name, String description) {
        this.modelId = modelId;
        this.price = price;
        this.name = name;
        this.description = description;
        this.type = "ADD_VSELLER_VEHICLE";
    }

    public String getType() {
        return type;
    }

    public int getModelId() {
        return modelId;
    }

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
