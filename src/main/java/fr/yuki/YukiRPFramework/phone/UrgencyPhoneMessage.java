package fr.yuki.YukiRPFramework.phone;

public class UrgencyPhoneMessage extends PhoneMessage {
    private int id;
    private String service;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
