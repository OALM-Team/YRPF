package fr.yuki.yrpf.net.payload;

public class ClearChestContentPayload {
    private String type;

    public ClearChestContentPayload() {
        this.type = "CLEAR_CHEST_CONTENT";
    }

    public String getType() {
        return type;
    }
}
