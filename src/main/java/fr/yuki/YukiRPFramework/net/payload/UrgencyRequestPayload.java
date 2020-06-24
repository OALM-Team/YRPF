package fr.yuki.YukiRPFramework.net.payload;

public class UrgencyRequestPayload {
    private String service;
    private String text;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
