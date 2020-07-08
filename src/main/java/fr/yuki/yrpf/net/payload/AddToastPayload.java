package fr.yuki.yrpf.net.payload;

public class AddToastPayload {
    private String type;
    private String notifType;
    private String message;

    public AddToastPayload(String notifType, String message) {
        this.notifType = notifType;
        this.message = message;
        this.type = "ADD_TOAST";
    }

    public String getType() {
        return type;
    }

    public String getNotifType() {
        return notifType;
    }

    public String getMessage() {
        return message;
    }
}
