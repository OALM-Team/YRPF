package fr.yuki.yrpf.net.payload;

public class ResetVehicleGaragePayload {
    private String type;

    public ResetVehicleGaragePayload() {
        this.type = "RESET_GARAGE_VEHICLE";
    }

    public String getType() {
        return type;
    }
}
