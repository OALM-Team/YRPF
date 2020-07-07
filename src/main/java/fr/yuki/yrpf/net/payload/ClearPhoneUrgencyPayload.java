package fr.yuki.yrpf.net.payload;

public class ClearPhoneUrgencyPayload {
    private String type;

    public ClearPhoneUrgencyPayload() {
        this.type = "CLEAR_PHONE_URGENCY";
    }

    public String getType() {
        return type;
    }
}
