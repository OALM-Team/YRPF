package fr.yuki.YukiRPFramework.net.payload;

public class GrowboxFillWaterPotPayload {
    private String growboxId;
    private String potId;

    public String getGrowboxId() {
        return growboxId;
    }

    public void setGrowboxId(String growboxId) {
        this.growboxId = growboxId;
    }

    public String getPotId() {
        return potId;
    }

    public void setPotId(String potId) {
        this.potId = potId;
    }
}
