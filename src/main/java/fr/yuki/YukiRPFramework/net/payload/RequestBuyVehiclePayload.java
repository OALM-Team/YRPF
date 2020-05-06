package fr.yuki.YukiRPFramework.net.payload;

import java.awt.*;

public class RequestBuyVehiclePayload {
    private int modelId;
    private String color;

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Color getAWTColor() {
        return Color.decode(this.color);
    }
}
