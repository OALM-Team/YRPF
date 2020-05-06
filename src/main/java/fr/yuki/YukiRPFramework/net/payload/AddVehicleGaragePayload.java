package fr.yuki.YukiRPFramework.net.payload;

public class AddVehicleGaragePayload {
    private String type;
    private int modelId;
    private String uuid;
    private String color;
    private String plate;

    public AddVehicleGaragePayload(int modelId, String uuid, String color, String plate) {
        this.modelId = modelId;
        this.uuid = uuid;
        this.color = color;
        this.plate = plate;
        this.type = "ADD_GARAGE_VEHICLE";
    }

    public String getType() {
        return type;
    }

    public int getModelId() {
        return modelId;
    }

    public String getUuid() {
        return uuid;
    }

    public String getColor() {
        return color;
    }

    public String getPlate() {
        return plate;
    }
}
