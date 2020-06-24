package fr.yuki.YukiRPFramework.net.payload;

public class ClearGenericMenuPayload {
    private String type;

    public ClearGenericMenuPayload() {
        this.type = "CLEAR_GENERIC_MENU";
    }

    public String getType() {
        return type;
    }
}
